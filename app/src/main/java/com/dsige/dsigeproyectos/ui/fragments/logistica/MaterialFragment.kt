package com.dsige.dsigeproyectos.ui.fragments.logistica

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
import com.dsige.dsigeproyectos.data.local.model.MenuPrincipal
import com.dsige.dsigeproyectos.data.local.model.engie.CentroCostos
import com.dsige.dsigeproyectos.data.local.model.logistica.Delegacion
import com.dsige.dsigeproyectos.data.local.model.logistica.RequerimientoDetalle
import com.dsige.dsigeproyectos.data.local.model.logistica.RequerimientoMaterial
import com.dsige.dsigeproyectos.data.viewModel.LogisticaViewModel
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.adapters.*
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_general.*
import kotlinx.android.synthetic.main.fragment_material.*
import kotlinx.android.synthetic.main.fragment_material.recyclerView
import kotlinx.android.synthetic.main.fragment_solicitud_material.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MaterialFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabNuevo -> if (validate != 0) {
                dialogMaterial(0)
            } else
                logisticaViewModel.setError("Completar el Primer Formulario")
            R.id.fabAprobar -> validateAprobacion()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var logisticaViewModel: LogisticaViewModel
    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null

    private var requerimientoId: Int = 0
    private var validate: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            requerimientoId = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_material, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        logisticaViewModel =
            ViewModelProvider(this, viewModelFactory).get(LogisticaViewModel::class.java)

        val requerimientoAdapter =
            RequerimientoDetalleAdapter(object : OnItemClickListener.RequerimientoDetalleListener {
                override fun onItemClick(r: RequerimientoDetalle, v: View, position: Int) {
                    when (v.id) {
                        R.id.imgEdit -> dialogMaterial(r.detalleId)
                        R.id.imgDelete -> confirmDelete(r)
                    }
                }
            })
        recyclerView.itemAnimator = DefaultItemAnimator()
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = requerimientoAdapter

        logisticaViewModel.getRequerimientoDetalles(requerimientoId).observe(viewLifecycleOwner, {
            requerimientoAdapter.addItems(it)
        })

        logisticaViewModel.getRequerimientoById(requerimientoId).observe(viewLifecycleOwner, {
            if (it != null) {
                validate = 1
            }
        })

        fabNuevo.setOnClickListener(this)
        fabAprobar.setOnClickListener(this)

        logisticaViewModel.mensajeSuccess.observe(viewLifecycleOwner, {
            Util.toastMensaje(context!!, it)
            activity!!.finish()
        })

        logisticaViewModel.mensajeError.observe(viewLifecycleOwner, {
            Util.toastMensaje(context!!, it)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            MaterialFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }

    private fun dialogMaterial(id: Int) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_material, null)

        val editTextCodigo: TextInputEditText = v.findViewById(R.id.editTextCodigo)
        val editTextDescripcion: TextInputEditText = v.findViewById(R.id.editTextDescripcion)
        val editTextCantidad: TextInputEditText = v.findViewById(R.id.editTextCantidad)
        val editTextUM: TextInputEditText = v.findViewById(R.id.editTextUM)
        val fabGenerate: ExtendedFloatingActionButton = v.findViewById(R.id.fabGenerate)
        val btnSearch: FloatingActionButton = v.findViewById(R.id.btnSearch)
        val btnUpdate: FloatingActionButton = v.findViewById(R.id.btnUpdate)

        var r = RequerimientoDetalle()

        logisticaViewModel.getRequerimientoDetalleById(id).observe(viewLifecycleOwner, {
            if (it != null) {
                r = it
                editTextCodigo.setText(it.material)
                editTextDescripcion.setText(it.descripionMaterial)
                editTextCantidad.setText(it.cantidad.toString())
                editTextUM.setText(it.um)
            }
        })

        btnSearch.setOnClickListener {
            spinnerDialog(editTextCodigo, editTextDescripcion, editTextUM)
        }
        btnUpdate.setOnClickListener {
            editTextCodigo.text = null
            editTextDescripcion.text = null
            editTextCodigo.requestFocus()
        }

        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        fabGenerate.setOnClickListener {
            r.material = editTextCodigo.text.toString()
            r.descripionMaterial = editTextDescripcion.text.toString()
            when {
                editTextCantidad.text.toString().isEmpty() -> r.cantidad = 0.0
                else -> r.cantidad = editTextCantidad.text.toString().toDouble()
            }
            r.um = editTextUM.text.toString()
            r.requerimientoId = requerimientoId
            logisticaViewModel.validateRequerimientoDetalle(r)
            dialog.dismiss()
        }
    }

    private fun validateAprobacion() {




    }

    private fun load() {
        builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(context).inflate(R.layout.dialog_login, null)
        builder.setView(view)
        val textViewTitle: TextView = view.findViewById(R.id.textView)
        textViewTitle.text = String.format("Aprobando...")
        dialog = builder.create()
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    private fun closeLoad() {
        if (dialog != null) {
            if (dialog!!.isShowing) {
                dialog!!.dismiss()
            }
        }
    }

    private fun confirmDelete(d: RequerimientoDetalle) {
        val dialog = MaterialAlertDialogBuilder(context!!)
            .setTitle("Mensaje")
            .setMessage(String.format("Deseas eliminar el Material %s ?.", d.material))
            .setPositiveButton("SI") { dialog, _ ->
                logisticaViewModel.deleteRequerimientoDetalle(d)
                dialog.dismiss()
            }
            .setNegativeButton("NO") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
    }


    private fun spinnerDialog(input: TextInputEditText, input2: TextInputEditText,input3:TextInputEditText) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_combo, null)

        val progressBar: ProgressBar = v.findViewById(R.id.progressBar)
        val textViewTitulo: TextView = v.findViewById(R.id.textViewTitulo)
        val editTextSearch: TextInputEditText = v.findViewById(R.id.editTextSearch)
        val recyclerView: RecyclerView = v.findViewById(R.id.recyclerView)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        progressBar.visibility = View.GONE
        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context, DividerItemDecoration.VERTICAL
            )
        )
        textViewTitulo.text = String.format("Materiales")
        val requerimientoMaterialAdapter =
            RequerimientoMaterialAdapter(object :
                OnItemClickListener.RequerimientoMaterialListener {
                override fun onItemClick(r: RequerimientoMaterial, v: View, position: Int) {
                    input.setText(r.codigo)
                    input2.setText(r.descripcion)
                    input3.setText(r.abreviatura)
                    dialog.dismiss()
                }
            })
        recyclerView.adapter = requerimientoMaterialAdapter
        logisticaViewModel.getRequerimientoMateriales().observe(viewLifecycleOwner, {
            requerimientoMaterialAdapter.addItems(it)
        })
        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                requerimientoMaterialAdapter.getFilter()
                    .filter(editTextSearch.text.toString())
            }
        })

    }
}