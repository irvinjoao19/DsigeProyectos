package com.dsige.dsigeproyectos.ui.activities.logistica

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.Query
import com.dsige.dsigeproyectos.data.local.model.logistica.Anulacion
import com.dsige.dsigeproyectos.data.local.model.logistica.Orden
import com.dsige.dsigeproyectos.data.local.model.logistica.OrdenDetalle
import com.dsige.dsigeproyectos.data.local.model.logistica.Pedido
import com.dsige.dsigeproyectos.data.viewModel.LogisticaViewModel
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.adapters.*
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_aprobation_detail.*
import kotlinx.android.synthetic.main.cardview_pedido_detalle.view.*
import javax.inject.Inject

class AprobationDetailActivity : DaggerAppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabAprobar -> messageDialog(tipo, "A", "Aprobar Orden de Compra")
            R.id.fabRechazar -> messageDialog(tipo, "D", "Rechazar Orden de Compra")
            R.id.fabAnular -> messageDialog(tipo, "D", "Anular Orden de Compra")
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var logisticaViewModel: LogisticaViewModel
    private var usuarioId: String = ""
    private var tipo: Int = 0
    private var cabeceraId: Int = 0
    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aprobation_detail)
        val b = intent.extras
        if (b != null) {
            bindUI(
                b.getString("codigo")!!, b.getInt("tipo"), b.getString("title")!!,
                b.getString("usuarioId")!!, b.getInt("id")
            )
        }
    }

    private fun bindUI(codigo: String, t: Int, title: String, u: String, id: Int) {
        usuarioId = u
        tipo = t
        cabeceraId = id
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
        fabAnular.setOnClickListener(this)

        when (t) {
            1 -> {
                logisticaViewModel.getPedidoGroupOne(codigo).observe(this, {
                    layout1.visibility = View.VISIBLE
                    textView1.text = it.nombreTipoPedido
                    textView2.text = it.nombreEmpleado
                    textView3.text = it.centroCostos
                    textView4.text = String.format("Tot. IGV %.4f", it.cantidad)
                    textView5.text = it.estado
                    textView6.text = it.fechaEnvio
                    textView7.text = it.moneda
                })

                val pedidoAdapter =
                    PedidoDetalleAdapter(object : OnItemClickListener.PedidoListener {
                        override fun onItemClick(p: Pedido, v: View, position: Int) {
                            when (v.id) {
                                R.id.textView31 -> updateCantidad(p)
                            }
                        }
                    })
                recyclerView.itemAnimator = DefaultItemAnimator()
                val layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = pedidoAdapter

                logisticaViewModel.getPedidoByCodigo(codigo).observe(this, {
                    pedidoAdapter.addItems(it)
                })
            }
            2 -> {
                logisticaViewModel.getOrdenGroupOne(codigo).observe(this, {
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
                    OrdenGroupAdapter(object : OnItemClickListener.OrdenListener {
                        override fun onItemClick(o: Orden, v: View, position: Int) {
                            when (v.id) {
                                R.id.imgHistorico -> dialogDetalle(o.articulo)
                            }
                        }
                    })
                recyclerView.itemAnimator = DefaultItemAnimator()
                val layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = ordenAdapter
                logisticaViewModel.getOrdenByCodigo(codigo).observe(this, {
                    ordenAdapter.addItems(it)
                })
            }
            3 -> {
                fabAprobar.visibility = View.GONE
                fabRechazar.visibility = View.GONE
                fabAnular.visibility = View.VISIBLE
                logisticaViewModel.getAnulacionGroupOne(codigo).observe(this, {
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
                    AnulacionGroupAdapter(object : OnItemClickListener.AnulacionListener {
                        override fun onItemClick(a: Anulacion, v: View, position: Int) {
                        }
                    })
                recyclerView.itemAnimator = DefaultItemAnimator()
                val layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = ordenAdapter
                logisticaViewModel.getAnulacionByCodigo(codigo).observe(this, {
                    ordenAdapter.addItems(it)
                })
            }
        }

        logisticaViewModel.mensajeSuccess.observe(this, {
            closeLoad()
            Util.toastMensaje(this, it)
            finish()
        })
        logisticaViewModel.mensajeError.observe(this, {
            closeLoad()
            Util.toastMensaje(this, it)
        })
    }

    private fun updateCantidad(p: Pedido) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(this).inflate(R.layout.dialog_aprobacion, null)
        val textviewTitle: TextView = v.findViewById(R.id.textviewTitle)
        val editText1: EditText = v.findViewById(R.id.editText1)
        val editText2: EditText = v.findViewById(R.id.editText2)
        val buttonAceptar: MaterialButton = v.findViewById(R.id.buttonAceptar)
        val buttonCancelar: MaterialButton = v.findViewById(R.id.buttonCancelar)
        textviewTitle.text = String.format("%s", "Cantidad Aprobada")
        editText1.visibility = View.GONE
        editText2.visibility = View.VISIBLE

        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        buttonAceptar.setOnClickListener {
            if (editText2.text.toString().isEmpty()) {
                Util.toastMensaje(this, "Ingrese cantidad")
                return@setOnClickListener
            }
            val q = Query(usuarioId, p.id, p.articulo, editText2.text.toString().toDouble())
            logisticaViewModel.sendUpdateCantidad(q, p.id)
            dialog.dismiss()
        }
        buttonCancelar.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun messageDialog(formato: Int, tipo: String, title: String) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(this).inflate(R.layout.dialog_aprobacion, null)
        val textviewTitle: TextView = v.findViewById(R.id.textviewTitle)
        val editText1: EditText = v.findViewById(R.id.editText1)
        val buttonAceptar: MaterialButton = v.findViewById(R.id.buttonAceptar)
        val buttonCancelar: MaterialButton = v.findViewById(R.id.buttonCancelar)
        textviewTitle.text = String.format("%s", title)
        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        buttonAceptar.setOnClickListener {

            val obs = editText1.text.toString()
            val q = Query(cabeceraId, usuarioId, tipo, obs,"")

            when (formato) {
                1, 2 -> logisticaViewModel.sendAprobarORechazar(formato, q, cabeceraId)
                3 -> logisticaViewModel.sendAnulacion(formato, q, cabeceraId)
            }

            load()
            dialog.dismiss()
        }
        buttonCancelar.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun dialogDetalle(articulo: String) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(this).inflate(R.layout.dialog_historico, null)

        val textViewTitulo: TextView = v.findViewById(R.id.textViewTitulo)
        val textViewArticulo: TextView = v.findViewById(R.id.textViewArticulo)
        val recyclerView: RecyclerView = v.findViewById(R.id.recyclerView)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        textViewTitulo.text = String.format("Historico de Precios")
        textViewArticulo.text = articulo

        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        val ordenDetalleAdapter =
            OrdenDetalleAdapter(object : OnItemClickListener.OrdenDetalleListener {
                override fun onItemClick(o: OrdenDetalle, v: View, position: Int) {
                }
            })

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        )
        recyclerView.adapter = ordenDetalleAdapter
        logisticaViewModel.getOrdenDetalleByCodigo(articulo).observe(this, {
            ordenDetalleAdapter.addItems(it)
        })
    }

    private fun load() {
        builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(this).inflate(R.layout.dialog_login, null)
        builder.setView(view)
        val textViewTitle: TextView = view.findViewById(R.id.textView)
        textViewTitle.text = String.format("Enviando...")
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
}