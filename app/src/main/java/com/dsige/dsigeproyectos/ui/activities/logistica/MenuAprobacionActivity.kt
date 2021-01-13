package com.dsige.dsigeproyectos.ui.activities.logistica

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.MenuPrincipal
import com.dsige.dsigeproyectos.data.local.model.logistica.MenuLogistica
import com.dsige.dsigeproyectos.data.viewModel.LogisticaViewModel
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.ui.activities.engie.RequerimientoSolicitudActivity
import com.dsige.dsigeproyectos.ui.adapters.MenuLogisticaAdapter
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_sub_main.*
import javax.inject.Inject

class MenuAprobacionActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var logisticaViewModel: LogisticaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_aprobacion)
        val b = intent.extras
        if (b != null) {
            bindUI(b.getString("title")!!,b.getString("usuarioId")!!)
        }
    }

    private fun bindUI(title: String, u: String) {
        logisticaViewModel =
            ViewModelProvider(this, viewModelFactory).get(LogisticaViewModel::class.java)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val menuAdapter = MenuLogisticaAdapter(object : OnItemClickListener.MenuLogisticaListener {
            override fun onItemClick(m: MenuLogistica, view: View, position: Int) {
                startActivity(
                    Intent(this@MenuAprobacionActivity, AprobacionActivity::class.java)
                        .putExtra("usuarioId", u)
                        .putExtra("tipo", m.orden)
                        .putExtra("title", m.nombre)
                )
            }
        })
        recyclerView.itemAnimator = DefaultItemAnimator()
        val layoutManager = LinearLayoutManager(this)

        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = menuAdapter

        logisticaViewModel.getMenuLogistica().observe(this,{
            menuAdapter.addItems(it)
        })
    }

    private fun goRequerimientoSolicitudActivity(m: MenuPrincipal, tipo: Int, subTitle: String) {
        startActivity(
            Intent(this, RequerimientoSolicitudActivity::class.java)
                .putExtra("tipoMaterialSolicitud", m.id)
                .putExtra("tipoSolicitudId", tipo)
                .putExtra("title", m.title)
                .putExtra("subTitle", subTitle)
        )
    }
}