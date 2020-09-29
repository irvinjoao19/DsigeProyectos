package com.dsige.dsigeproyectos.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.Usuario
import com.dsige.dsigeproyectos.data.viewModel.*
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.fragments.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import javax.inject.Inject

class PrincipalActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var usuarioViewModel: UsuarioViewModel
    lateinit var builder: AlertDialog.Builder
    var dialog: AlertDialog? = null
    var usuarioId: String = ""
    var logout: String = "off"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        bindUI()
    }

    private fun bindUI() {
        usuarioViewModel =
            ViewModelProvider(this, viewModelFactory).get(UsuarioViewModel::class.java)
        usuarioViewModel.user.observe(this, Observer { u ->
            if (u != null) {
                setSupportActionBar(toolbar)
                val toggle = ActionBarDrawerToggle(
                    this@PrincipalActivity,
                    drawerLayout,
                    toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close
                )
                drawerLayout.addDrawerListener(toggle)
                toggle.syncState()
                navigationView.setNavigationItemSelectedListener(this@PrincipalActivity)
                getUser(u)
                fragmentByDefault()
                message()
            } else {
                goLogin()
            }
        })
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.vehiculos -> changeFragment(VehicleFragment.newInstance("", ""), "Vehiculos")
            R.id.reparacion -> changeFragment(
                MainFragment.newInstance(1, usuarioId),
                "Reparación de Veredas"
            )
//            R.id.trabajo -> changeFragment(
//                MainFragment.newInstance(2, usuarioId),
//                "Trabajos SS"
//            )
            R.id.logout -> dialogLogout()
//            R.id.envio -> changeFragment(
//                SendFragment.newInstance("", ""),
//                "Enviar Pendientes"
//            )
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun goActivity(i: Intent) {
        startActivity(i)
    }

    private fun load() {
        builder = AlertDialog.Builder(ContextThemeWrapper(this@PrincipalActivity, R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(this@PrincipalActivity).inflate(R.layout.dialog_login, null)
        builder.setView(view)
        val textViewTitle: TextView = view.findViewById(R.id.textView)
        textViewTitle.text = String.format("%s", "Cerrando Session")
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

    private fun changeFragment(fragment: Fragment, title: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit()
        supportActionBar!!.title = title
    }

    private fun fragmentByDefault() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content_frame, MainFragment.newInstance(1, usuarioId))
            .commit()
        supportActionBar!!.title = "Reparación de Veredas"
        navigationView.menu.getItem(1).isChecked = true
    }

    private fun getUser(u: Usuario) {
        val header = navigationView.getHeaderView(0)
        header.textViewName.text = u.nombre
        header.textViewEmail.text = String.format("Cod : %s", u.usuarioId)
        header.textViewVersion.text = String.format("Versión : %s", Util.getVersion(this))
        usuarioId = u.usuarioId
    }

    private fun goLogin() {
        if (logout == "off") {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun message() {
        usuarioViewModel.success.observe(this, Observer { s ->
            if (s != null) {
                closeLoad()
                if (s == "Close") {
                    val intent = Intent(this@PrincipalActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Util.toastMensaje(this, s)
                }
            }
        })
        usuarioViewModel.error.observe(this@PrincipalActivity, Observer { s ->
            if (s != null) {
                closeLoad()
                Util.snackBarMensaje(window.decorView, s)
            }
        })
    }

    private fun dialogLogout() {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Mensaje")
            .setMessage("Deseas Salir ?")
            .setPositiveButton("SI") { dialog, _ ->
                logout = "on"
                load()
                usuarioViewModel.logout()
                dialog.dismiss()
            }
            .setNegativeButton("NO") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
    }
}