package com.dsige.dsigeproyectos.ui.fragments.logistica

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
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
import com.dsige.dsigeproyectos.data.local.model.logistica.Requerimiento
import com.dsige.dsigeproyectos.data.local.model.logistica.RequerimientoCentroCosto
import com.dsige.dsigeproyectos.data.local.model.logistica.RequerimientoTipo
import com.dsige.dsigeproyectos.data.viewModel.LogisticaViewModel
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.adapters.*
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_general.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GeneralFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.editTextFecha -> Util.getDateDialog(context!!, editTextFecha)
            R.id.editTextDelegacion -> spinnerDialog("Delegacion", 1)
            R.id.editTextCentro -> spinnerDialog("Centro de Costo", 2)
            R.id.editTextSolicitud -> spinnerDialog("Tipo de Solicitud", 3)
            R.id.fabGenerate -> formValidateRequerimiento()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var logisticaViewModel: LogisticaViewModel

    private var requerimientoId: Int = 0
    private var usuarioId: String = ""
    lateinit var r: Requerimiento
    private var viewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        r = Requerimiento()
        arguments?.let {
            requerimientoId = it.getInt(ARG_PARAM1)
            usuarioId = it.getString(ARG_PARAM2)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_general, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        viewPager = activity!!.findViewById(R.id.viewPager)
        logisticaViewModel =
            ViewModelProvider(this, viewModelFactory).get(LogisticaViewModel::class.java)

        editTextNro.setText(requerimientoId.toString())

        logisticaViewModel.getRequerimientoById(requerimientoId).observe(viewLifecycleOwner, {
            if (it != null) {
                r = it
                editTextNro.setText(it.nroSolicitud)
                editTextSolicitud.setText(it.nombreTipoSolicitud)
                editTextFecha.setText(it.fecha)
                editTextDelegacion.setText(it.nombreDelegacion)
                editTextCentro.setText(it.nombreCentroCosto)
                editTextObs.setText(it.observaciones)
            }
        })
        editTextFecha.setOnClickListener(this)
        editTextDelegacion.setOnClickListener(this)
        editTextCentro.setOnClickListener(this)
        editTextSolicitud.setOnClickListener(this)
        fabGenerate.setOnClickListener(this)

        logisticaViewModel.mensajeError.observe(viewLifecycleOwner, {
            Util.toastMensaje(context!!, it)
        })
        logisticaViewModel.mensajeSuccess.observe(viewLifecycleOwner, {
            viewPager?.currentItem = 1
            Util.toastMensaje(context!!, it)
        })
    }

    private fun formValidateRequerimiento() {
        r.requerimientoId = requerimientoId
        r.nroSolicitud = editTextNro.text.toString()
        r.nombreTipoSolicitud = editTextSolicitud.text.toString()
        r.fecha = editTextFecha.text.toString()
        r.nombreDelegacion = editTextDelegacion.text.toString()
        r.nombreCentroCosto = editTextCentro.text.toString()
        r.observaciones = editTextObs.text.toString()
        r.estado = "Generado"
        r.usuario = usuarioId
        r.estadoId = 1
        logisticaViewModel.validateRequerimiento(r)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
            GeneralFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun spinnerDialog(title: String, tipo: Int) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_combo, null)

        val progressBar: ProgressBar = v.findViewById(R.id.progressBar)
        val textViewTitulo: TextView = v.findViewById(R.id.textViewTitulo)
        val editTextSearch: TextInputEditText = v.findViewById(R.id.editTextSearch)
        val layoutSearch: TextInputLayout = v.findViewById(R.id.layoutSearch)
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
        textViewTitulo.text = title
        when (tipo) {
            1 -> {
                val delegacionAdapter =
                    DelegacionAdapter(object : OnItemClickListener.DelegacionListener {
                        override fun onItemClick(d: Delegacion, v: View, position: Int) {
                            r.codigoDelegacion = d.codigo
                            editTextDelegacion.setText(d.descripcion)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = delegacionAdapter
                logisticaViewModel.getDelegacion().observe(viewLifecycleOwner, {
                    delegacionAdapter.addItems(it)
                })
                editTextSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun afterTextChanged(editable: Editable) {
                        delegacionAdapter.getFilter().filter(editTextSearch.text.toString())
                    }
                })
            }
            2 -> {
                val centroAdapter =
                    RequerimientoCentroAdapter(object :
                        OnItemClickListener.RequerimientoCentroCostoListener {
                        override fun onItemClick(
                            c: RequerimientoCentroCosto, v: View, position: Int
                        ) {
                            r.codigoCentroCosto = c.codigo
                            editTextCentro.setText(c.nombre)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = centroAdapter
                logisticaViewModel.getCentroCostos().observe(this, {
                    centroAdapter.addItems(it)
                })

                editTextSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun afterTextChanged(editable: Editable) {
                        centroAdapter.getFilter().filter(editTextSearch.text.toString())
                    }
                })
            }
            3 -> {
                layoutSearch.visibility = View.GONE
                val tipoAdapter = RequerimientoTipoAdapter(object :
                    OnItemClickListener.RequerimientoTipoListener {
                    override fun onItemClick(c: RequerimientoTipo, v: View, position: Int) {
                        r.tipoSolicitud = c.codigo
                        editTextSolicitud.setText(c.nombre)
                        dialog.dismiss()
                    }
                })
                recyclerView.adapter = tipoAdapter
                logisticaViewModel.getTipos().observe(this, {
                    tipoAdapter.addItems(it)
                })
            }
        }
    }
}