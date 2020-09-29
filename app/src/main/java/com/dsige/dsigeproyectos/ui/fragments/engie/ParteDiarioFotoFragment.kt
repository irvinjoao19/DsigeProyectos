package com.dsige.dsigeproyectos.ui.fragments.engie

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.RegistroPhoto
import com.dsige.dsigeproyectos.data.viewModel.ParteDiarioViewModel
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.helper.Permission
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.adapters.PhotoEngieAdapter
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_parte_diario_foto.*
import java.io.File
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"

class ParteDiarioFotoFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabTakePhoto -> if (obraTd.isNotEmpty()) {
                createImage()
            } else {
                Util.snackBarMensaje(v, "Llenar el primer formulario")
            }
        }
    }

    var parteDiarioId: Int = 0
    var obraTd: String = ""
    lateinit var p: RegistroPhoto

    lateinit var folder: File
    lateinit var image: File

    private lateinit var nameImg: String
    private lateinit var direction: String

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var parteDiarioViewModel: ParteDiarioViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        p = RegistroPhoto()
        arguments?.let {
            parteDiarioId = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_parte_diario_foto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parteDiarioViewModel = ViewModelProvider(this, viewModelFactory).get(
            ParteDiarioViewModel::class.java
        )
        fabTakePhoto.setOnClickListener(this)
        bindUI()
    }

    private fun bindUI() {
        val layoutManager = GridLayoutManager(context, 2)
        val photoAdapter = PhotoEngieAdapter(object : OnItemClickListener.RegistroPhotoListener {
            override fun onItemClick(p: RegistroPhoto, v: View, position: Int) {
                if (p.tipo != 1) {
                    showPopupMenu(p, v, context!!)
                }
            }
        })
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = photoAdapter

        parteDiarioViewModel.getRegistroPhotos(parteDiarioId)
            .observe(viewLifecycleOwner, Observer {
                    photoAdapter.addItems(it)
            })

        parteDiarioViewModel.getParteDiarioById(parteDiarioId)
            .observe(viewLifecycleOwner, Observer { d ->
                if (d != null) {
                    obraTd = d.obraTd
                    if (d.estadoCodigo == "121") {
                        fabTakePhoto.visibility = View.GONE
                    }
                }
            })

        parteDiarioViewModel.error.observe(viewLifecycleOwner, Observer { m ->
            Util.snackBarMensaje(view!!, m)
        })
        parteDiarioViewModel.success.observe(viewLifecycleOwner, Observer { m ->
            Util.toastMensaje(context!!, m)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(parteDiarioId: Int) =
            ParteDiarioFotoFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, parteDiarioId)
                }
            }
    }

    private fun showPopupMenu(r: RegistroPhoto, v: View, context: Context) {
        val popupMenu = PopupMenu(context, v)
        popupMenu.menu.add(0, Menu.FIRST, 0, getText(R.string.changePhoto))
        popupMenu.menu.add(1, Menu.FIRST + 1, 1, getText(R.string.deletePhoto))
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                1 -> {
                    p.registroPhotoId = r.registroPhotoId
                    createImage()
                }
                2 -> {
                    deletePhoto(r)
                }
            }
            false
        }
        popupMenu.show()
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

    private fun deletePhoto(p: RegistroPhoto) {
        val dialog = MaterialAlertDialogBuilder(context!!)
            .setTitle("Mensaje")
            .setMessage(String.format("Deseas eliminar a %s ?.", p.nombre))
            .setPositiveButton("Aceptar") { dialog, _ ->
                parteDiarioViewModel.deleteRegistroPhoto(p, context!!)
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
        p.parteDiarioId = parteDiarioId
        p.fecha = Util.getFecha()
        p.nombre = nameImg
        parteDiarioViewModel.insertOrUpdatePhoto(p)
    }
}