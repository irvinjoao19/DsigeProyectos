package com.dsige.dsigeproyectos.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.MenuPrincipal
import com.dsige.dsigeproyectos.data.viewModel.ParteDiarioViewModel
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.activities.engie.ParteDiarioActivity
import com.dsige.dsigeproyectos.ui.activities.engie.RequerimientoSolicitudActivity
import com.dsige.dsigeproyectos.ui.activities.logistica.MenuAprobacionActivity
import com.dsige.dsigeproyectos.ui.activities.logistica.RequerimientoActivity
import com.dsige.dsigeproyectos.ui.activities.trinidad.MenuActivity
import com.dsige.dsigeproyectos.ui.adapters.MenuAdapter
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_sub_main.*
import javax.inject.Inject

class SubMainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var parteDiarioViewModel: ParteDiarioViewModel
    private var cantidad: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_main)
        val b = intent.extras
        if (b != null) {
            bindUI(
                b.getInt("tipo"),
                b.getString("title")!!,
                b.getString("usuarioId")!!,
                b.getInt("estado")
            )
        }
    }

    private fun bindUI(tipo: Int, title: String, usuarioId: String, estado: Int) {
        parteDiarioViewModel =
            ViewModelProvider(this, viewModelFactory).get(ParteDiarioViewModel::class.java)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        parteDiarioViewModel.getParteDiarioPendiente().observe(this, Observer { p ->
            cantidad = p.size
        })

        val menuAdapter = MenuAdapter(object : OnItemClickListener.MenuListener {
            override fun onItemClick(m: MenuPrincipal, view: View, position: Int) {
                when (tipo) {
                    1 -> {
                        when (m.position) {
                            1 -> goRequerimientoSolicitudActivity(m, 1, "Salida")
                            2 -> {
                                if (m.id == 1) {
                                    if (cantidad != 0) {
                                        Util.dialogMensaje(
                                            this@SubMainActivity, "Mensaje",
                                            "Favor de enviar los Parte Diario."
                                        )
                                    } else {
                                        goRequerimientoSolicitudActivity(m, 2, "Devolución")
                                    }
                                } else {
                                    goRequerimientoSolicitudActivity(m, 2, "Devolución")
                                }
                            }
                        }
                    }
                    2 -> when (m.id) {
                        2 -> startActivity(
                            Intent(this@SubMainActivity, RequerimientoActivity::class.java)
                                .putExtra("usuarioId", usuarioId)
                        )
                        3 -> startActivity(
                            Intent(this@SubMainActivity, MenuAprobacionActivity::class.java)
                                .putExtra("title", m.title)
                                .putExtra("usuarioId", usuarioId)
                        )
                    }
                    4 -> when (m.id) {
                        1 -> startActivity(
                            Intent(this@SubMainActivity, ParteDiarioActivity::class.java)
                        )
                        4 -> Util.toastMensaje(this@SubMainActivity, m.title)
                        else -> startActivity(
                            Intent(this@SubMainActivity, MenuActivity::class.java)
                                .putExtra("title", m.title)
                                .putExtra("tipo", m.id)
                                .putExtra("usuarioId", usuarioId)
                        )
                    }
                }
            }
        })
        recyclerView.itemAnimator = DefaultItemAnimator()
        val layoutManager = LinearLayoutManager(this)

        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = menuAdapter

        val menus: ArrayList<MenuPrincipal> = ArrayList()
        when (tipo) {
            1 -> {
                menus.add(
                    MenuPrincipal(1, "Solicito Materiales para la Obra", R.drawable.ic_work, 1)
                )
                menus.add(
                    MenuPrincipal(1, "Devolver Materiales de una Obra", R.drawable.ic_work, 2)
                )
                menus.add(
                    MenuPrincipal(
                        2, "Solicito Materiales al Personal", R.drawable.ic_users_group, 1
                    )
                )
                menus.add(
                    MenuPrincipal(
                        2, "Devolver Materiales del Personal", R.drawable.ic_users_group, 2
                    )
                )
            }
            2 -> {
                menus.add(MenuPrincipal(1, "Stock de Materiales", R.drawable.ic_car_black))
                menus.add(MenuPrincipal(2, "Solicitud de Requerimiento", R.drawable.ic_sync))
                menus.add(MenuPrincipal(3, "Bandeja de Aprobaciones", R.drawable.ic_sync))
            }
            4 -> {
                menus.add(MenuPrincipal(1, "Parte Diario Obras", R.drawable.ic_car_black))
                menus.add(MenuPrincipal(2, "Trabajos SS", R.drawable.ic_sync))
                menus.add(MenuPrincipal(3, "Reparación Veredas", R.drawable.ic_sync))
                menus.add(MenuPrincipal(4, "Enviar Pendientes", R.drawable.ic_sync))
            }
        }
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