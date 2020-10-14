package com.dsige.dsigeproyectos.ui.fragments.engie

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager

import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.*
import com.dsige.dsigeproyectos.data.viewModel.SolicitudViewModel
import com.dsige.dsigeproyectos.data.viewModel.UsuarioViewModel
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.adapters.CoordinadorAdapter
import com.dsige.dsigeproyectos.ui.adapters.ObraAdapter
import com.dsige.dsigeproyectos.ui.adapters.PersonalAdapter
import com.dsige.dsigeproyectos.ui.adapters.TipoDevolucionAdapter
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import com.google.android.material.textfield.TextInputEditText
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_solicitud_general.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

class SolicitudGeneralFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        if (s.pubEstadoCodigo == "105") {
            when (v.id) {
                R.id.editTextFecha -> Util.getDateDialog(context!!, editTextFecha)
                R.id.editTextObra -> spinnerDialog(1)
                R.id.editTextPersonal -> spinnerDialog(2)
                R.id.editTextTipoDevolucion -> spinnerDialog(3)
                R.id.editTextCoordinador -> spinnerDialog(4)
                R.id.fabGenerate -> formGeneral()
            }
        } else {
            solicitudViewModel.setError("Solicitud en progreso...")
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var solicitudViewModel: SolicitudViewModel
    lateinit var usuarioViewModel: UsuarioViewModel
    lateinit var builder: AlertDialog.Builder
    var dialog: AlertDialog? = null
    var viewPager: ViewPager? = null

    var solicitudId: Int = 0
    var tipoMaterialSolicitud: Int = 0
    var tipoSolicitudId: Int = 0
    lateinit var s: Solicitud

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        s = Solicitud()
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
        return inflater.inflate(R.layout.fragment_solicitud_general, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        solicitudViewModel =
            ViewModelProvider(this, viewModelFactory).get(SolicitudViewModel::class.java)
        usuarioViewModel =
            ViewModelProvider(this, viewModelFactory).get(UsuarioViewModel::class.java)
        editTextFecha.setOnClickListener(this)
        editTextPersonal.setOnClickListener(this)
        editTextObra.setOnClickListener(this)
        editTextTipoDevolucion.setOnClickListener(this)
        editTextCoordinador.setOnClickListener(this)
        fabGenerate.setOnClickListener(this)
        bindUI()
        menssage()
        success()
    }

    private fun bindUI() {
        viewPager = activity!!.findViewById(R.id.viewPager)
        editTextNumero.setText(solicitudId.toString())
        editTextFecha.setText(Util.getFecha())
        usuarioViewModel.user.observe(viewLifecycleOwner, { u ->
            s.usuario = u.usuarioId
            s.centroCosto = u.centroId
            s.cuadrillaCodigo = u.cuadrillaId
            s.dniCuadrilla = u.dniCuadrillaId
            s.sucursalId = u.sucursalId
        })

        solicitudViewModel.getSolicitudById(solicitudId).observe(viewLifecycleOwner, { g ->
            if (g != null) {
                s.solicitudId = g.solicitudId
                s.identity = g.identity
                s.tipo = g.tipo
                s.dniCuadrilla = g.dniCuadrilla
                s.tipoMaterial = g.tipoMaterial
                s.pubEstadoCodigo = g.pubEstadoCodigo
                editTextTipoDevolucion.setText(g.nombreTipoMaterial)
                editTextObra.setText(g.obraTd)
                textViewDescripcion.text = g.descripcionObra
                editTextPersonal.setText(g.dniPersonal)
                textViewDescripcionPersonal.text = g.nombrePersonal
                editTextCoordinador.setText(g.dniCoordinador)
                textViewDescripcionCoordinador.text = g.nombreCoordinador
                editTextFecha.setText(g.fechaAtencion)
                editTextObservacion.setText(g.observacion)
                if (g.pubEstadoCodigo != "105") {
                    fabGenerate.visibility = View.GONE
                }
            } else {
                s.pubEstadoCodigo = "105"
            }
        })
        if (tipoMaterialSolicitud == 1) {
            textInputPersonal.visibility = View.GONE
            textViewDescripcionPersonal.visibility = View.GONE
        }

        if (tipoSolicitudId == 1) {
            textInputTipoDevolucion.visibility = View.GONE
        } else {
            fabGenerate.backgroundTintList = resources.getColorStateList(R.color.colorPrimary, null)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(solicitudId: Int, tipoMaterialSolicitud: Int, tipoSolicitudId: Int) =
            SolicitudGeneralFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, solicitudId)
                    putInt(ARG_PARAM2, tipoMaterialSolicitud)
                    putInt(ARG_PARAM3, tipoSolicitudId)
                }
            }
    }

    private fun spinnerDialog(tipo: Int) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_combo, null)

        val progressBar: ProgressBar = v.findViewById(R.id.progressBar)
        val textViewTitulo: TextView = v.findViewById(R.id.textViewTitulo)
        val editTextSearch: TextInputEditText = v.findViewById(R.id.editTextSearch)
        val recyclerView: RecyclerView = v.findViewById(R.id.recyclerView)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        progressBar.visibility = View.GONE
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )

        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        when (tipo) {
            1 -> {
                textViewTitulo.text = String.format("%s", "Obras | Td")
                val obraAdapter = ObraAdapter(object : OnItemClickListener.ObraListener {
                    override fun onItemClick(o: Obra, view: View, position: Int) {
                        s.obraTd = o.obraId
                        s.descripcionObra = o.descripcion
                        textViewDescripcion.visibility = View.VISIBLE
                        textViewDescripcion.text = o.descripcion
                        editTextObra.setText(o.obraId)
                        dialog.dismiss()
                    }
                })
                recyclerView.adapter = obraAdapter
                solicitudViewModel.getObras().observe(this, { o ->
                    if (o != null) {
                        obraAdapter.addItems(o)
                    }
                })

                editTextSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun afterTextChanged(editable: Editable) {
                        obraAdapter.getFilter().filter(editTextSearch.text.toString())
                    }
                })
            }
            2 -> {
                textViewTitulo.text = String.format("%s", "Personal")
                val personalAdapter =
                    PersonalAdapter(object : OnItemClickListener.PersonalListener {
                        override fun onItemClick(p: Personal, v: View, position: Int) {
                            s.dniPersonal = p.nroDocumento
                            editTextPersonal.setText(p.nroDocumento)
                            textViewDescripcionPersonal.text =
                                String.format("%s %s", p.nombre, p.apellido)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = personalAdapter
                solicitudViewModel.getPersonal().observe(this, { p ->
                    if (p != null) {
                        personalAdapter.addItems(p)
                    }
                })

                editTextSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun afterTextChanged(editable: Editable) {
                        personalAdapter.getFilter().filter(editTextSearch.text.toString())
                    }
                })
            }
            3 -> {
                textViewTitulo.text = String.format("%s", "Tipo de Devolucion")
                val tipoDevolucionAdapter =
                    TipoDevolucionAdapter(object : OnItemClickListener.TipoDevolucionListener {
                        override fun onItemClick(t: TipoDevolucion, v: View, position: Int) {
                            s.tipoMaterial = t.tipo
                            editTextTipoDevolucion.setText(t.descripcion)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = tipoDevolucionAdapter
                solicitudViewModel.getTipoDevolucion()
                    .observe(this, { t ->
                        if (t != null) {
                            tipoDevolucionAdapter.addItems(t)
                        }
                    })

                editTextSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun afterTextChanged(editable: Editable) {
                        tipoDevolucionAdapter.getFilter().filter(editTextSearch.text.toString())
                    }
                })
            }
            4 -> {
                textViewTitulo.text = String.format("%s", "Coordinador")
                val coordinadorAdapter =
                    CoordinadorAdapter(object : OnItemClickListener.CoordinadorListener {
                        override fun onItemClick(c: Coordinador, v: View, position: Int) {
                            s.dniCoordinador = c.codigo
                            editTextCoordinador.setText(c.codigo)
                            textViewDescripcionCoordinador.text = String.format("%s", c.nombre)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = coordinadorAdapter
                solicitudViewModel.getCoordinador().observe(this, {
                    coordinadorAdapter.addItems(it)
                })
                editTextSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun afterTextChanged(editable: Editable) {
                        coordinadorAdapter.getFilter().filter(editTextSearch.text.toString())
                    }
                })
            }
        }
    }

    private fun formGeneral() {
        s.solicitudId = solicitudId
        s.tipoSolicitudId = tipoSolicitudId
        s.tipoMaterialSol = tipoMaterialSolicitud
        s.fechaAtencion = editTextFecha.text.toString()
        s.observacion = editTextObservacion.text.toString()
        s.pubEstadoCodigo = "105"
        s.nombreTipoMaterial = editTextTipoDevolucion.text.toString()
        s.obraTd = editTextObra.text.toString()
        s.dniPersonal = editTextPersonal.text.toString()
        s.dniCoordinador = editTextCoordinador.text.toString()
        s.descripcionObra = textViewDescripcion.text.toString()
        s.nombrePersonal = textViewDescripcionPersonal.text.toString()
        s.nombreCoordinador = textViewDescripcionCoordinador.text.toString()
        if (solicitudViewModel.validateSolicitud(s)) {
            load("Enviando")
        }
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
        solicitudViewModel.error.observe(viewLifecycleOwner, { m ->
            if (dialog != null) {
                if (dialog!!.isShowing) {
                    dialog!!.dismiss()
                }
            }
            Util.hideKeyboardFrom(context!!, view!!)
            Util.toastMensaje(context!!, m)
        })
    }

    private fun success() {
        solicitudViewModel.success.observe(viewLifecycleOwner, { m ->
            if (dialog != null) {
                if (dialog!!.isShowing) {
                    dialog!!.dismiss()
                }
            }
            viewPager?.currentItem = 1
            Util.hideKeyboardFrom(context!!, view!!)
            Util.toastMensaje(context!!, m)
        })
    }
}