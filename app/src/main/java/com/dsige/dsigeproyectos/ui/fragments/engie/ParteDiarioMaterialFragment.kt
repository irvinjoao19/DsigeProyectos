package com.dsige.dsigeproyectos.ui.fragments.engie

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.*
import com.dsige.dsigeproyectos.data.viewModel.ParteDiarioViewModel
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.adapters.AlmacenAdapter
import com.dsige.dsigeproyectos.ui.adapters.MaterialAdapter
import com.dsige.dsigeproyectos.ui.adapters.MedidorAdapter
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import com.dsige.dsigeproyectos.ui.adapters.ParteDiarioMaterialRegistroAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_parte_diario_material.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"

class ParteDiarioMaterialFragment : DaggerFragment(), View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            editTextMedidor.text = null
            editTextMedidorManual.visibility = View.VISIBLE
        } else {
            editTextMedidorManual.text = null
            editTextMedidorManual.visibility = View.GONE
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.editTextAlmacen -> if (p.obraTd.isNotEmpty()) {
                spinnerDialog("Almacen", 1)
            } else {
                parteDiarioViewModel.setError("Completar primer formulario")
            }
            R.id.editTextMaterial -> if (m.almacenId.isNotEmpty()) {
                materialDialog()
            } else {
                Util.snackBarMensaje(v, "Eliga un almacen")
            }
            R.id.editTextMedidor -> spinnerDialog("Medidor", 2)
            R.id.fabRegisterMaterial -> formRegistroMaterial()
        }
    }

    var parteDiarioId: Int = 0
    lateinit var m: RegistroMaterial
    lateinit var p: ParteDiario

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var parteDiarioViewModel: ParteDiarioViewModel
    lateinit var parteDiarioMaterialRegistroAdapter: ParteDiarioMaterialRegistroAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        m = RegistroMaterial()
        p = ParteDiario()
        arguments?.let {
            parteDiarioId = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_parte_diario_material, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parteDiarioViewModel =
            ViewModelProvider(this, viewModelFactory).get(ParteDiarioViewModel::class.java)
        editTextAlmacen.setOnClickListener(this)
        editTextMaterial.setOnClickListener(this)
        editTextMedidor.setOnClickListener(this)
        checkMedidorManual.setOnCheckedChangeListener(this)
        fabRegisterMaterial.setOnClickListener(this)
        bindUI()
        menssage()
        success()
    }

    private fun bindUI() {
        val layoutManager = LinearLayoutManager(context)
        parteDiarioMaterialRegistroAdapter = ParteDiarioMaterialRegistroAdapter(object :
            OnItemClickListener.RegistroMaterialListener {
            override fun onItemClick(m: RegistroMaterial, view: View, position: Int) {
                when (view.id) {
                    R.id.buttonEdit -> editCantidad(m)
                    R.id.buttonDelete -> deleteMaterial(m)
                }
            }
        })
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = parteDiarioMaterialRegistroAdapter

        parteDiarioViewModel.getRegistroMaterialTaskByFK(parteDiarioId)
            .observe(viewLifecycleOwner, Observer {
                parteDiarioMaterialRegistroAdapter.addItems(it)
            })

        parteDiarioViewModel.getParteDiarioById(parteDiarioId)
            .observe(viewLifecycleOwner, Observer { d ->
                if (d != null) {
                    p.obraTd = d.obraTd
                    if (d.estadoCodigo == "121") {
                        editTextAlmacen.visibility = View.GONE
                        editTextMaterial.visibility = View.GONE
                    }
                }
            })
    }

    companion object {
        @JvmStatic
        fun newInstance(parteDiarioId: Int) =
            ParteDiarioMaterialFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, parteDiarioId)
                }
            }
    }

    private fun materialDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_combo, null)

        val progressBar: ProgressBar = v.findViewById(R.id.progressBar)
        val textViewTitulo: TextView = v.findViewById(R.id.textViewTitulo)
        val editTextSearch: TextInputEditText = v.findViewById(R.id.editTextSearch)
        val recyclerView: RecyclerView = v.findViewById(R.id.recyclerView)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        textViewTitulo.text = String.format("%s", "Materiales")
        progressBar.visibility = View.GONE
        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        val materialAdapter = MaterialAdapter(object : OnItemClickListener.MaterialListener {
            override fun onItemClick(ma: Material, view: View, position: Int) {
                m.codigoMaterial = ma.materialId
                m.abreviatura = ma.abreviatura
                m.guiaSalida = ma.guiaSalida
                m.materialId = ma.id
                m.stock = ma.stock
                m.exigeSerie = ma.exigeSerie
                m.guiaIngreso = ma.guiaIngreso
                m.guiaIngresoId = ma.guiaIngresoId
                cardView.visibility = View.VISIBLE

                textViewMedidor.visibility = View.GONE
                checkMedidorManual.visibility = View.GONE
                editTextCantidad.isEnabled = true
                editTextCantidad.text = null
                if (ma.exigeSerie == "SI") {
                    editTextCantidad.setText("1")
                    editTextCantidad.isEnabled = false
                    textViewMedidor.visibility = View.VISIBLE
                    checkMedidorManual.visibility = View.VISIBLE
                }

                editTextMaterial.setText(ma.materialId)
                textViewDescripcion.text = ma.descripcion
                textViewGuiaSalida.text = ma.guiaSalida
                textViewUnidadMedida.text = String.format("Unidad de Medida : %s", ma.abreviatura)
                textViewStock.text = String.format("Stock : %s", ma.stock)
                Util.showKeyboard(editTextCantidad, context!!)
                dialog.dismiss()
            }
        })

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
        recyclerView.adapter = materialAdapter

        parteDiarioViewModel.getMateriales(p.obraTd, m.almacenId)
            .observe(this, Observer<List<Material>> { m ->
                if (m != null) {
                    materialAdapter.addItems(m)
                }
            })

        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                materialAdapter.getFilter().filter(editTextSearch.text.toString())
            }
        })
    }


    private fun spinnerDialog(title: String, tipo: Int) {
        val builder = AlertDialog.Builder(
            ContextThemeWrapper(context, R.style.AppTheme)
        )
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_combo, null)
        val progressBar: ProgressBar = v.findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE
        val textViewTitulo: TextView = v.findViewById(R.id.textViewTitulo)
        val editTextSearch: TextInputEditText = v.findViewById(R.id.editTextSearch)
        val recyclerView: RecyclerView = v.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(v.context)
        textViewTitulo.text = String.format("%s", title)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
        recyclerView.layoutManager = layoutManager

        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        when (tipo) {
            1 -> {
                val almacenAdapter = AlmacenAdapter(object : OnItemClickListener.AlmacenListener {
                    override fun onItemClick(a: Almacen, view: View, position: Int) {
                        m.almacenId = a.codigo
                        editTextAlmacen.setText(a.descripcion)
                        clear()
                        dialog.dismiss()
                    }
                })
                recyclerView.adapter = almacenAdapter
                parteDiarioViewModel.getAlmacenEdelnor()
                    .observe(this, Observer {
                        almacenAdapter.addItems(it)
                    })
                editTextSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        charSequence: CharSequence, i: Int, i1: Int, i2: Int
                    ) {

                    }

                    override fun onTextChanged(
                        charSequence: CharSequence, i: Int, i1: Int, i2: Int
                    ) {

                    }

                    override fun afterTextChanged(editable: Editable) {
                        almacenAdapter.getFilter().filter(editTextSearch.text.toString())
                    }
                })
            }
            2 -> {
                val medidorAdapter = MedidorAdapter(object : OnItemClickListener.MedidorListener {
                    override fun onItemClick(me: Medidor, view: View, position: Int) {
                        editTextMedidor.setText(me.medidorId)
                        dialog.dismiss()
                    }
                })
                recyclerView.adapter = medidorAdapter
                parteDiarioViewModel.getMedidor()
                    .observe(this, Observer { a ->
                        if (a != null) {
                            medidorAdapter.addItems(a)
                        }
                    })
                editTextSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        charSequence: CharSequence, i: Int, i1: Int, i2: Int
                    ) {

                    }

                    override fun onTextChanged(
                        charSequence: CharSequence, i: Int, i1: Int, i2: Int
                    ) {

                    }

                    override fun afterTextChanged(editable: Editable) {
                        medidorAdapter.getFilter().filter(editTextSearch.text.toString())
                    }
                })
            }
        }
    }

    private fun formRegistroMaterial() {
        when {
            editTextCantidad.text.toString().isEmpty() -> {
                m.cantidadMovil = 0.0
                m.cantidadOk = 0.0
            }
            else -> {
                m.cantidadMovil = editTextCantidad.text.toString().toDouble()
                m.cantidadOk = editTextCantidad.text.toString().toDouble()
            }
        }
        m.parteDiarioId = parteDiarioId
        m.fecha = Util.getFechaActual()
        m.descripcion = textViewDescripcion.text.toString()
        m.unidadMedida = textViewUnidadMedida.text.toString()

        m.nroSerie = editTextMedidor.text.toString()
        if (editTextMedidor.text.toString().isEmpty()) {
            m.nroSerie = editTextMedidorManual.text.toString()
        }

        m.tipo = 0
        parteDiarioViewModel.validateRegistroMaterial(m, p)
    }

    private fun editCantidad(m: RegistroMaterial) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_cantidad, null)

        val textviewTitle: TextView = v.findViewById(R.id.textviewTitle)
        val editTextCount: EditText = v.findViewById(R.id.editTextCount)
        val buttonCancelar: Button = v.findViewById(R.id.buttonCancelar)
        val buttonAceptar: Button = v.findViewById(R.id.buttonAceptar)

        textviewTitle.text = String.format("Editar : %s", m.codigoMaterial)
        textViewStock.text = m.cantidadOk.toString()
        if (m.tipoMaterial == 2) {
            textViewStock.visibility = View.GONE
        }
        editTextCount.setText(m.cantidadMovil.toString())
        Util.showKeyboard(editTextCount, context!!)

        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        buttonAceptar.setOnClickListener {
            m.tipo = 1
            when {
                editTextCount.text.toString().isEmpty() -> {
                    m.cantidadMovil = 0.0
                }
                else -> {
                    m.cantidadMovil = editTextCount.text.toString().toDouble()
                }
            }
            if (parteDiarioViewModel.validateUpdateRegistroMaterial(m)) {
                Util.hideKeyboardFrom(context!!, v)
                dialog.dismiss()
            }
        }
        buttonCancelar.setOnClickListener {
            Util.hideKeyboardFrom(context!!, v)
            dialog.dismiss()
        }
    }

    private fun deleteMaterial(m: RegistroMaterial) {
        val dialog = MaterialAlertDialogBuilder(context!!)
            .setTitle("Mensaje")
            .setMessage(String.format("Deseas eliminar %s ?.", m.codigoMaterial))
            .setPositiveButton("Aceptar") { dialog, _ ->
                parteDiarioViewModel.deleteRegistroMaterial(m)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
    }

    private fun menssage() {
        parteDiarioViewModel.error.observe(viewLifecycleOwner, Observer { m ->
            Util.hideKeyboardFrom(context!!, view!!)
            Util.snackBarMensaje(view!!, m)
        })
    }

    private fun success() {
        parteDiarioViewModel.success.observe(viewLifecycleOwner, Observer { m ->
            clear()
            Util.hideKeyboardFrom(context!!, view!!)
            Util.toastMensaje(context!!, m)
        })
    }

    private fun clear() {
        cardView.visibility = View.GONE
        editTextCantidad.text = null
        editTextMedidorManual.text = null
        editTextMedidor.text = null
        checkMedidorManual.isChecked = false
    }
}