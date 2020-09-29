package com.dsige.dsigeproyectos.ui.activities.logistica

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.logistica.Orden
import com.dsige.dsigeproyectos.data.local.model.logistica.Pedido
import com.dsige.dsigeproyectos.data.viewModel.LogisticaViewModel
import com.dsige.dsigeproyectos.data.viewModel.ParteDiarioViewModel
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.ui.adapters.OrdenAdapter
import com.dsige.dsigeproyectos.ui.adapters.PedidoAdapter
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_aprobacion.*
import kotlinx.android.synthetic.main.activity_aprobacion.recyclerView
import kotlinx.android.synthetic.main.activity_aprobacion.toolbar
import kotlinx.android.synthetic.main.activity_sub_main.*
import javax.inject.Inject

class AprobacionActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var logisticaViewModel: LogisticaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aprobacion)
        val b = intent.extras
        if (b != null) {
            bindUI(b.getString("usuarioId")!!, b.getInt("tipo"), b.getString("title")!!)
        }
    }

    private fun bindUI(u: String, tipo: Int, title: String) {
        logisticaViewModel =
            ViewModelProvider(this, viewModelFactory).get(LogisticaViewModel::class.java)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        when (tipo) {
            1 -> {
                logisticaViewModel.getSyncPedido(u)
                val pedidoAdapter = PedidoAdapter(object : OnItemClickListener.PedidoListener {
                    override fun onItemClick(p: Pedido, v: View, position: Int) {
                        startActivity(
                            Intent(this@AprobacionActivity, AprobationDetailActivity::class.java)
                                .putExtra("codigo", p.nroPedido)
                                .putExtra("tipo", tipo)
                                .putExtra("title", title)
                        )
                    }
                })

                recyclerView.itemAnimator = DefaultItemAnimator()
                val layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = pedidoAdapter

                logisticaViewModel.getPedidoGroup().observe(this, Observer {
                    if (it.isNotEmpty()) {
                        progressBar.visibility = View.GONE
                    } else
                        progressBar.visibility = View.VISIBLE

                    pedidoAdapter.addItems(it)
                })
            }
            2 -> {
                logisticaViewModel.getSyncOrden(u)
                val ordenAdapter = OrdenAdapter(object : OnItemClickListener.OrdenListener {
                    override fun onItemClick(o: Orden, v: View, position: Int) {
                        startActivity(
                            Intent(this@AprobacionActivity, AprobationDetailActivity::class.java)
                                .putExtra("codigo", o.nroOrden)
                                .putExtra("tipo", tipo)
                                .putExtra("title", title)
                        )
                    }
                })

                recyclerView.itemAnimator = DefaultItemAnimator()
                val layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = ordenAdapter

                logisticaViewModel.getOrdenGroup().observe(this, Observer {
                    if (it.isNotEmpty()) {
                        progressBar.visibility = View.GONE
                    } else
                        progressBar.visibility = View.VISIBLE

                    ordenAdapter.addItems(it)
                })
            }
        }
    }
}