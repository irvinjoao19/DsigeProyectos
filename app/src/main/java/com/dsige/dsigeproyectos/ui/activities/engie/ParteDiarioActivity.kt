package com.dsige.dsigeproyectos.ui.activities.engie

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.Estado
import com.dsige.dsigeproyectos.data.local.model.Query
import com.dsige.dsigeproyectos.data.local.model.engie.ParteDiario
import com.dsige.dsigeproyectos.data.viewModel.ParteDiarioViewModel
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.adapters.EstadoAdapter
import com.dsige.dsigeproyectos.ui.adapters.ParteDiarioAdapter
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_parte_diario.*
import javax.inject.Inject

class ParteDiarioActivity : DaggerAppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.editTextFecha -> Util.getDateDialog(this, editTextFecha)
            R.id.editTextEstado -> spinnerEstado()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var parteDiarioViewModel: ParteDiarioViewModel
    lateinit var parteDiarioAdapter: ParteDiarioAdapter

    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null
    private var parteDiarioId = 0
    private var activeOrClose: Int = 0
    private var topMenu: Menu? = null
    lateinit var f: Query

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {
                when (activeOrClose) {
                    0 -> startActivity(
                        Intent(this, ParteDiarioFormActivity::class.java)
                            .putExtra("parteDiarioId", parteDiarioId)
                    )
                    1 -> {
                        f.search = editTextSearch.text.toString()
                        val json = Gson().toJson(f)
                        parteDiarioViewModel.search.value = json
                    }
                }
            }
            R.id.filter -> {
                f = Query()
                editTextFecha.text = null
                editTextEstado.text = null
                editTextSearch.text = null
                val menu = topMenu!!.findItem(R.id.filter)
                val menu2 = topMenu!!.findItem(R.id.add)
                when (activeOrClose) {
                    0 -> {
                        layoutFilter.visibility = View.VISIBLE
                        menu.icon = ContextCompat.getDrawable(this, R.drawable.ic_close)
                        menu2.icon = ContextCompat.getDrawable(this, R.drawable.ic_done)
                        activeOrClose = 1
                    }
                    1 -> {
                        layoutFilter.visibility = View.GONE
                        menu.icon = ContextCompat.getDrawable(this, R.drawable.ic_search_white)
                        menu2.icon = ContextCompat.getDrawable(this, R.drawable.ic_add)
                        parteDiarioViewModel.search.value = ""
                        activeOrClose = 0
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        topMenu = menu
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parte_diario)
        bindUI()
    }

    private fun bindUI() {
        parteDiarioViewModel =
            ViewModelProvider(this, viewModelFactory).get(ParteDiarioViewModel::class.java)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Parte Diaro"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val layoutManager = LinearLayoutManager(this)
        parteDiarioAdapter = ParteDiarioAdapter(object : OnItemClickListener.ParteDiarioListener {
            override fun onItemClick(p: ParteDiario, position: Int, v: View) {
                showPopupMenu(p, v, this@ParteDiarioActivity)
            }
        })
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = parteDiarioAdapter

        parteDiarioViewModel.getParteDiarios().observe(this, Observer {
            parteDiarioAdapter.addItems(it)
        })
        parteDiarioViewModel.search.value = null

        parteDiarioViewModel.getMaxIdParteDiario().observe(this, Observer { p ->
            parteDiarioId = if (p != null) {
                p.parteDiarioId + 1
            } else
                1
        })

        parteDiarioViewModel.error.observe(this, Observer {
            closeLoad()
            Util.toastMensaje(this, it)
        })

        parteDiarioViewModel.success.observe(this, Observer {
            closeLoad()
            Util.toastMensaje(this, it)
        })

        editTextEstado.setOnClickListener(this)
        editTextFecha.setOnClickListener(this)
        editTextFecha.setText(Util.getFecha())
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

    private fun showPopupMenu(p: ParteDiario, v: View, context: Context) {
        val popup = PopupMenu(context, v)
        popup.menu.add(0, R.id.go, 0, getText(R.string.ingresar))
        if (p.estadoCodigo != "121") {
            popup.menu.add(1, R.id.send, 1, getText(R.string.enviar))
        }
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.go -> {
                    startActivity(
                        Intent(context, ParteDiarioFormActivity::class.java).putExtra(
                            "parteDiarioId",
                            p.parteDiarioId
                        )
                    )
                    true
                }
                R.id.send -> {
                    confirmSend(p, context)
                    true
                }
                else -> false
            }
        }
        popup.show()
        //val inflater = popup.menuInflater
        //inflater.inflate(R.menu.menu_parte_diario, popup.menu)
        //val menuBuilder = popup.menu as MenuBuilder
        //menuBuilder.setOptionalIconsVisible(true)
    }

    private fun confirmSend(p: ParteDiario, context: Context) {
        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle("Mensaje")
            .setMessage(String.format("Deseas enviar Nro Parte Diario : %s ?.", p.parteDiarioId))
            .setPositiveButton("Aceptar") { dialog, _ ->
                load()
                parteDiarioViewModel.sendParteDiario(p.parteDiarioId, this)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
        dialog.show()
    }

    private fun spinnerEstado() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(this).inflate(R.layout.dialog_combo, null)
        val progressBar: ProgressBar = v.findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE
        val textViewTitulo: TextView = v.findViewById(R.id.textViewTitulo)
        val editTextSearch: EditText = v.findViewById(R.id.editTextSearch)
        val recyclerView: RecyclerView = v.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(v.context)
        textViewTitulo.text = String.format("Estados")

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

        val estadoAdapter = EstadoAdapter(object : OnItemClickListener.EstadoListener {
            override fun onItemClick(c: Estado, view: View, position: Int) {
                editTextEstado.setText(c.nombre)
                dialog.dismiss()
            }
        })
        recyclerView.adapter = estadoAdapter
        parteDiarioViewModel.getEstados().observe(this, Observer {
            estadoAdapter.addItems(it)
        })
    }
}
