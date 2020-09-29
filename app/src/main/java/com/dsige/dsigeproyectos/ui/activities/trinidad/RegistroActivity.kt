package com.dsige.dsigeproyectos.ui.activities.trinidad

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.*
import com.dsige.dsigeproyectos.data.local.model.trinidad.Registro
import com.dsige.dsigeproyectos.data.local.model.trinidad.RegistroDetalle
import com.dsige.dsigeproyectos.data.viewModel.*
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.helper.Gps
import com.dsige.dsigeproyectos.helper.Permission
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.activities.CameraActivity
import com.dsige.dsigeproyectos.ui.activities.PreviewCameraActivity
import com.dsige.dsigeproyectos.ui.adapters.PhotoAdapter
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_registro.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class RegistroActivity : DaggerAppCompatActivity(), View.OnClickListener, TextWatcher {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabCamara -> formRegistro("1")
            R.id.fabGaleria -> formRegistro("2")
            R.id.fabSave -> registroViewModel.closeRegistro(
                registroId,
                detalleId,
                tipoDetalle,
                r.tipo
            )
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var registroViewModel: RegistroViewModel

    lateinit var r: Registro
    private var detalleId: Int = 0
    private var registroId: Int = 0
    private var tipoDetalle: Int = 0

    private var count: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        val b = intent.extras
        if (b != null) {
            r = Registro()
            r.tipo = b.getInt("tipo")
            r.usuarioId = b.getString("usuarioId")!!
            registroId = b.getInt("id")
            detalleId = b.getInt("detalleId")
            tipoDetalle = b.getInt("tipoDetalle")
            bindUI(b.getInt("tipo"), b.getInt("id"), b.getInt("detalleId"), b.getInt("tipoDetalle"))
        }
    }

    private fun bindUI(tipo: Int, id: Int, detalleId: Int, tipoDetalle: Int) {
        registroViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegistroViewModel::class.java)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Registro"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        fabCamara.setOnClickListener(this)
        fabGaleria.setOnClickListener(this)
        fabSave.setOnClickListener(this)

        if (tipo == 2) {
            layoutReparacion.visibility = View.GONE
        } else {
            editTextAncho.addTextChangedListener(this)
            editTextLargo.addTextChangedListener(this)
        }

        if (tipoDetalle != 0) {
            checkbox.isChecked = tipoDetalle != 1
            checkbox.isEnabled = false

            editTextObra.isEnabled = false
            editTextPoste.isEnabled = false
            editTextAncho.isEnabled = false
            editTextLargo.isEnabled = false
            editTextNombrePunto.isEnabled = false
            editTextM3.isEnabled = false
        }

        val photoAdapter = PhotoAdapter(object : OnItemClickListener.MenuListener {
            override fun onItemClick(m: MenuPrincipal, view: View, position: Int) {
                val popupMenu = PopupMenu(this@RegistroActivity, view)
                popupMenu.menu.add(1, 1, 1, getText(R.string.delete))
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        1 -> deleteConfirmation(m)
                    }
                    false
                }
                popupMenu.show()
            }
        })

        recyclerView.itemAnimator = DefaultItemAnimator()
        val layoutManager = StaggeredGridLayoutManager(2, 1)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = photoAdapter

        registroViewModel.getRegistroById(id).observe(this, Observer { re ->
            if (re != null) {
                r = re
                editTextObra.setText(re.nroObra)
                editTextPoste.setText(re.nroPoste)

                if (tipo == 1) {
                    getDetalle(photoAdapter, detalleId, tipo)
                } else {
                    getDetalle(photoAdapter, r.registroId, tipo)
                }
            }
        })

        registroViewModel.mensajeSuccess.observe(this, Observer { s ->
            if (s != null) {
                if (s == "Cerrado") {
                    finish()
                    return@Observer
                }

                if (tipo == 1) {
                    if (count == 2) {
                        Util.toastMensaje(this, "Maximo 2 fotos")
                        return@Observer
                    }
                } else {
                    if (count == 3) {
                        Util.toastMensaje(this, "Maximo 3 fotos")
                        return@Observer
                    }
                }

                if (s == "1") {
                    goCamera()
                    return@Observer
                }

                if (s == "2") {
                    goGalery()
                    return@Observer
                }

                startActivity(
                    Intent(this, PreviewCameraActivity::class.java)
                        .putExtra("tipo", r.tipo)
                        .putExtra("nameImg", s)
                        .putExtra("usuarioId", r.usuarioId)
                        .putExtra("id", r.usuarioId)
                        .putExtra("detalleId", detalleId)
                        .putExtra("tipoDetalle",  if (tipoDetalle == 0) 1 else tipoDetalle)
                        .putExtra("galery", true)

                )
                finish()
            }
        })

        registroViewModel.mensajeError.observe(this, Observer { s ->
            if (s != null) {
                Util.toastMensaje(this, s)
            }
        })
    }

    private fun getDetalle(photoAdapter: PhotoAdapter, id: Int, tipo: Int) {
        registroViewModel.getRegistroDetalle(tipo, id)
            .observe(this, Observer { d ->
                if (d != null) {
                    editTextNombrePunto.setText(d.nombrePunto)
                    editTextLargo.setText(d.largo.toString())
                    editTextAncho.setText(d.ancho.toString())
                    editTextM3.setText(d.totalM3.toString())
                    editTextObservacion.setText(d.observacion)
                    count = 0
                    if (tipoDetalle == 1) {
                        val a = ArrayList<MenuPrincipal>()
                        if (d.foto1PuntoAntes.isNotEmpty()) {
                            a.add(MenuPrincipal(d.detalleId, d.foto1PuntoAntes, 1))
                            count++
                        }
                        if (d.foto2PuntoAntes.isNotEmpty()) {
                            a.add(MenuPrincipal(d.detalleId, d.foto2PuntoAntes, 2))
                            count++
                        }
                        if (d.foto3PuntoAntes.isNotEmpty()) {
                            a.add(MenuPrincipal(d.detalleId, d.foto3PuntoAntes, 3))
                            count++
                        }

                        if (tipo == 1) {
                            if (count == 2) {
                                fabSave.visibility = View.VISIBLE
                            } else
                                fabSave.visibility = View.GONE
                        } else {
                            if (count == 3) {
                                fabSave.visibility = View.VISIBLE
                            } else fabSave.visibility = View.GONE
                        }
                        photoAdapter.addItems(a)
                    } else {
                        val a = ArrayList<MenuPrincipal>()
                        if (d.foto1PuntoDespues.isNotEmpty()) {
                            a.add(MenuPrincipal(d.detalleId, d.foto1PuntoDespues, 4))
                            count++
                        }
                        if (d.foto2PuntoDespues.isNotEmpty()) {
                            a.add(MenuPrincipal(d.detalleId, d.foto2PuntoDespues, 5))
                            count++
                        }
                        if (d.foto3PuntoDespues.isNotEmpty()) {
                            a.add(MenuPrincipal(d.detalleId, d.foto3PuntoDespues, 6))
                            count++
                        }
                        if (count == 2) {
                            fabSave.visibility = View.VISIBLE
                        } else
                            fabSave.visibility = View.GONE
                        photoAdapter.addItems(a)
                    }
                }
            })
    }

    private fun formRegistro(tipo: String) {
        val gps = Gps(this)
        if (gps.isLocationEnabled()) {
            if (gps.latitude.toString() != "0.0" || gps.longitude.toString() != "0.0") {
                r.nroObra = editTextObra.text.toString().toUpperCase(Locale.getDefault())
                r.nroPoste = editTextPoste.text.toString()
                r.fecha = Util.getFecha()
                r.latitud = gps.latitude.toString()
                r.longitud = gps.longitude.toString()
                r.punto = editTextNombrePunto.text.toString().trim()
                r.estado = "065"
                r.active = 0

                val d =
                    RegistroDetalle()
                d.nombrePunto = editTextNombrePunto.text.toString().trim()
                d.tipo = if (checkbox.isChecked) 2 else 1
                d.registroId = registroId
                d.detalleId = detalleId
                d.observacion = editTextObservacion.text.toString().trim()

                when {
                    editTextAncho.text.toString().isEmpty() -> d.ancho = 0.0
                    else -> d.ancho = editTextAncho.text.toString().toDouble()
                }
                when {
                    editTextLargo.text.toString().isEmpty() -> d.largo = 0.0
                    else -> d.largo = editTextLargo.text.toString().toDouble()
                }
                when {
                    editTextM3.text.toString().isEmpty() -> d.totalM3 = 0.0
                    else -> d.totalM3 = editTextM3.text.toString().toDouble()
                }
                r.detalles = d
                registroViewModel.validateRegistro(r, detalleId, tipo)
            }
        } else {
            gps.showSettingsAlert(this)
        }
    }

    private fun deleteConfirmation(d: MenuPrincipal) {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Mensaje")
            .setMessage("Deseas Eliminar la foto ?")
            .setPositiveButton("SI") { dialog, _ ->
                registroViewModel.deleteGaleria(d, this)
                dialog.dismiss()
            }
            .setNegativeButton("NO") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
    }

    private fun goCamera() {
        startActivity(
            Intent(this, CameraActivity::class.java)
                .putExtra("tipo", r.tipo)
                .putExtra("usuarioId", r.usuarioId)
                .putExtra("id", r.usuarioId)
                .putExtra("detalleId", detalleId)
                .putExtra("tipoDetalle", if (tipoDetalle == 0) 1 else tipoDetalle)
        )
        finish()
    }

    private fun goGalery() {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(i, Permission.GALERY_REQUEST)
    }

    override fun afterTextChanged(p0: Editable?) {
        val a = when {
            editTextAncho.text.toString().isEmpty() -> 0.0
            else -> editTextAncho.text.toString().toDouble()
        }
        val b = when {
            editTextLargo.text.toString().isEmpty() -> 0.0
            else -> editTextLargo.text.toString().toDouble()
        }

        val result = a * b
        editTextM3.setText(String.format("%.2f", result))
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Permission.GALERY_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                registroViewModel.generarArchivo(
                    Util.getFechaActualForPhoto(r.usuarioId),
                    this,
                    data
                )
            }
        }
    }
}