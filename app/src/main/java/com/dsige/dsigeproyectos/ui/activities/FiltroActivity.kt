package com.dsige.dsigeproyectos.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
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
import com.dsige.dsigeproyectos.data.local.model.Query
import com.dsige.dsigeproyectos.data.local.model.Usuario
import com.dsige.dsigeproyectos.data.local.model.engie.Area
import com.dsige.dsigeproyectos.data.local.model.engie.CentroCostos
import com.dsige.dsigeproyectos.data.local.model.engie.Sucursal
import com.dsige.dsigeproyectos.data.viewModel.UsuarioViewModel
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.adapters.AreaAdapter
import com.dsige.dsigeproyectos.ui.adapters.CentroAdapter
import com.dsige.dsigeproyectos.ui.adapters.SucursalAdapter
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_filtro.*
import javax.inject.Inject

class FiltroActivity : DaggerAppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View) {
        when (v.id) {
            R.id.editTextSucursal -> spinnerDialog("Local/SED", 3)
            R.id.editTextArea -> spinnerDialog("Contrato", 1)
            R.id.editTextCentro -> spinnerDialog("Centro de Costos", 2)
            R.id.fabRegister -> {
                if (q.areaId != "") {
                    if (q.centroCostoId != "") {
                        load()
                        u.centroId = q.centroCostoId
                        u.areaId = q.areaId
                        u.estado = 1
                        usuarioViewModel.updateUsuario(u, q)
                    } else {
                        Util.snackBarMensaje(v, "Eliga un centro de costo")
                    }
                } else {
                    Util.snackBarMensaje(v, "Eliga un Area")
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var usuarioViewModel: UsuarioViewModel
    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null
    lateinit var q: Query
    lateinit var u: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filtro)
        q = Query()
        u = Usuario()
        bindUI()
    }

    private fun bindUI() {
        usuarioViewModel =
            ViewModelProvider(this, viewModelFactory).get(UsuarioViewModel::class.java)

        fabRegister.setOnClickListener(this)
        editTextSucursal.setOnClickListener(this)
        editTextArea.setOnClickListener(this)
        editTextCentro.setOnClickListener(this)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Sincronizar"

        usuarioViewModel.user.observe(this, Observer {
            u = it
            q.usuarioId = it.usuarioId
        })

        usuarioViewModel.error.observe(this@FiltroActivity, Observer { s ->
            if (s != null) {
                closeLoad()
                Util.snackBarMensaje(window.decorView, s)
            }
        })

        usuarioViewModel.success.observe(this@FiltroActivity, Observer { s ->
            if (s != null) {
                closeLoad()
                goMainActivity()
                Util.snackBarMensaje(window.decorView, s)
            }
        })
    }

    private fun spinnerDialog(title: String, tipo: Int) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(this).inflate(R.layout.dialog_combo, null)
        val progressBar: ProgressBar = v.findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE
        val textViewTitulo: TextView = v.findViewById(R.id.textViewTitulo)
        val editTextSearch: EditText = v.findViewById(R.id.editTextSearch)
        val recyclerView: RecyclerView = v.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(v.context)
        textViewTitulo.text = String.format("%s", title)

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

        when (tipo) {
            1 -> {
                val areaAdapter = AreaAdapter(object : OnItemClickListener.AreaListener {
                    override fun onItemClick(a: Area, view: View, position: Int) {
                        editTextArea.setText(a.descripcion)
                        q.areaId = a.areaId
                        u.servicio = a.descripcion
                        dialog.dismiss()
                    }
                })
                recyclerView.adapter = areaAdapter
                usuarioViewModel.getAreas().observe(this, Observer { a ->
                    if (a != null) {
                        areaAdapter.addItems(a)
                    }
                })
                editTextSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun afterTextChanged(editable: Editable) {
                        areaAdapter.getFilter().filter(editTextSearch.text.toString())
                    }
                })
            }
            2 -> {
                val centroAdapter =
                    CentroAdapter(object : OnItemClickListener.CentroCostosListener {
                        override fun onItemClick(c: CentroCostos, view: View, position: Int) {
                            editTextCentro.setText(c.descripcion)
                            q.centroCostoId = c.orden
                            q.sucursalId = c.sucursalId
                            u.sucursalId = c.sucursalId
                            u.sucursal = c.nombreSucursal
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = centroAdapter
                usuarioViewModel.getCentroCostos().observe(this, Observer { c ->
                    if (c != null) {
                        centroAdapter.addItems(c)
                    }
                })

                editTextSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun afterTextChanged(editable: Editable) {
                        centroAdapter.getFilter().filter(editTextSearch.text.toString())
                    }
                })
            }
            3 -> {
                val sucursalAdapter =
                    SucursalAdapter(object : OnItemClickListener.SucursalListener {
                        override fun onItemClick(s: Sucursal, v: View, position: Int) {
                            editTextSucursal.setText(s.nombre)
                            q.sucursalId = s.codigo
                            u.sucursalId = s.codigo
                            u.sucursal = s.nombre
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = sucursalAdapter
                usuarioViewModel.getSucursal().observe(this, Observer {
                    sucursalAdapter.addItems(it)
                })

                editTextSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun afterTextChanged(editable: Editable) {
                        sucursalAdapter.getFilter().filter(editTextSearch.text.toString())
                    }
                })
            }
        }
    }

    private fun load() {
        builder = AlertDialog.Builder(ContextThemeWrapper(this@FiltroActivity, R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(this@FiltroActivity).inflate(R.layout.dialog_login, null)
        builder.setView(view)
        val textViewTitle: TextView = view.findViewById(R.id.textView)
        textViewTitle.text = String.format("%s", "Sincronizando")
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
}
