package com.dsige.dsigeproyectos.ui.activities.logistica

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
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
import com.dsige.dsigeproyectos.data.local.model.Estado
import com.dsige.dsigeproyectos.data.local.model.Query
import com.dsige.dsigeproyectos.data.local.model.logistica.Anulacion
import com.dsige.dsigeproyectos.data.local.model.logistica.ComboEstado
import com.dsige.dsigeproyectos.data.local.model.logistica.Orden
import com.dsige.dsigeproyectos.data.local.model.logistica.Pedido
import com.dsige.dsigeproyectos.data.viewModel.LogisticaViewModel
import com.dsige.dsigeproyectos.data.viewModel.ParteDiarioViewModel
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.adapters.*
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_aprobacion.*
import kotlinx.android.synthetic.main.activity_aprobacion.editTextEstado
import kotlinx.android.synthetic.main.activity_aprobacion.recyclerView
import kotlinx.android.synthetic.main.activity_aprobacion.toolbar
import kotlinx.android.synthetic.main.activity_parte_diario.*
import kotlinx.android.synthetic.main.activity_sub_main.*
import javax.inject.Inject

class AprobacionActivity : DaggerAppCompatActivity(), View.OnClickListener {

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.send -> {
                val fi = editTextFinicio.text.toString()
                val ff = editTextFfinal.text.toString()
                if (fi.isEmpty()) {
                    logisticaViewModel.setError("Seleccione Fecha Inicial")
                    return false
                }
                if (ff.isEmpty()) {
                    logisticaViewModel.setError("Seleccione Fecha Final")
                    return false
                }
                logisticaViewModel.setLoading(true)
                logisticaViewModel.getSyncAnulacion(usuarioId, fi, ff)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_anulacion, menu)
        if (tipo != 3) {
            menu.findItem(R.id.search).setVisible(false).isEnabled = false
        }
        return true
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.editTextEstado -> spinnerDialog()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var logisticaViewModel: LogisticaViewModel
    lateinit var q: Query
    private var tipo: Int = 0
    private var usuarioId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aprobacion)
        q = Query()

        val b = intent.extras
        if (b != null) {
            bindUI(b.getString("usuarioId")!!, b.getInt("tipo"), b.getString("title")!!)
        }
    }

    private fun bindUI(u: String, t: Int, title: String) {
        tipo = t
        usuarioId = u
        logisticaViewModel =
            ViewModelProvider(this, viewModelFactory).get(LogisticaViewModel::class.java)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        when (t) {
            1 -> {
                logisticaViewModel.setLoading(true)
                logisticaViewModel.getSyncPedido(u)
                val pedidoAdapter = PedidoAdapter(object : OnItemClickListener.PedidoListener {
                    override fun onItemClick(p: Pedido, v: View, position: Int) {
                        startActivity(
                            Intent(this@AprobacionActivity, AprobationDetailActivity::class.java)
                                .putExtra("codigo", p.nroPedido)
                                .putExtra("tipo", tipo)
                                .putExtra("title", title)
                                .putExtra("usuarioId", u)
                                .putExtra("id", p.id)
                        )
                    }
                })

                recyclerView.itemAnimator = DefaultItemAnimator()
                val layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = pedidoAdapter

                logisticaViewModel.getPedidoGroup().observe(this, {
                    pedidoAdapter.addItems(it)
                })
                logisticaViewModel.pedidoSearch.value = null
            }
            2 -> {
                logisticaViewModel.setLoading(true)
                logisticaViewModel.getSyncOrden(u)
                val ordenAdapter = OrdenAdapter(object : OnItemClickListener.OrdenListener {
                    override fun onItemClick(o: Orden, v: View, position: Int) {
                        startActivity(
                            Intent(this@AprobacionActivity, AprobationDetailActivity::class.java)
                                .putExtra("codigo", o.nroOrden)
                                .putExtra("tipo", tipo)
                                .putExtra("title", title)
                                .putExtra("usuarioId", u)
                                .putExtra("id", o.id)
                        )
                    }
                })

                recyclerView.itemAnimator = DefaultItemAnimator()
                val layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = ordenAdapter

                logisticaViewModel.getOrdenGroup().observe(this, {
                    ordenAdapter.addItems(it)
                })
                logisticaViewModel.ordenSearch.value = null
            }
            3 -> {
                editTextEstado.visibility = View.GONE
                layout3.visibility = View.VISIBLE
                editTextFinicio.setText(Util.getFirstDay())
                editTextFfinal.setText(Util.getLastDay())
                logisticaViewModel.getSyncAnulacion(u, Util.getFirstDay(), Util.getLastDay())
                val anulacionAdapter =
                    AnulacionAdapter(object : OnItemClickListener.AnulacionListener {
                        override fun onItemClick(a: Anulacion, v: View, position: Int) {
                            startActivity(
                                Intent(
                                    this@AprobacionActivity,
                                    AprobationDetailActivity::class.java
                                )
                                    .putExtra("codigo", a.nroOrden)
                                    .putExtra("tipo", tipo)
                                    .putExtra("title", title)
                                    .putExtra("usuarioId", u)
                                    .putExtra("id", a.id)
                            )
                        }
                    })

                recyclerView.itemAnimator = DefaultItemAnimator()
                val layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = anulacionAdapter

                logisticaViewModel.getAnulacionGroup().observe(this, {
                    anulacionAdapter.addItems(it)
                })
                logisticaViewModel.ordenSearch.value = null
            }
        }

        logisticaViewModel.loading.observe(this, {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })

        logisticaViewModel.mensajeSuccess.observe(this, {
            Util.toastMensaje(this, it)
        })
        logisticaViewModel.mensajeError.observe(this, {
            Util.toastMensaje(this, it)
        })

        editTextEstado.setOnClickListener(this)
    }

    private fun spinnerDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(this).inflate(R.layout.dialog_combo, null)
        val progressBar: ProgressBar = v.findViewById(R.id.progressBar)

        val textViewTitulo: TextView = v.findViewById(R.id.textViewTitulo)
        val layoutSearch: TextInputLayout = v.findViewById(R.id.layoutSearch)
        val recyclerView: RecyclerView = v.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(v.context)
        textViewTitulo.text = String.format("Delegación de Estados")

        layoutSearch.visibility = View.GONE
        progressBar.visibility = View.GONE

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

        val estadoAdapter = ComboEstadoAdapter(object : OnItemClickListener.ComboEstadoListener {
            override fun onItemClick(c: ComboEstado, v: View, position: Int) {
                q.estado = c.codigo
                when (tipo) {
                    1 -> logisticaViewModel.pedidoSearch.value = Gson().toJson(q)
                    2 -> logisticaViewModel.ordenSearch.value = Gson().toJson(q)
                }
                editTextEstado.setText(c.nombre)
                dialog.dismiss()
            }
        })
        recyclerView.adapter = estadoAdapter
        logisticaViewModel.getCombosEstados().observe(this, {
            estadoAdapter.addItems(it)
        })
    }
}