package com.dsige.dsigeproyectos.ui.activities.logistica

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.logistica.Requerimiento
import com.dsige.dsigeproyectos.data.local.model.logistica.RequerimientoEstado
import com.dsige.dsigeproyectos.data.viewModel.LogisticaViewModel
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.adapters.*
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_requerimiento.*
import javax.inject.Inject

class RequerimientoActivity : DaggerAppCompatActivity(), View.OnClickListener {

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.send -> confirmSend()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_logistica, menu)
        return true
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.editTextFecha -> Util.getDateDialog(this, editTextFecha)
            R.id.editTextEstado -> spinnerDialog()
//            R.id.editTextSearch -> spinnerDialog(1, 1)
            R.id.fabGenerate -> startActivity(
                Intent(this, RequerimientoFormActivity::class.java)
                    .putExtra("id", requerimientoId)
                    .putExtra("usuarioId", usuarioId)
            )
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var logisticaViewModel: LogisticaViewModel
    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null
    private var requerimientoId: Int = 0
    private var usuarioId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requerimiento)
        val b = intent.extras
        if (b != null) {
            usuarioId = b.getString("usuarioId")!!
            bindUI()
            Util.hideKeyboard(this)
        }
    }

    private fun bindUI() {
        logisticaViewModel =
            ViewModelProvider(this, viewModelFactory).get(LogisticaViewModel::class.java)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Lista de Requerimiento"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val requerimientoAdapter =
            RequerimientoAdapter(object : OnItemClickListener.RequerimientoListener {
                override fun onItemClick(r: Requerimiento, v: View, position: Int) {
                    when (v.id) {
                        R.id.imgDelete -> confirmDelete(r)
                        else -> startActivity(
                            Intent(
                                this@RequerimientoActivity,
                                RequerimientoFormActivity::class.java
                            )
                                .putExtra("id", r.requerimientoId)
                                .putExtra("usuarioId", r.usuario)
                        )
                    }
                }
            })

        recyclerView.itemAnimator = DefaultItemAnimator()
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = requerimientoAdapter
        logisticaViewModel.getRequerimientos().observe(this, {
            requerimientoAdapter.addItems(it)
        })

        logisticaViewModel.getRequerimientoId().observe(this, {
            requerimientoId = if (it == null || it == 0) 1 else it + 1
        })

        logisticaViewModel.mensajeSuccess.observe(this, {
            closeLoad()
            Util.toastMensaje(this, it)
        })

        logisticaViewModel.mensajeError.observe(this, {
            closeLoad()
            Util.toastMensaje(this, it)
        })
        editTextFecha.setText(Util.getFecha())
        editTextFecha.setOnClickListener(this)
        editTextEstado.setOnClickListener(this)
        fabGenerate.setOnClickListener(this)
    }

    private fun confirmDelete(r: Requerimiento) {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Mensaje")
            .setMessage(String.format("Deseas eliminar %s ?.", r.nroSolicitud))
            .setPositiveButton("Aceptar") { dialog, _ ->
                logisticaViewModel.deleteRequerimiento(r)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
    }

    private fun confirmSend() {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Mensaje")
            .setMessage(String.format("Deseas enviar las solicitudes ?."))
            .setPositiveButton("Aceptar") { dialog, _ ->
                load()
                logisticaViewModel.sendRequerimiento()
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
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

    private fun spinnerDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(this).inflate(R.layout.dialog_combo, null)

        val progressBar: ProgressBar = v.findViewById(R.id.progressBar)
        val textViewTitulo: TextView = v.findViewById(R.id.textViewTitulo)
        val layoutSearch: TextInputLayout = v.findViewById(R.id.layoutSearch)
        val recyclerView: RecyclerView = v.findViewById(R.id.recyclerView)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        progressBar.visibility = View.GONE
        layoutSearch.visibility = View.GONE

        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context, DividerItemDecoration.VERTICAL
            )
        )
        textViewTitulo.text = String.format("Estado")

        val estadoAdapter =
            RequerimientoEstadoAdapter(object :
                OnItemClickListener.RequerimientoEstadoListener {
                override fun onItemClick(c: RequerimientoEstado, v: View, position: Int) {
                    editTextEstado.setText(c.nombre)
                    dialog.dismiss()
                }
            })
        recyclerView.adapter = estadoAdapter
        logisticaViewModel.getEstados().observe(this, {
            estadoAdapter.addItems(it)
        })
    }
}