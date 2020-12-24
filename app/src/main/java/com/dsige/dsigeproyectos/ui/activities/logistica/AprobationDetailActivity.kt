package com.dsige.dsigeproyectos.ui.activities.logistica

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.dsige.dsigeproyectos.data.local.model.logistica.*
import com.dsige.dsigeproyectos.data.viewModel.LogisticaViewModel
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.adapters.*
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_aprobation_detail.*
import javax.inject.Inject

class AprobationDetailActivity : DaggerAppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabAprobarItems -> aprobarItems(tipo)
            R.id.fabAprobar -> if (tipo == 2) {
                confirmComentarioAprobar(tipo)
            } else {
                aprobarOrdenCompra(tipo)
            }
            R.id.fabRechazar -> messageDialog(tipo, "D")
            R.id.fabAnular -> messageDialog(tipo, "D")
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var logisticaViewModel: LogisticaViewModel
    private var usuarioId: String = ""
    private var tipo: Int = 0
    private var cabeceraId: Int = 0
    private var codigo: String = ""
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

    private fun bindUI(c: String, t: Int, title: String, u: String, id: Int) {
        usuarioId = u
        tipo = t
        cabeceraId = id
        codigo = c
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
        fabAprobarItems.setOnClickListener(this)

        when (t) {
            1 -> {
                logisticaViewModel.getPedidoGroupOne(c).observe(this, {
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
                                R.id.textView31 -> updateCantidadPedido(p)
                            }
                        }
                    })
                recyclerView.itemAnimator = DefaultItemAnimator()
                val layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = pedidoAdapter

                logisticaViewModel.getPedidoByCodigo(c).observe(this, {
                    pedidoAdapter.addItems(it)
                })
            }
            2 -> {
                logisticaViewModel.getOrdenGroupOne(c).observe(this, {
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
                logisticaViewModel.getOrdenByCodigo(c).observe(this, {
                    ordenAdapter.addItems(it)
                })
            }
            3 -> {
                fabAprobar.visibility = View.GONE
                fabRechazar.visibility = View.GONE
                fabAnular.visibility = View.VISIBLE
                logisticaViewModel.getAnulacionGroupOne(c).observe(this, {
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
                logisticaViewModel.getAnulacionByCodigo(c).observe(this, {
                    ordenAdapter.addItems(it)
                })
            }
            4 -> {
                fabAprobarItems.visibility = View.VISIBLE
                logisticaViewModel.getCampoJefeGroupOne(c.toInt()).observe(this, {
                    layout2.visibility = View.VISIBLE
                    layout3.visibility = View.GONE
                    textView11.text = it.pedcNumero
                    textView12.text = it.obraCodigo
                    textView13.text = it.almaCodigo
                    textView14.text = it.nomApellidos
                    textView15.text = it.nombreOt
                    textView16.text = it.usuarioCrea
                    textView18.text = String.format("Fec Ped.: %s", it.pedcFechaEnvio)
                    textView17.visibility = View.GONE
                    textView30.visibility = View.GONE
                    textView31.visibility = View.GONE
                    textView32.visibility = View.GONE
                    textView33.visibility = View.GONE
                })

                val campoJefeGroupAdapter =
                    CampoJefeGroupAdapter(object : OnItemClickListener.CampoJefeListener {
                        override fun onItemClick(c: CampoJefe, v: View, position: Int) {
                            when (v.id) {
                                R.id.textView31 -> updateCantidadCampoJefe(c)
                            }
                        }
                    })
                recyclerView.itemAnimator = DefaultItemAnimator()
                val layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = campoJefeGroupAdapter
                logisticaViewModel.getCampoJefeByCodigo(c.toInt()).observe(this, {
                    campoJefeGroupAdapter.addItems(it)
                })
            }
            5 -> {
                fabAprobarItems.visibility = View.VISIBLE
                logisticaViewModel.getCampoTiempoVidaOne(c.toInt()).observe(this, {
                    layout2.visibility = View.VISIBLE
                    layout3.visibility = View.GONE
                    textView11.text = it.pedcNumero
                    textView12.text = it.obraCodigo
                    textView13.text = it.almaCodigo
                    textView14.text = it.nomApellidos
                    textView15.text = it.nombreOt
                    textView16.text = it.usuarioCrea
                    textView18.text = String.format("Fec Ped.: %s", it.pedcFechaEnvio)
                    textView17.visibility = View.GONE
                    textView30.visibility = View.GONE
                    textView31.visibility = View.GONE
                    textView32.visibility = View.GONE
                    textView33.visibility = View.GONE
                })

                val tiempoVidaGroupAdapter =
                    TiempoVidaGroupAdapter(object : OnItemClickListener.TiempoVidaListener {
                        override fun onItemClick(t: TiempoVida, v: View, position: Int) {
                            when (v.id) {
                                R.id.textView31 -> updateCantidadTiempoVida(t)
                            }
                        }
                    })
                recyclerView.itemAnimator = DefaultItemAnimator()
                val layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = tiempoVidaGroupAdapter
                logisticaViewModel.getTiempoVidaByCodigo(c.toInt()).observe(this, {
                    tiempoVidaGroupAdapter.addItems(it)
                })
            }
        }

        logisticaViewModel.mensajeSuccess.observe(this, {
            closeLoad()
            Util.toastMensaje(this, it)
            if (it != "Cantidades Aprobadas..") {
                finish()
            }
        })
        logisticaViewModel.mensajeError.observe(this, {
            closeLoad()
            Util.toastMensaje(this, it)
        })
    }

    private fun updateCantidadPedido(p: Pedido) {
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
            if (editText2.text.toString().toDouble() > p.cantidad) {
                Util.toastMensaje(this, "Cantidad debe ser menor o igual a la cantidad solicitada")
                return@setOnClickListener
            }
            val q = Query(usuarioId, p.id, p.articulo, editText2.text.toString().toDouble(), "1")
            logisticaViewModel.sendUpdateCantidad(q, p.id, 1)
            dialog.dismiss()
        }
        buttonCancelar.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun updateCantidadCampoJefe(p: CampoJefe) {
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
            if (editText2.text.toString().toDouble() > p.cantidadAprobada) {
                Util.toastMensaje(this, "Cantidad debe ser menor o igual a la cantidad aprobada")
                return@setOnClickListener
            }
            val q = Query(usuarioId, p.id, p.articulo, editText2.text.toString().toDouble(), "1")
            logisticaViewModel.sendUpdateCantidad(q, p.id, 4)
            dialog.dismiss()
        }
        buttonCancelar.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun updateCantidadTiempoVida(p: TiempoVida) {
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
            if (editText2.text.toString().toDouble() > p.cantidadAprobada) {
                Util.toastMensaje(this, "Cantidad debe ser menor o igual a la cantidad aprobada")
                return@setOnClickListener
            }
            val q = Query(usuarioId, p.id, p.articulo, editText2.text.toString().toDouble(), "2")
            logisticaViewModel.sendUpdateCantidad(q, p.id, 5)
            dialog.dismiss()
        }
        buttonCancelar.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun messageDialog(formato: Int, tipo: String) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(this).inflate(R.layout.dialog_aprobacion, null)
        val textviewTitle: TextView = v.findViewById(R.id.textviewTitle)
        val editText1: EditText = v.findViewById(R.id.editText1)
        val buttonAceptar: MaterialButton = v.findViewById(R.id.buttonAceptar)
        val buttonCancelar: MaterialButton = v.findViewById(R.id.buttonCancelar)
        textviewTitle.text = when (formato) {
            1 -> "Rechazar Pedido"
            2 -> if (tipo == "A") "Aprobar Orden" else "Rechazar Orden"
            3 -> "Anular Orden de Compra"
            4 -> "Rechazar Campo por Jefe"
            else -> "Rechazar Campo por Tiempo de Vida"
        }
        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        buttonAceptar.setOnClickListener {

            val obs = editText1.text.toString()
            val q = Query(cabeceraId, usuarioId, tipo, obs, "")

            when (formato) {
                1 -> logisticaViewModel.sendAprobarORechazarPedido(formato, q, cabeceraId)
                2 -> logisticaViewModel.sendAprobarORechazarOrden(formato, q, cabeceraId)
                3 -> logisticaViewModel.sendAnulacion(formato, q, cabeceraId)
                4 -> logisticaViewModel.sendAprobarORechazarCampoJefeTiempoVida(
                    formato, q, cabeceraId
                )
                5 -> logisticaViewModel.sendAprobarORechazarCampoJefeTiempoVida(
                    formato, q, cabeceraId
                )
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

    private fun aprobarOrdenCompra(formato: Int) {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Mensaje")
            .setMessage(
                when (formato) {
                    1 -> "Deseas aprobar Pedido"
                    2 -> "Deseas aprobar Orden"
                    4 -> "Deseas aprobar Campo por Jefe"
                    else -> "Deseas aprobar Campo por Tiempo de Vida"
                }
            )
            .setPositiveButton("Aceptar") { dialog, _ ->
                load()
                val q = Query(cabeceraId, usuarioId, "A", "", "")
                when (formato) {
                    1 -> logisticaViewModel.sendAprobarORechazarPedido(formato, q, cabeceraId)
                    2 -> logisticaViewModel.sendAprobarORechazarOrden(formato, q, cabeceraId)
                    3 -> logisticaViewModel.sendAnulacion(formato, q, cabeceraId)
                    4 -> logisticaViewModel.sendAprobarORechazarCampoJefeTiempoVida(
                        formato, q, cabeceraId
                    )
                    5 -> logisticaViewModel.sendAprobarORechazarCampoJefeTiempoVida(
                        formato, q, cabeceraId
                    )
                }

                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
    }

    private fun aprobarItems(formato: Int) {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Mensaje")
            .setMessage("Deseas aprobar Items")
            .setPositiveButton("Aceptar") { dialog, _ ->
                load()
                val tipo = when (formato) {
                    4 -> "1"
                    else -> "2"
                }
                val q = Query(cabeceraId, usuarioId, tipo, "", codigo)
                when (formato) {
                    4 -> logisticaViewModel.aprobarItemsCampoJefeTiempoVida(formato, q)
                    5 -> logisticaViewModel.aprobarItemsCampoJefeTiempoVida(formato, q)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
    }

    private fun confirmComentarioAprobar(tipo: Int) {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Mensaje")
            .setMessage("Desea agregar un comentario ?")
            .setPositiveButton("SI") { dialog, _ ->
                messageDialog(tipo, "A")
                dialog.dismiss()
            }
            .setNegativeButton("NO") { dialog, _ ->
                aprobarOrdenCompra(tipo)
                dialog.cancel()
            }
        dialog.show()
    }
}