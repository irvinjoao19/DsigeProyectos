package com.dsige.dsigeproyectos.ui.fragments.engie

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
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
import com.dsige.dsigeproyectos.ui.adapters.ActividadAdapter
import com.dsige.dsigeproyectos.ui.adapters.BaremoAdapter
import com.dsige.dsigeproyectos.ui.adapters.BaremoRegistroAdapter
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_parte_diario_baremos.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"

class ParteDiarioBaremosFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.editTextActividad -> if (p.obraTd.isNotEmpty()) {
                spinnerDialog(1, "Actividad")
            } else {
                parteDiarioViewModel.setError("Completar primer formulario")
            }
            R.id.editTextBaremo -> if (r.actividadId != 0) {
                spinnerDialog(2, "Baremos")
            } else {
                parteDiarioViewModel.setError("Elige un Baremo")
            }
            R.id.fabRegisterBaremo -> formRegistroBaremo()
        }
    }

    private var parteDiarioId: Int = 0

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var parteDiarioViewModel: ParteDiarioViewModel

    lateinit var r: RegistroBaremo
    lateinit var p: ParteDiario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        r = RegistroBaremo()
        p = ParteDiario()

        arguments?.let {
            parteDiarioId = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_parte_diario_baremos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parteDiarioViewModel =
            ViewModelProvider(this, viewModelFactory).get(ParteDiarioViewModel::class.java)
        editTextActividad.setOnClickListener(this)
        editTextBaremo.setOnClickListener(this)
        fabRegisterBaremo.setOnClickListener(this)
        bindUI()
        menssage()
        success()
    }

    private fun bindUI() {
        val layoutManager = LinearLayoutManager(context)
        val baremoRegistroAdapter =
            BaremoRegistroAdapter(object : OnItemClickListener.RegistroBaremoListener {
                override fun onItemClick(b: RegistroBaremo, view: View, position: Int) {
                    when (view.id) {
                        R.id.buttonEdit -> editCantidad(b)
                        R.id.buttonDelete -> deleteBaremo(b)
                    }
                }
            })
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = baremoRegistroAdapter

        parteDiarioViewModel.getRegistroBaremos(parteDiarioId)
            .observe(viewLifecycleOwner, Observer { r ->
                if (r != null) {
                    baremoRegistroAdapter.addItems(r)
                }
            })

        parteDiarioViewModel.getParteDiarioById(parteDiarioId)
            .observe(viewLifecycleOwner, Observer { d ->
                if (d != null) {
                    p.obraTd = d.obraTd
                    if (d.estadoCodigo == "121") {
                        editTextActividad.visibility = View.GONE
                        editTextBaremo.visibility = View.GONE
                    }
                }
            })
    }

    companion object {
        @JvmStatic
        fun newInstance(parteDiarioId: Int) =
            ParteDiarioBaremosFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, parteDiarioId)
                }
            }
    }

    private fun spinnerDialog(tipo: Int, title: String) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_combo, null)

        val progressBar: ProgressBar = v.findViewById(R.id.progressBar)
        val textViewTitulo: TextView = v.findViewById(R.id.textViewTitulo)
        val editTextSearch: TextInputEditText = v.findViewById(R.id.editTextSearch)
        val recyclerView: RecyclerView = v.findViewById(R.id.recyclerView)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        textViewTitulo.text = title
        progressBar.visibility = View.GONE

        builder.setView(v)
        val dialog = builder.create()
        dialog.show()


        when (tipo) {
            1 -> {
                val actividadAdapter =
                    ActividadAdapter(object : OnItemClickListener.ActividadListener {
                        override fun onItemClick(a: Actividad, v: View, position: Int) {
                            editTextActividad.setText(a.descripcion)
                            r.actividadId = a.actividadId
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
                recyclerView.adapter = actividadAdapter

                parteDiarioViewModel.getActividad().observe(this, Observer<List<Actividad>> { b ->
                    if (b != null) {
                        actividadAdapter.addItems(b)
                    }
                })

                editTextSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {

                    }

                    override fun onTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {

                    }

                    override fun afterTextChanged(editable: Editable) {
                        actividadAdapter.getFilter().filter(editTextSearch.text.toString())
                    }
                })
            }

            2 -> {
                val baremoAdapter = BaremoAdapter(object : OnItemClickListener.BaremoListener {
                    override fun onItemClick(b: Baremo, view: View, position: Int) {
                        r.codigoBaremo = b.baremoId
                        r.abreviatura = b.abreviatura
                        cardView.visibility = View.VISIBLE
//
                        editTextBaremo.setText(b.baremoId)
                        textViewDescripcion.text = b.descripcion
                        textViewUnidadMedida.text =
                            String.format("Unidad de Medida : %s", b.abreviatura)
//
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
                recyclerView.adapter = baremoAdapter

                parteDiarioViewModel.getBaremosById(r.actividadId)
                    .observe(this, Observer<List<Baremo>> { b ->
                        if (b != null) {
                            baremoAdapter.addItems(b)
                        }
                    })

                editTextSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {

                    }

                    override fun onTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {

                    }

                    override fun afterTextChanged(editable: Editable) {
                        baremoAdapter.getFilter().filter(editTextSearch.text.toString())
                    }
                })
            }
        }
    }

    private fun editCantidad(b: RegistroBaremo) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_cantidad, null)

        val textviewTitle: TextView = v.findViewById(R.id.textviewTitle)
        val editTextCount: EditText = v.findViewById(R.id.editTextCount)
        val buttonCancelar: Button = v.findViewById(R.id.buttonCancelar)
        val buttonAceptar: Button = v.findViewById(R.id.buttonAceptar)

        textviewTitle.text = String.format("Editar : %s", b.codigoBaremo)
        editTextCount.setText(b.cantidadMovil.toString())
        Util.showKeyboard(editTextCount, context!!)

        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        buttonAceptar.setOnClickListener {
            when {
                editTextCount.text.toString().isEmpty() -> {
                    b.cantidadMovil = 0.0
                    b.cantidadOk = 0.0
                }
                else -> {
                    b.cantidadMovil = editTextCount.text.toString().toDouble()
                    b.cantidadOk = editTextCount.text.toString().toDouble()
                }
            }
            if (parteDiarioViewModel.validateUpdateRegistroBaremo(b)) {
                Util.hideKeyboardFrom(context!!, v)
                dialog.dismiss()
            }
        }

        buttonCancelar.setOnClickListener {
            Util.hideKeyboardFrom(context!!, v)
            dialog.dismiss()
        }
    }

    private fun deleteBaremo(b: RegistroBaremo) {
        val dialog = MaterialAlertDialogBuilder(context!!)
            .setTitle("Mensaje")
            .setMessage(String.format("Deseas eliminar %s ?.", b.codigoBaremo))
            .setPositiveButton("Aceptar") { dialog, _ ->
                parteDiarioViewModel.deleteRegistroBaremo(b)
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
        parteDiarioViewModel.success.observe(viewLifecycleOwner, Observer<String> { m ->
            if (m == "Guardado") {
                editTextCantidad.text = null
                cardView.visibility = View.GONE
                editTextBaremo.text = null
                Util.hideKeyboardFrom(context!!, view!!)
            }
            Util.toastMensaje(context!!, m)
        })
    }

    private fun formRegistroBaremo() {
        r.parteDiarioId = parteDiarioId
        when {
            editTextCantidad.text.toString().isEmpty() -> {
                r.cantidadMovil = 0.0
                r.cantidadOk = 0.0
            }
            else -> {
                r.cantidadMovil = editTextCantidad.text.toString().toDouble()
                r.cantidadOk = editTextCantidad.text.toString().toDouble()
            }
        }

        r.fecha = Util.getFechaActual()
        r.descripcion = textViewDescripcion.text.toString()
        r.unidadMedida = textViewUnidadMedida.text.toString()
        r.tipo = 0

        parteDiarioViewModel.validateRegistroBaremo(r)
    }
}