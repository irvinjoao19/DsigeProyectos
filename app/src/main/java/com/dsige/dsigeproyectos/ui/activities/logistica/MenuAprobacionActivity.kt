package com.dsige.dsigeproyectos.ui.activities.logistica

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.MenuPrincipal
import com.dsige.dsigeproyectos.ui.activities.engie.RequerimientoSolicitudActivity
import com.dsige.dsigeproyectos.ui.adapters.MenuAdapter
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.activity_sub_main.*

class MenuAprobacionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_aprobacion)
        val b = intent.extras
        if (b != null) {
            bindUI(b.getString("title")!!,b.getString("usuarioId")!!)
        }
    }

    private fun bindUI(title: String, u: String) {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val menuAdapter = MenuAdapter(object : OnItemClickListener.MenuListener {
            override fun onItemClick(m: MenuPrincipal, view: View, position: Int) {
                startActivity(
                    Intent(this@MenuAprobacionActivity, AprobacionActivity::class.java)
                        .putExtra("usuarioId", u)
                        .putExtra("tipo", m.id)
                        .putExtra("title", m.title)
                )
            }
        })
        recyclerView.itemAnimator = DefaultItemAnimator()
        val layoutManager = LinearLayoutManager(this)

        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = menuAdapter

        val menus: ArrayList<MenuPrincipal> = ArrayList()
        menus.add(MenuPrincipal(1, getString(R.string.aprobation1), R.drawable.ic_car_black))
        menus.add(MenuPrincipal(2, getString(R.string.aprobation2), R.drawable.ic_sync))
        menus.add(MenuPrincipal(3, getString(R.string.aprobation3), R.drawable.ic_sync))
        menus.add(MenuPrincipal(4, getString(R.string.aprobation4), R.drawable.ic_sync))
        menus.add(MenuPrincipal(5, getString(R.string.aprobation5), R.drawable.ic_sync))
        menuAdapter.addItems(menus)
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