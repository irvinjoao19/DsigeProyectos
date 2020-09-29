package com.dsige.dsigeproyectos.ui.activities.engie

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.Estado
import com.dsige.dsigeproyectos.data.local.model.Query
import com.dsige.dsigeproyectos.data.local.model.engie.Solicitud
import com.dsige.dsigeproyectos.data.viewModel.SolicitudViewModel
import com.dsige.dsigeproyectos.data.viewModel.UsuarioViewModel
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.adapters.EstadoAdapter
import com.dsige.dsigeproyectos.ui.adapters.SolicitudObrAdapter
import com.dsige.dsigeproyectos.ui.adapters.SolicitudPersonalAdapter
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import com.google.android.material.textfield.TextInputEditText
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_requerimiento_solicitud.*
import java.util.*
import javax.inject.Inject

class RequerimientoSolicitudActivity : DaggerAppCompatActivity(), View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener, TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        q.search = s.toString()
        solicitudViewModel.clear()
        solicitudViewModel.paginationSolicitud(q)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun onRefresh() {
        obraAdapter?.addItems(listOfNotNull())
        personalAdapter?.addItems(listOfNotNull())
        solicitudViewModel.clear()
        solicitudViewModel.paginationSolicitud(q)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabRequest -> startActivity(
                Intent(this, SolicitudMaterialFormActivity::class.java)
                    .putExtra("solicitudId", solicitudId)
                    .putExtra("tipoMaterialSolicitud", tipoMaterialSolicitud)
                    .putExtra("tipoSolicitudId", tipoSolicitudId)
                    .putExtra("title", title)
                    .putExtra("subTitle", subTitle)
            )
            R.id.editTextDate -> getDateDialog(this, editTextDate)
            R.id.editTextEstado -> spinnerDialog()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var solicitudViewModel: SolicitudViewModel
    lateinit var usuarioViewModel: UsuarioViewModel

    var solicitudId: Int = 0
    var tipoMaterialSolicitud: Int = 0
    var tipoSolicitudId: Int = 0
    var title: String = ""
    var subTitle: String = ""

    var loading = false
    var pageNumber = 1
    var lastVisibleItem: Int = 0
    var totalItemCount: Int = 0
    var visibleItemCount: Int = 0

    var obraAdapter: SolicitudObrAdapter? = null
    var personalAdapter: SolicitudPersonalAdapter? = null
    lateinit var q: Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requerimiento_solicitud)
        q = Query()
        val b = intent.extras
        if (b != null) {
            tipoMaterialSolicitud = b.getInt("tipoMaterialSolicitud")
            tipoSolicitudId = b.getInt("tipoSolicitudId")
            title = b.getString("title")!!
            subTitle = b.getString("subTitle")!!
            bindUI()
            load()
            menssage()
            success()
        }
    }

    private fun bindUI() {
        solicitudViewModel =
            ViewModelProvider(this, viewModelFactory).get(SolicitudViewModel::class.java)
        usuarioViewModel =
            ViewModelProvider(this, viewModelFactory).get(UsuarioViewModel::class.java)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = title
        supportActionBar!!.subtitle = subTitle

        if (tipoSolicitudId == 2) {
            supportActionBar!!.setBackgroundDrawable(
                ColorDrawable(
                    resources.getColor(R.color.colorAccent, theme)
                )
            )
            collapsing.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
        } else {
            supportActionBar!!.setBackgroundDrawable(
                ColorDrawable(
                    resources.getColor(R.color.colorPrimary, theme)
                )
            )
            collapsing.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        editTextDate.setText(Util.getFecha())
        editTextDate.setOnClickListener(this)
        editTextEstado.setOnClickListener(this)
        editTextEstado.setText(String.format("%s", "Registrado"))
        fabRequest.setOnClickListener(this)
        refreshLayout.setOnRefreshListener(this)
        editTextSearch.addTextChangedListener(this)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!loading) {
                    if (dy > 0) {
                        visibleItemCount = layoutManager.childCount
                        totalItemCount = layoutManager.itemCount
                        lastVisibleItem = layoutManager.findFirstVisibleItemPosition()

                        if ((visibleItemCount + lastVisibleItem) >= totalItemCount) {
                            solicitudViewModel.getLoading(true)
                            solicitudViewModel.getPageNumber(pageNumber++)
                            solicitudViewModel.next(pageNumber)
                        }
                    }
                }
            }
        })
        solicitudViewModel.getPageNumber(pageNumber)

        usuarioViewModel.user.observe(this, Observer { u ->
            q.usuarioId = u.usuarioId
            q.centroCostoId = u.centroId
            q.cuadrillaId = u.cuadrillaId
        })

        q.tipoMaterialSolicitud = tipoMaterialSolicitud
        q.tipoSolicitud = tipoSolicitudId
        q.fechaRegistro = Util.getFecha()
        q.estado = "105"
        solicitudViewModel.paginationSolicitud(q)

        when (tipoMaterialSolicitud) {
            1 -> {
                obraAdapter = SolicitudObrAdapter(object : OnItemClickListener.SolicitudListener {
                    override fun onItemClick(s: Solicitud, v: View, position: Int) {
                        goFormSolicitud(s)
                    }
                })

                solicitudViewModel.getSolicitud().observe(this, Observer { b ->
                    if (b != null) {
                        obraAdapter!!.addItems(b)
                    } else {
                        obraAdapter!!.addItems(listOfNotNull())
                    }
                    refreshLayout.isRefreshing = false
                })
                recyclerView.adapter = obraAdapter
            }
            2 -> {
                personalAdapter =
                    SolicitudPersonalAdapter(object : OnItemClickListener.SolicitudListener {
                        override fun onItemClick(s: Solicitud, v: View, position: Int) {
                            goFormSolicitud(s)
                        }
                    })
                solicitudViewModel.getSolicitud().observe(this, Observer { b ->
                    if (b != null) {
                        personalAdapter!!.addItems(b)
                    } else {
                        personalAdapter!!.addItems(listOfNotNull())
                    }
                    refreshLayout.isRefreshing = false
                })
                recyclerView.adapter = personalAdapter
            }
        }

        solicitudViewModel.getMaxIdSolicitud().observe(this, Observer { s ->
            solicitudId = if (s != null) {
                s.solicitudId + 1
            } else
                1
        })
    }

    private fun load() {
        solicitudViewModel.load.observe(this, Observer { b ->
            loading = b
            if (b) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun menssage() {
        solicitudViewModel.error.observe(this, Observer { s ->
            if (s != null) {
                Util.toastMensaje(this, s)
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun success() {
        solicitudViewModel.success.observe(this, Observer { s ->
            if (s != null) {
                Util.toastMensaje(this, s)
            }
        })
    }

    private fun goFormSolicitud(s: Solicitud) {
        startActivity(
            Intent(this, SolicitudMaterialFormActivity::class.java)
                .putExtra("solicitudId", s.solicitudId)
                .putExtra("tipoMaterialSolicitud", s.tipoMaterialSol)
                .putExtra("tipoSolicitudId", s.tipoSolicitudId)
                .putExtra("title", title)
                .putExtra("subTitle", subTitle)
        )
    }

    private fun spinnerDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(this).inflate(R.layout.dialog_combo, null)
        val progressBar: ProgressBar = v.findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE
        val textViewTitulo: TextView = v.findViewById(R.id.textViewTitulo)
        val editTextSearch: TextInputEditText = v.findViewById(R.id.editTextSearch)
        val recyclerView: RecyclerView = v.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(v.context)
        textViewTitulo.text = String.format("%s", "Estado")

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
            override fun onItemClick(e: Estado, view: View, position: Int) {
                q.estado = e.codigo
                editTextEstado.setText(e.nombre)
                solicitudViewModel.clearSolicitud()
                solicitudViewModel.paginationSolicitud(q)
                dialog.dismiss()
            }
        })
        recyclerView.adapter = estadoAdapter

        solicitudViewModel.getEstados().observe(this, Observer {
            estadoAdapter.addItems(it)
        })

        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                estadoAdapter.getFilter().filter(editTextSearch.text.toString())
            }
        })
    }

    private fun getDateDialog(context: Context, input: EditText) {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(context, { _, year, monthOfYear, dayOfMonth ->
            val month =
                if (((monthOfYear + 1) / 10) == 0) "0" + (monthOfYear + 1).toString() else (monthOfYear + 1).toString()
            val day = if (((dayOfMonth + 1) / 10) == 0) "0$dayOfMonth" else dayOfMonth.toString()
            val fecha = "$day/$month/$year"
            input.setText(fecha)
            q.fechaRegistro = fecha
            solicitudViewModel.clearSolicitud()
            solicitudViewModel.paginationSolicitud(q)
        }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }
}
