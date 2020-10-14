package com.dsige.dsigeproyectos.ui.fragments.engie

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.Coordinador
import com.dsige.dsigeproyectos.data.local.model.engie.Obra
import com.dsige.dsigeproyectos.data.local.model.engie.ParteDiario

import com.dsige.dsigeproyectos.data.viewModel.ParteDiarioViewModel
import com.dsige.dsigeproyectos.data.viewModel.UsuarioViewModel
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.helper.Gps
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.adapters.CoordinadorAdapter
import com.dsige.dsigeproyectos.ui.adapters.ObraAdapter
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import com.google.android.material.textfield.TextInputEditText
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_parte_diario_general.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"

class ParteDiarioGeneralFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.editTextFecha -> Util.getDateDialog(context!!, editTextFecha)
            R.id.editTextObra -> spinnerDialog(1)
            R.id.editTextCoordinador -> spinnerDialog(2)
            R.id.fabRegister -> formParteDiario()
        }
    }

    var parteDiarioId: Int = 0

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var parteDiarioViewModel: ParteDiarioViewModel
    lateinit var usuarioViewModel: UsuarioViewModel
    private var viewPager: ViewPager? = null
    lateinit var p: ParteDiario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        return inflater.inflate(R.layout.fragment_parte_diario_general, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parteDiarioViewModel = ViewModelProvider(this, viewModelFactory).get(
            ParteDiarioViewModel::class.java
        )
        usuarioViewModel =
            ViewModelProvider(this, viewModelFactory).get(UsuarioViewModel::class.java)
        editTextFecha.setOnClickListener(this)
        editTextObra.setOnClickListener(this)
        editTextCoordinador.setOnClickListener(this)
        fabRegister.setOnClickListener(this)
        bindUI()
        menssage()
        success()
    }

    private fun bindUI() {
        viewPager = activity!!.findViewById(R.id.viewPager)
        editTextFecha.setText(Util.getFecha())
        editTextNumero.setText(parteDiarioId.toString())
        usuarioViewModel.user.observe(viewLifecycleOwner, Observer { u ->
            p.areaCodigo = u.areaId
            p.costoCodigo = u.centroId
            p.dniCuadrilla = u.cuadrillaId
            p.usuarioCreacion = u.usuarioId
            p.sucursalId = u.sucursalId
        })

        parteDiarioViewModel.getParteDiarioById(parteDiarioId)
            .observe(viewLifecycleOwner, Observer { d ->
                if (d != null) {
                    p.parteDiarioId = d.parteDiarioId
                    editTextFecha.setText(d.fecha)
                    editTextObra.setText(d.obraTd)
                    editTextCoordinador.setText(d.coordinadorDni)
                    editTextSuministro.setText(d.suministro)
                    editTextSed.setText(d.sed)
                    editTextObservacion.setText(d.observacion)
                    textViewDescripcion.visibility = View.VISIBLE
                    textViewDescripcion.text = d.descripcion
                    textViewDescripcionCoordinador.text = d.descripcionCoordinador

                    if (d.estadoCodigo == "121") {
                        fabRegister.visibility = View.GONE
                    }
                }
            })
    }

    companion object {
        @JvmStatic
        fun newInstance(parteDiarioId: Int) =
            ParteDiarioGeneralFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, parteDiarioId)
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

        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
        when (tipo) {
            1 -> {
                textViewTitulo.text = String.format("%s", "Obras | Td")
                val obraAdapter = ObraAdapter(object : OnItemClickListener.ObraListener {
                    override fun onItemClick(o: Obra, view: View, position: Int) {
                        textViewDescripcion.visibility = View.VISIBLE
                        textViewDescripcion.text = o.descripcion
                        editTextObra.setText(o.obraId)
                        dialog.dismiss()
                    }
                })
                recyclerView.adapter = obraAdapter

                parteDiarioViewModel.getObras().observe(viewLifecycleOwner, Observer { o ->
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
                textViewTitulo.text = String.format("%s", "Coordinador")
                val coordinadorAdapter =
                    CoordinadorAdapter(object : OnItemClickListener.CoordinadorListener {
                        override fun onItemClick(c: Coordinador, v: View, position: Int) {
                            editTextCoordinador.setText(c.codigo)
                            textViewDescripcionCoordinador.text = String.format("%s", c.nombre)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = coordinadorAdapter
                parteDiarioViewModel.getCoordinador().observe(this, Observer { c ->
                    if (c != null) {
                        coordinadorAdapter.addItems(c)
                    }
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

    private fun formParteDiario() {
        val gps = Gps(context!!)
        if (gps.isLocationEnabled()) {
            if (gps.latitude.toString() == "0.0" || gps.longitude.toString() == "0.0") {
                gps.showAlert(context!!)
            } else {
                p.parteDiarioId = parteDiarioId
                p.latitud = gps.latitude.toString()
                p.longitud = gps.longitude.toString()
                p.estadoCodigo = "120"
                p.fecha = editTextFecha.text.toString()
                p.obraTd = editTextObra.text.toString()
                p.coordinadorDni = editTextCoordinador.text.toString()
                p.suministro = editTextSuministro.text.toString()
                p.sed = editTextSed.text.toString()
                p.observacion = editTextObservacion.text.toString()
                p.descripcion = textViewDescripcion.text.toString()
                p.descripcionCoordinador = textViewDescripcionCoordinador.text.toString()
                p.fechaMovil = Util.getFecha()
                parteDiarioViewModel.validateParteDiario(p)
            }
        } else {
            gps.showSettingsAlert(context!!)
        }
    }

    private fun menssage() {
        parteDiarioViewModel.error.observe(viewLifecycleOwner, Observer { m ->
            Util.hideKeyboardFrom(context!!, view!!)
            Util.toastMensaje(context!!, m)
        })
    }

    private fun success() {
        parteDiarioViewModel.success.observe(viewLifecycleOwner, Observer { m ->
            viewPager?.currentItem = 1
            Util.hideKeyboardFrom(context!!, view!!)
            Util.toastMensaje(context!!, m)
        })
    }
}