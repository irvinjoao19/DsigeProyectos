package com.dsige.dsigeproyectos.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.viewModel.UsuarioViewModel
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.helper.Permission
import com.dsige.dsigeproyectos.helper.Util
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : DaggerAppCompatActivity(), View.OnClickListener {

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        var cantidad = 0
        when (requestCode) {
            1 -> {
                for (valor: Int in grantResults) {
                    if (valor == PackageManager.PERMISSION_DENIED) {
                        cantidad += 1
                    }
                }
                if (cantidad > 0) {
                    buttonEnviar.visibility = View.GONE
                    messagePermission()
                } else {
                    buttonEnviar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.buttonEnviar -> {
                val usuario = editTextUser.text.toString().trim()
                val pass = editTextPass.text.toString().trim()
                if (usuario.isNotEmpty()) {
                    if (pass.isNotEmpty()) {
                        load()
                        usuarioViewModel.getLogin(usuario, pass, pass, Util.getVersion(this))
                    } else {
                        Util.snackBarMensaje(window.decorView, "Ingrese password")
                    }
                } else {
                    Util.snackBarMensaje(window.decorView, "Ingrese usuario")
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var usuarioViewModel: UsuarioViewModel
    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        bindUI()
        if (Build.VERSION.SDK_INT >= 23) {
            permision()
        }
    }

    private fun permision() {
        if (!Permission.hasPermissions(this@LoginActivity, *Permission.PERMISSIONS)) {
            ActivityCompat.requestPermissions(
                this@LoginActivity, Permission.PERMISSIONS, Permission.PERMISSION_ALL
            )
        }
    }

    private fun bindUI() {
        usuarioViewModel =
            ViewModelProvider(this, viewModelFactory).get(UsuarioViewModel::class.java)
        textViewVersion.text = String.format("V %s", Util.getVersion(this))
        buttonEnviar.setOnClickListener(this)

        usuarioViewModel.error.observe(this, {
            closeLoad()
            Util.toastMensaje(this, it)
        })

        usuarioViewModel.success.observe(this, {
            closeLoad()
            goMainActivity()
        })
    }

    private fun load() {
        builder = AlertDialog.Builder(ContextThemeWrapper(this@LoginActivity, R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(this@LoginActivity).inflate(R.layout.dialog_login, null)
        builder.setView(view)
        val textViewTitle: TextView = view.findViewById(R.id.textView)
        textViewTitle.text = String.format("Iniciando Sesión")
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

    private fun goMainActivity() {
        startActivity(
            Intent(this, MainActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
    }

    private fun messagePermission() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this@LoginActivity, R.style.AppTheme))
        val dialog: AlertDialog

        builder.setTitle("Permisos Denegados")
        builder.setMessage("Debes de aceptar los permisos para poder acceder al aplicativo.")
        builder.setPositiveButton("Aceptar") { dialogInterface, _ ->
            permision()
            dialogInterface.dismiss()
        }
        dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
    }
}