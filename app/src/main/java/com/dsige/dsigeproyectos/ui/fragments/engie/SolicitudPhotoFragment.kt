package com.dsige.dsigeproyectos.ui.fragments.engie

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager

import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.RegistroSolicitudPhoto
import com.dsige.dsigeproyectos.data.viewModel.SolicitudViewModel
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.helper.Permission
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.adapters.PhotoSolicitudAdapter
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_solicitud_photo.*
import java.io.File
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

class SolicitudPhotoFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabTakePhoto -> createImage()
        }
    }

    var solicitudId: Int = 0
    var tipoMaterialSolicitud: Int = 0
    var tipoSolicitudId: Int = 0

    lateinit var p: RegistroSolicitudPhoto

    lateinit var folder: File
    lateinit var image: File

    private lateinit var nameImg: String
    private lateinit var direction: String
    lateinit var builder: AlertDialog.Builder
    var dialog: AlertDialog? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var solicitudViewModel: SolicitudViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        p = RegistroSolicitudPhoto()
        arguments?.let {
            solicitudId = it.getInt(ARG_PARAM1)
            tipoMaterialSolicitud = it.getInt(ARG_PARAM2)
            tipoSolicitudId = it.getInt(ARG_PARAM3)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_solicitud_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        solicitudViewModel =
            ViewModelProvider(this, viewModelFactory).get(SolicitudViewModel::class.java)
        fabTakePhoto.setOnClickListener(this)
        bindUI()
        menssage()
        success()
    }

    private fun bindUI() {
        val layoutManager = GridLayoutManager(context, 2)
        val photoAdapter =
            PhotoSolicitudAdapter(object : OnItemClickListener.SolicitudRegistroPhotoListener {
                override fun onItemClick(p: RegistroSolicitudPhoto, v: View, position: Int) {
                    deletePhoto(p, context!!)
                }
            })
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = photoAdapter

        solicitudViewModel.getSolicitudRegistroPhotosByFK(solicitudId)
            .observe(viewLifecycleOwner, Observer {
                photoAdapter.addItems(it)
            })

        solicitudViewModel.getSolicitudById(solicitudId).observe(viewLifecycleOwner, Observer { g ->
            if (g != null) {
                p.identity = g.identity
                if (g.pubEstadoCodigo != "105") {
                    fabTakePhoto.visibility = View.GONE
                }
            } else {
                fabTakePhoto.visibility = View.GONE
            }
        })

        if (tipoSolicitudId == 2) {
            fabTakePhoto.backgroundTintList =
                resources.getColorStateList(R.color.colorPrimary, null)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(solicitudId: Int, tipoMaterialSolicitud: Int, tipoSolicitudId: Int) =
            SolicitudPhotoFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, solicitudId)
                    putInt(ARG_PARAM2, tipoMaterialSolicitud)
                    putInt(ARG_PARAM3, tipoSolicitudId)
                }
            }
    }

    private fun createImage() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(context!!.packageManager) != null) {
            folder = Util.getFolder(context!!)
            nameImg = Util.getFechaActualForPhoto("1")
            image = File(folder, nameImg)
            direction = "$folder/$nameImg"
            val uriSavedImage = Uri.fromFile(image)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage)

            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                    m.invoke(null)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            startActivityForResult(takePictureIntent, Permission.CAMERA_REQUEST)
        }
    }

    private fun deletePhoto(p: RegistroSolicitudPhoto, context: Context) {
        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle("Mensaje")
            .setMessage(String.format("Deseas eliminar a %s ?.", p.nombre))
            .setPositiveButton("Aceptar") { dialog, _ ->
                load("Eliminando...")
                solicitudViewModel.sendFoto(p, context)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Permission.CAMERA_REQUEST && resultCode == DaggerAppCompatActivity.RESULT_OK) {
            generateImage()
        }
    }

    private fun generateImage() {
        val image: Observable<Boolean> = Util.generateImageAsync(direction)
        image.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : io.reactivex.Observer<Boolean> {
                override fun onComplete() {
                    Log.i("ERROR PHOTO", "EXITOSO")
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Boolean) {
                    if (t) {
                        formRegistroPhoto()
                    }
                }

                override fun onError(e: Throwable) {
                    Log.e("ERROR PHOTO", e.toString())
                    Util.toastMensaje(context!!, "Volver a intentarlo")
                }
            })
    }

    private fun formRegistroPhoto() {
        p.solicitudId = solicitudId
        p.fecha = Util.getFecha()
        p.nombre = nameImg
        p.tipo = 0
        load("Enviando")
        solicitudViewModel.sendFoto(p, context!!)
    }

    private fun menssage() {
        solicitudViewModel.error.observe(viewLifecycleOwner, Observer { m ->
            closeLoad()
            Util.snackBarMensaje(view!!, m)
        })
    }

    private fun success() {
        solicitudViewModel.success.observe(viewLifecycleOwner, Observer { m ->
            closeLoad()
            Util.toastMensaje(context!!, m)
        })
    }

    private fun closeLoad() {
        if (dialog != null) {
            if (dialog!!.isShowing) {
                dialog!!.dismiss()
            }
        }
    }

    private fun load(title: String) {
        builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(context).inflate(R.layout.dialog_login, null)
        builder.setView(view)
        val textViewTitle: TextView = view.findViewById(R.id.textView)
        textViewTitle.text = title
        dialog = builder.create()
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setCancelable(false)
        dialog!!.show()
    }
}