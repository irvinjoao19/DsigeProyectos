package com.dsige.dsigeproyectos.ui.activities.logistica

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
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
import com.dsige.dsigeproyectos.data.local.model.engie.Area
import com.dsige.dsigeproyectos.data.local.model.engie.CentroCostos
import com.dsige.dsigeproyectos.data.local.model.engie.Sucursal
import com.dsige.dsigeproyectos.data.local.model.logistica.Orden
import com.dsige.dsigeproyectos.data.local.model.logistica.Pedido
import com.dsige.dsigeproyectos.data.viewModel.LogisticaViewModel
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.ui.adapters.*
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_aprobation_detail.*
import javax.inject.Inject

class AprobationDetailActivity : DaggerAppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabAprobar -> messageDialog(1, "Cantidad de Aprobada")
            R.id.fabRechazar -> messageDialog(2, "Rechazar Requerimiento")
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var logisticaViewModel: LogisticaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aprobation_detail)
        val b = intent.extras
        if (b != null) {
            bindUI(b.getString("codigo")!!, b.getInt("tipo"), b.getString("title")!!)
        }
    }

    private fun bindUI(codigo: String, tipo: Int, title: String) {
        logisticaViewModel =
            ViewModelProvider(this, viewModelFactory).get(LogisticaViewModel::class.java)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        fabAprobar.setOnClickListener(this)
        fabRechazar.setOnClickListener(this)

        when (tipo) {
            1 -> {
                logisticaViewModel.getPedidoGroupOne(codigo).observe(this, Observer {
                    layout1.visibility = View.VISIBLE
                    textView1.text = it.nombreTipoPedido
                    textView2.text = it.nombreEmpleado
                    textView3.text = it.centroCostos
                    textView4.text = String.format("Tot. IGV %s", it.cantidad)
                    textView5.text = it.estado
                    textView6.text = it.fechaEnvio
                    textView7.text = it.moneda
                })

                val pedidoAdapter =
                    PedidoDetalleAdapter(object : OnItemClickListener.PedidoListener {
                        override fun onItemClick(p: Pedido, v: View, position: Int) {

                        }
                    })
                recyclerView.itemAnimator = DefaultItemAnimator()
                val layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = pedidoAdapter

                logisticaViewModel.getPedidoByCodigo(codigo).observe(this, Observer {
                    pedidoAdapter.addItems(it)
                })
            }

            2 -> {
                logisticaViewModel.getOrdenGroupOne(codigo).observe(this, Observer {
                    layout2.visibility = View.VISIBLE
                    textView11.text = it.toc
                    textView12.text = it.nroOrden
                    textView13.text = it.contableOt
                    textView14.text = it.usuCreaOrden
                    textView15.text = it.usuSolicitante
                    textView16.text = it.provee
                    textView17.text = it.forma
                    textView18.text = it.fechaEmisionOrden
                    textView30.text = String.format("%s", it.subtotalOc)
                    textView31.text = String.format("%s", it.igv)
                    textView32.text = String.format("%s", it.totalOc)
                    textView33.text = it.mone
                })

                val ordenAdapter =
                    OrdenDetalleAdapter(object : OnItemClickListener.OrdenListener {
                        override fun onItemClick(o: Orden, v: View, position: Int) {

                        }
                    })
                recyclerView.itemAnimator = DefaultItemAnimator()
                val layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = ordenAdapter

                logisticaViewModel.getOrdenByCodigo(codigo).observe(this, Observer {
                    ordenAdapter.addItems(it)
                })
            }
        }
    }


    private fun messageDialog(tipo: Int, title: String) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(this).inflate(R.layout.dialog_aprobacion, null)


        val textviewTitle: TextView = v.findViewById(R.id.textviewTitle)
        val editText1: EditText = v.findViewById(R.id.editText1)
        val buttonAceptar: MaterialButton = v.findViewById(R.id.buttonAceptar)
        val buttonCancelar: MaterialButton = v.findViewById(R.id.buttonCancelar)
        textviewTitle.text = String.format("%s", title)
        if (tipo == 1) {
            editText1.inputType = InputType.TYPE_CLASS_NUMBER;
        }
        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        buttonAceptar.setOnClickListener {


            dialog.dismiss()
        }
        buttonCancelar.setOnClickListener {
            dialog.dismiss()
        }
    }
}