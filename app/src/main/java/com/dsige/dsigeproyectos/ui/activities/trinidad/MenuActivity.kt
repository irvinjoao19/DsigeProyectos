package com.dsige.dsigeproyectos.ui.activities.trinidad

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
import com.dsige.dsigeproyectos.data.local.model.trinidad.Registro
import com.dsige.dsigeproyectos.data.viewModel.RegistroViewModel
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.adapters.EstadoAdapter
import com.dsige.dsigeproyectos.ui.adapters.RegistroAdapter
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_menu.*
import javax.inject.Inject


class MenuActivity : DaggerAppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fab -> startActivity(
                Intent(this, RegistroActivity::class.java)
                    .putExtra("tipo", tipo)
                    .putExtra("usuarioId", usuarioId)
                    .putExtra("id", 0)
                    .putExtra("tipoDetalle", 0)
            )
            R.id.editTextEstado -> spinnerEstado()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var registroViewModel: RegistroViewModel
    private var tipo: Int = 0
    private var usuarioId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val b = intent.extras
        if (b != null) {
            bindUI(b.getString("title")!!, b.getInt("tipo"), b.getString("usuarioId")!!)
        }
    }

    private fun bindUI(title: String, t: Int, u: String) {
        registroViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegistroViewModel::class.java)

        tipo = t
        usuarioId = u

        setSupportActionBar(toolbar)
        supportActionBar!!.title = title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        fab.setOnClickListener(this)
        editTextEstado.setOnClickListener(this)

        val registroAdapter = RegistroAdapter(object : OnItemClickListener.RegistroListener {
            override fun onItemClick(r: Registro, view: View, position: Int) {
                if (r.tipo == 1) {
                    if (r.active == 0) {
                        startActivity(
                            Intent(this@MenuActivity, DetailActivity::class.java)
                                .putExtra("id", r.registroId)
                                .putExtra("obra", r.nroObra)
                                .putExtra("nroPoste", r.nroPoste)
                                .putExtra("tipo", r.tipo)
                                .putExtra("usuarioId", r.usuarioId)
                        )
                    } else
                        Util.toastMensaje(this@MenuActivity, "Registro Cerrado")
                } else {
                    if (r.active == 0) {
                        startActivity(
                            Intent(this@MenuActivity, RegistroActivity::class.java)
                                .putExtra("tipo", r.tipo)
                                .putExtra("usuarioId", r.usuarioId)
                                .putExtra("id", r.registroId)
                                .putExtra("tipoDetalle", 1)
                        )
                    } else
                        Util.toastMensaje(this@MenuActivity, "Registro Cerrado")
                }
            }
        })

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = registroAdapter

        registroViewModel.getRegistroByTipoPaging(tipo)
            .observe(this, Observer { s ->
                if (s != null) {
                    registroAdapter.addItems(s)
                }
            })
        registroViewModel.search.value = ""
    }

    private fun spinnerEstado() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(this).inflate(R.layout.dialog_combo, null)
        val textViewTitulo: TextView = v.findViewById(R.id.textViewTitulo)
        val recyclerView: RecyclerView = v.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context, DividerItemDecoration.VERTICAL
            )
        )
        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        textViewTitulo.text = String.format("Tipo de Estado")

        val estadoAdapter = EstadoAdapter(object : OnItemClickListener.EstadoListener {
            override fun onItemClick(c: Estado, view: View, position: Int) {
                editTextEstado.setText(c.nombre)
                registroViewModel.search.value = c.codigo
                dialog.dismiss()
            }
        })
        recyclerView.adapter = estadoAdapter
        registroViewModel.getEstados().observe(this, Observer { p ->
            if (p != null) {
                estadoAdapter.addItems(p)
            }
        })
    }
}