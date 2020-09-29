package com.dsige.dsigeproyectos.ui.fragments.engie

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.Query
import com.dsige.dsigeproyectos.data.local.model.engie.Almacen
import com.dsige.dsigeproyectos.data.local.model.engie.Material
import com.dsige.dsigeproyectos.data.local.model.engie.RegistroSolicitudMaterial
import com.dsige.dsigeproyectos.data.viewModel.MaterialViewModel
import com.dsige.dsigeproyectos.data.viewModel.SolicitudViewModel
import com.dsige.dsigeproyectos.data.viewModel.UsuarioViewModel
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.adapters.AlmacenAdapter
import com.dsige.dsigeproyectos.ui.adapters.MaterialPaginationAdapter
import com.dsige.dsigeproyectos.ui.adapters.SolicitudMaterialRegistroAdapter
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_solicitud_material.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

class SolicitudMaterialFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        if (estadoSolicitud == "105") {
            when (v.id) {
                R.id.editTextAlmacen -> if (q.obraId.isNotEmpty()) {
                    spinnerAlmacenDialog()
                } else {
                    solicitudViewModel.setError("Completar primer formulario")
                }
                R.id.editTextMaterial -> if (m.almacenId.isNotEmpty()) {
                    spinnerStockMaterialDialog()
                } else {
                    solicitudViewModel.setError("Eliga un Almacen")
                }
                R.id.fabRegisterMaterial -> formRegistroMaterial()
                R.id.fabAprobation -> getAprobationSolicitud()
                R.id.fabAnnulment -> getAnnulmentSolicitud()
            }
        } else {
            solicitudViewModel.setError("Solicitud en progreso...")
        }
    }

    private var solicitudId: Int = 0
    private var tipoMaterialSolicitud: Int = 0
    private var tipoSolicitudId: Int = 0
    private var stock: Double = 0.0
    private var estadoSolicitud: String = ""

    lateinit var q: Query
    lateinit var m: RegistroSolicitudMaterial

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var solicitudViewModel: SolicitudViewModel
    lateinit var materialViewModel: MaterialViewModel
    lateinit var usuarioViewModel: UsuarioViewModel
    lateinit var builder: AlertDialog.Builder
    var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        q = Query()
        m = RegistroSolicitudMaterial()
        arguments?.let {
            solicitudId = it.getInt(ARG_PARAM1)
            tipoMaterialSolicitud = it.getInt(ARG_PARAM2)
            tipoSolicitudId = it.getInt(ARG_PARAM3)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_solicitud_material, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        solicitudViewModel =
            ViewModelProvider(this, viewModelFactory).get(SolicitudViewModel::class.java)
        materialViewModel =
            ViewModelProvider(this, viewModelFactory).get(MaterialViewModel::class.java)
        usuarioViewModel =
            ViewModelProvider(this, viewModelFactory).get(UsuarioViewModel::class.java)
        editTextAlmacen.setOnClickListener(this)
        editTextMaterial.setOnClickListener(this)
        fabRegisterMaterial.setOnClickListener(this)
        fabAprobation.setOnClickListener(this)
        fabAnnulment.setOnClickListener(this)
        bindUI()
        menssage()
        success()
    }

    private fun bindUI() {
        val layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager

        q.tipoSolicitud = tipoSolicitudId
        q.tipoMaterialSolicitud = tipoMaterialSolicitud

        usuarioViewModel.user.observe(viewLifecycleOwner, Observer { u ->
            q.usuarioId = u.usuarioId
            q.centroCostoId = u.centroId
            m.usuarioId = u.usuarioId
        })

        solicitudViewModel.getSolicitudById(solicitudId).observe(viewLifecycleOwner, Observer { g ->
            if (g != null) {
                m.solicitudId = g.solicitudId
                m.identity = g.identity
                q.obraId = g.obraTd
                q.personalDni = g.dniPersonal
                estadoSolicitud = g.pubEstadoCodigo
                if (estadoSolicitud != "105") {
                    fabAprobation.visibility = View.GONE
                    fabAnnulment.visibility = View.GONE
                }
            } else {
                estadoSolicitud = "105"
            }
        })

        val materialRegistroAdapter =
            SolicitudMaterialRegistroAdapter(object :
                OnItemClickListener.SolicitudRegistroMaterialListener {
                override fun onItemClick(m: RegistroSolicitudMaterial, view: View, position: Int) {
                    if (estadoSolicitud == "105") {
                        when (view.id) {
                            R.id.buttonEdit -> editCantidad(m)
                            R.id.buttonDelete -> deleteRegistroMaterial(m)
                        }
                    } else {
                        solicitudViewModel.setError("Solicitud en progreso...")
                    }
                }
            })

        recyclerView.adapter = materialRegistroAdapter
        solicitudViewModel.getRegistroMaterialByFK(solicitudId)
            .observe(viewLifecycleOwner, Observer { s ->
                if (s != null) {
                    if (s.isNotEmpty()) {
                        if (estadoSolicitud != "105") {
                            fabAprobation.visibility = View.GONE
                            fabAnnulment.visibility = View.GONE
                        } else {
                            fabAprobation.visibility = View.VISIBLE
                            fabAnnulment.visibility = View.VISIBLE
                        }
                    } else {
                        fabAprobation.visibility = View.GONE
                        fabAnnulment.visibility = View.GONE
                    }
                    materialRegistroAdapter.addItems(s)
                }
            })
    }

    companion object {
        @JvmStatic
        fun newInstance(solicitudId: Int, tipoMaterialSolicitud: Int, tipoSolicitudId: Int) =
            SolicitudMaterialFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, solicitudId)
                    putInt(ARG_PARAM2, tipoMaterialSolicitud)
                    putInt(ARG_PARAM3, tipoSolicitudId)
                }
            }
    }

    private fun spinnerAlmacenDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_combo, null)
        val progressBar: ProgressBar = v.findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE
        val textViewTitulo: TextView = v.findViewById(R.id.textViewTitulo)
        val editTextSearch: TextInputEditText = v.findViewById(R.id.editTextSearch)
        val recyclerView: RecyclerView = v.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(v.context)
        textViewTitulo.text = String.format("%s", "Almacenes")
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(
            DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        )
        recyclerView.layoutManager = layoutManager
        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        val almacenAdapter = AlmacenAdapter(object : OnItemClickListener.AlmacenListener {
            override fun onItemClick(a: Almacen, view: View, position: Int) {
                m.almacenId = a.codigo
                q.almacenId = a.codigo
                editTextAlmacen.setText(a.descripcion)
                dialog.dismiss()
            }
        })
        recyclerView.adapter = almacenAdapter
        solicitudViewModel.getAlmacenByTipo(tipoMaterialSolicitud)
            .observe(this, Observer {
                almacenAdapter.addItems(it)
            })
        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                almacenAdapter.getFilter().filter(editTextSearch.text.toString())
            }
        })
    }

    private fun spinnerStockMaterialDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_combo, null)
        val progressBar: ProgressBar = v.findViewById(R.id.progressBar)
        val textViewTitulo: TextView = v.findViewById(R.id.textViewTitulo)
        val editTextSearch: TextInputEditText = v.findViewById(R.id.editTextSearch)
        val recyclerView: RecyclerView = v.findViewById(R.id.recyclerView)
        val fabClose: FloatingActionButton = v.findViewById(R.id.fabClose)
        val layoutManager = LinearLayoutManager(v.context)
        textViewTitulo.text = String.format("%s", "Material")
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(
            DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        )
        recyclerView.layoutManager = layoutManager
        builder.setView(v)
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()

        var loading = false
        var pageNumber = 1
        var lastVisibleItem: Int
        var totalItemCount: Int
        var visibleItemCount: Int

        val materialPaginationAdapter =
            MaterialPaginationAdapter(tipoMaterialSolicitud, tipoSolicitudId,
                object : OnItemClickListener.MaterialListener {
                    override fun onItemClick(ma: Material, view: View, position: Int) {
                        editTextMaterial.setText(ma.materialId)
                        m.abreviatura = ma.abreviatura
                        m.guiaSalida = ma.fecha
                        m.guiaIngreso = ma.guiaIngreso
                        m.guiaIngresoId = ma.guiaIngresoId
                        stock = ma.stock
                        cardView.visibility = View.VISIBLE
                        textViewDescripcion.text = ma.descripcion
                        textViewUnidadMedida.text =
                            String.format("Unidad de Medida : %s", ma.abreviatura)
                        textViewStock.text = String.format("Stock : %s", ma.stock)
                        when (tipoSolicitudId) {
                            1 -> if (tipoMaterialSolicitud == 2) {
                                textViewStock.visibility = View.GONE
                            }
                            2 -> {
                                textViewGuiaSalida.visibility = View.VISIBLE
                                textViewGuiaSalida.text =
                                    String.format("Guia de Salida : %s", ma.fecha)
                            }
                        }
                        fabAprobation.visibility = View.GONE
                        fabAnnulment.visibility = View.GONE
                        q.search = ""
                        Util.showKeyboard(editTextCantidad, context!!)
                        materialViewModel.clearMateriales()
                        dialog.dismiss()
                    }
                })

        recyclerView.adapter = materialPaginationAdapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!loading) {
                    if (dy > 0) {
                        visibleItemCount = layoutManager.childCount
                        totalItemCount = layoutManager.itemCount
                        lastVisibleItem = layoutManager.findFirstVisibleItemPosition()

                        if ((visibleItemCount + lastVisibleItem) >= totalItemCount) {
                            materialViewModel.setLoading(true)
                            materialViewModel.setPageNumber(pageNumber++)
                            materialViewModel.next(pageNumber)
                        }
                    }
                }
            }
        })
        materialViewModel.setPageNumber(pageNumber)
        materialViewModel.paginationStockMateriales(q)
        materialViewModel.getMateriales().observe(this, Observer { b ->
            if (b != null) {
                materialPaginationAdapter.addItems(b)
                fabClose.visibility = View.VISIBLE
            } else {
                materialPaginationAdapter.addItems(listOfNotNull())
            }
        })

        editTextSearch.setOnEditorActionListener { t, _, _ ->
            if (editTextSearch.text.toString().trim().isNotEmpty()) {
                materialViewModel.clearMateriales()
                materialViewModel.clear()
                materialPaginationAdapter.addItems(emptyList())
                q.search = editTextSearch.text.toString()
                materialViewModel.paginationStockMateriales(q)
            } else {
                Util.toastMensaje(context!!, "Ingresar una busqueda")
            }
            false
        }

        materialViewModel.load.observe(this, Observer { b ->
            loading = b
            if (b) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })

        materialViewModel.error.observe(this, Observer { s ->
            if (s != null) {
                Util.toastMensaje(context!!, s)
                progressBar.visibility = View.GONE
                fabClose.visibility = View.VISIBLE
            }
        })

        fabClose.setOnClickListener {
            q.search = ""
            materialViewModel.clearMateriales()
            dialog.dismiss()
        }
    }

    private fun formRegistroMaterial() {
        m.tipoSolicitudId = tipoSolicitudId
        m.tipoMaterial = tipoMaterialSolicitud
        when {
            editTextCantidad.text.toString().isEmpty() -> {
                m.cantidadMovil = 0.0
            }
            else -> {
                m.cantidadMovil = editTextCantidad.text.toString().toDouble()
            }
        }
        m.cantidadOk = stock
        m.stock = m.cantidadMovil - stock
        m.fecha = Util.getFecha()
        m.tipo = 0
        m.estado = "105"
        m.descripcion = textViewDescripcion.text.toString()
        m.unidadMedida = textViewUnidadMedida.text.toString()
        m.codigoMaterial = editTextMaterial.text.toString()
        if (solicitudViewModel.validateRegistroSolicitudMaterial(m)) {
            load("Enviando")
        }
    }

    private fun getAprobationSolicitud() {
        val dialog = MaterialAlertDialogBuilder(context!!)
            .setTitle("Mensaje")
            .setMessage(String.format("%s", "Deseas aprobar la solicitud ?."))
            .setPositiveButton("Aceptar") { dialog, _ ->
                q.solicitudId = m.identity
                q.estado = "106"
                solicitudViewModel.sendAprobation(q, solicitudId)
                load("Aprobando...")
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
    }

    private fun getAnnulmentSolicitud() {
        val dialog = MaterialAlertDialogBuilder(context!!)
            .setTitle("Mensaje")
            .setMessage(String.format("%s", "Deseas anular la solicitud ?."))
            .setPositiveButton("Aceptar") { dialog, _ ->
                q.solicitudId = m.identity
                q.estado = "110"
                solicitudViewModel.sendAprobation(q, solicitudId)
                load("Anulando...")
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
    }

    private fun load(title: String) {
        builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(context).inflate(R.layout.dialog_login, null)
        builder.setView(view)
        val textViewTitle: TextView = view.findViewById(R.id.textView)
        textViewTitle.text = title
        dialog = builder.create()
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    private fun menssage() {
        solicitudViewModel.error.observe(viewLifecycleOwner, Observer { m ->
            if (dialog != null) {
                if (dialog!!.isShowing) {
                    dialog!!.dismiss()
                }
            }
            Util.hideKeyboardFrom(context!!, view!!)
            Util.snackBarMensaje(view!!, m)
        })
    }

    private fun success() {
        solicitudViewModel.success.observe(viewLifecycleOwner, Observer { m ->
            if (dialog != null) {
                if (dialog!!.isShowing) {
                    dialog!!.dismiss()
                }
            }
            cardView.visibility = View.GONE
            editTextCantidad.text = null
            Util.hideKeyboardFrom(context!!, view!!)
            Util.toastMensaje(context!!, m)

            if (m == "Solicitud Aprobada") {
                activity!!.finish()
            }
        })
    }

    private fun editCantidad(m: RegistroSolicitudMaterial) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_cantidad, null)
        val textviewTitle: TextView = v.findViewById(R.id.textviewTitle)
        val editTextCount: EditText = v.findViewById(R.id.editTextCount)
        val buttonCancelar: MaterialButton = v.findViewById(R.id.buttonCancelar)
        val buttonAceptar: MaterialButton = v.findViewById(R.id.buttonAceptar)
        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        textviewTitle.text = String.format("Editar %s :", m.codigoMaterial)
        editTextCount.setText(m.cantidadMovil.toString())
        Util.showKeyboard(editTextCount, context!!)
        buttonAceptar.setOnClickListener {
            when {
                editTextCount.text.toString().isEmpty() -> {
                    m.cantidadMovil = 0.0
                    m.cantidadOk = 0.0
                }
                else -> {
                    m.cantidadMovil = editTextCount.text.toString().toDouble()
                    m.cantidadOk = editTextCount.text.toString().toDouble()
                }
            }
            solicitudViewModel.validateEditSolicitudMaterial(m)
            dialog.dismiss()
            load("Editando...")
        }

        buttonCancelar.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun deleteRegistroMaterial(p: RegistroSolicitudMaterial) {
        val dialog = MaterialAlertDialogBuilder(context!!)
            .setTitle("Mensaje")
            .setMessage(String.format("Deseas eliminar %s ?.", p.codigoMaterial))
            .setPositiveButton("Aceptar") { dialog, _ ->
                load("Eliminando...")
                solicitudViewModel.validateDeleteSolicitudMaterial(p)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
    }
}