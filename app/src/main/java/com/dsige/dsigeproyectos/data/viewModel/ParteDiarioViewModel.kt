package com.dsige.dsigeproyectos.data.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.dsige.dsigeproyectos.data.local.model.Estado
import com.dsige.dsigeproyectos.data.local.model.Query
import com.dsige.dsigeproyectos.data.local.model.engie.*
import com.dsige.dsigeproyectos.data.local.repository.ApiError
import com.dsige.dsigeproyectos.data.local.repository.AppRepository
import com.dsige.dsigeproyectos.helper.Mensaje
import com.dsige.dsigeproyectos.helper.Util
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ParteDiarioViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) :
    ViewModel() {

    val mensajeError: MutableLiveData<String> = MutableLiveData()
    val mensajeSuccess: MutableLiveData<String> = MutableLiveData()
    val search: MutableLiveData<String> = MutableLiveData()

    fun setError(s: String) {
        mensajeError.value = s
    }

    val error: LiveData<String>
        get() = mensajeError

    val success: LiveData<String>
        get() = mensajeSuccess

    // TODO : General

    fun getMaxIdParteDiario(): LiveData<ParteDiario> {
        return roomRepository.getMaxIdParteDiario()
    }


    fun getParteDiarios(): LiveData<PagedList<ParteDiario>> {
        return Transformations.switchMap(search) { input ->
            if (input == null || input.isEmpty()) {
                roomRepository.getParteDiarios()
            } else {
                val f = Gson().fromJson(input, Query::class.java)
                if (f.estado.isEmpty()) {
                    if (f.search.isEmpty()) {
                        roomRepository.getParteDiariosFecha(f.fechaInicial)
                    } else {
                        roomRepository.getParteDiariosSearch(
                            f.fechaInicial, String.format("%s%s%s", "%", f.search, "%")
                        )
                    }
                } else {
                    if (f.search.isEmpty()) {
                        roomRepository.getParteDiarioEstado(f.fechaInicial, f.estado)
                    } else {
                        roomRepository.getParteDiariosComplete(
                            f.fechaInicial, f.estado, String.format("%s%s%s", "%", f.search, "%")
                        )
                    }
                }
            }
        }
    }


    fun getParteDiarioPendiente(): LiveData<List<ParteDiario>> {
        return roomRepository.getParteDiarioPendiente("120")
    }

    fun getParteDiarioById(id: Int): LiveData<ParteDiario> {
        return roomRepository.getParteDiarioById(id)
    }

    fun getObras(): LiveData<List<Obra>> {
        return roomRepository.getObras()
    }

    fun getCoordinador(): LiveData<List<Coordinador>> {
        return roomRepository.getCoordinador()
    }

    fun getAlmacenEdelnor(): LiveData<List<Almacen>> {
        return roomRepository.getAlmacenEdelnor()
    }

    fun getMedidor(): LiveData<List<Medidor>> {
        return roomRepository.getMedidor()
    }

    fun validateParteDiario(p: ParteDiario): Boolean {
        val json = Gson().toJson(p)
        Log.i("TAG", json)

        if (p.obraTd.isEmpty()) {
            mensajeError.value = "Seleccionar Obra Td"
            return false
        }

        if (p.coordinadorDni.isEmpty()) {
            mensajeError.value = "Seleccionar Coordinador"
            return false
        }

        if (p.suministro.isEmpty()) {
            mensajeError.value = "Escribir un suministro"
            return false
        }

        if (p.sed.isEmpty()) {
            mensajeError.value = "Escribir nro ficha"
            return false
        }

        insertOrUpdateParteDiario(p)
        return true
    }

    private fun insertOrUpdateParteDiario(p: ParteDiario) {
        roomRepository.insertOrUpdateParteDiario(p)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    if (p.parteDiarioId == 0) {
                        mensajeSuccess.value = "Parte Diario Guardado"
                    } else {
                        mensajeSuccess.value = "Parte Diario Actualizado"
                    }
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }

            })
    }

    // TODO : MATERIAL

    fun getRegistroMaterialTaskByFK(id: Int): LiveData<List<RegistroMaterial>> {
        return roomRepository.getRegistroMaterialTaskByFK(id)
    }


    fun getMateriales(o: String, a: String): LiveData<List<Material>> {
        return roomRepository.getMateriales(o, a)
    }

    fun getArticuloByTipo(t: Int): LiveData<List<Articulo>> {
        return roomRepository.getArticuloByTipo(t)
    }

    fun validateRegistroMaterial(m: RegistroMaterial, p: ParteDiario): Boolean {
        if (p.obraTd.isEmpty()) {
            mensajeError.value = "Completar Primer Formulario"
            return false
        }
        if (m.cantidadMovil == 0.0) {
            mensajeError.value = "Ingrese Cantidad"
            return false
        }

        if (m.stock < m.cantidadMovil) {
            mensajeError.value = "Cantidad no debe ser mayor a stock"
            return false
        }

        if (m.exigeSerie == "SI") {
            if (m.nroSerie.isEmpty()) {
                mensajeError.value = "Seleccione o Escriba Medidor"
                return false
            }
        }

        insertRegistroMaterial(m)
        return true
    }

    fun validateUpdateRegistroMaterial(m: RegistroMaterial): Boolean {
        if (m.cantidadMovil == 0.0) {
            mensajeSuccess.value = "Ingrese un valor"
            return false
        }
        updateRegistroMaterial(m)
        return true
    }

    private fun insertRegistroMaterial(m: RegistroMaterial) {
        roomRepository.insertRegistroMaterial(m)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Boolean> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Boolean) {
                    if (t) {
                        mensajeSuccess.value = "Guardado"
                    } else {
                        mensajeSuccess.value = "Registro existe"
                    }
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    private fun updateRegistroMaterial(m: RegistroMaterial) {
        roomRepository.updateRegistroMaterial(m)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Boolean> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Boolean) {
                    if (t) {
                        mensajeSuccess.value = "Registro Actualizado"
                    } else {
                        mensajeError.value = "La cantidad no debe ser mayor al stock Actual"
                    }
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    fun deleteRegistroMaterial(m: RegistroMaterial) {
        roomRepository.deleteRegistroMaterial(m)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    mensajeSuccess.value = "Eliminado"
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }

            })
    }

    // TODO : BAREMOS

    fun validateRegistroBaremo(r: RegistroBaremo): Boolean {
        if (r.cantidadMovil == 0.0) {
            mensajeSuccess.value = "Ingrese Cantidad"
            return false
        }
        insertOrUpdateRegistroBaremo(r)
        return true
    }

    fun validateUpdateRegistroBaremo(r: RegistroBaremo): Boolean {
        if (r.cantidadMovil == 0.0) {
            mensajeSuccess.value = "Ingrese un valor"
            return false
        }
        insertOrUpdateRegistroBaremo(r)
        return true
    }


    private fun insertOrUpdateRegistroBaremo(b: RegistroBaremo) {
        roomRepository.insertOrUpdateRegistroBaremo(b)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Boolean> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Boolean) {
                    if (t) {
                        if (b.registroBaremoId == 0) {
                            mensajeSuccess.value = "Guardado"
                        } else {
                            mensajeSuccess.value = "Actualizado"
                        }
                    } else {
                        mensajeSuccess.value = "Registro existe"
                    }
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    fun deleteRegistroBaremo(b: RegistroBaremo) {
        roomRepository.deleteRegistroBaremo(b)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    mensajeSuccess.value = "Eliminado"
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }


    fun getRegistroBaremos(id: Int): LiveData<List<RegistroBaremo>> {
        return roomRepository.getRegistroBaremos(id)
    }

    fun getBaremosById(id: Int): LiveData<List<Baremo>> {
        return roomRepository.getBaremosById(id)
    }

    fun getActividad(): LiveData<List<Actividad>> {
        return roomRepository.getActividad()
    }

    // TODO : PHOTOS

    fun getRegistroPhotos(id: Int): LiveData<List<RegistroPhoto>> {
        return roomRepository.getRegistroPhotos(id)
    }

    fun insertOrUpdatePhoto(p: RegistroPhoto) {
        roomRepository.insertOrUpdatePhoto(p)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    if (p.registroPhotoId == 0) {
                        mensajeSuccess.value = "Foto Guardada"
                    } else {
                        mensajeSuccess.value = "Foto Editada"
                    }
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }

            })
    }

    fun deleteRegistroPhoto(p: RegistroPhoto, context: Context) {
        roomRepository.deleteRegistroPhoto(p)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    Util.deleteImage(p.nombre, context)
                    mensajeSuccess.value = "Eliminado"
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }

            })
    }

    fun sendParteDiario(id: Int, context: Context) {
        val observableParteDiario: Observable<ParteDiario> = roomRepository.getParteDiario(id)
        observableParteDiario.flatMap { p ->
            val b = MultipartBody.Builder()
            val filePaths: ArrayList<String> = ArrayList()
            val photos: List<RegistroPhoto>? = p.photos
            if (p.firmaMovil.isNotEmpty()) {
                val file =
                    File(Util.getFolder(context), p.firmaMovil)
                if (file.exists()) {
                    filePaths.add(file.toString())
                }
            }

            if (photos != null) {
                for (f: RegistroPhoto in photos) {
                    val file = File(Util.getFolder(context), f.nombre)
                    if (file.exists()) {
                        filePaths.add(file.toString())
                    }
                }
            }

            for (i in 0 until filePaths.size) {
                val file = File(filePaths[i])
                b.addFormDataPart(
                    "files",
                    file.name,
                    RequestBody.create(MediaType.parse("multipart/form-data"), file)
                )
            }

            val json = Gson().toJson(p)
            Log.i("TAG", json)
            b.setType(MultipartBody.FORM)
            b.addFormDataPart("data", json)

            val requestBody = b.build()
            Observable.zip(
                Observable.just(p), roomRepository.saveParteDiario(requestBody),
                BiFunction<ParteDiario, Mensaje, Mensaje> { _, mensaje ->
                    mensaje
                })
        }.subscribeOn(Schedulers.io())
            .delay(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {
                override fun onComplete() {
                    mensajeSuccess.value = "Enviado"
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Mensaje) {
                    updateParteDiario(t)
                }

                override fun onError(t: Throwable) {
                    if (t is HttpException) {
                        val body = t.response().errorBody()
                        try {
                            val error = retrofit.errorConverter.convert(body!!)
                            mensajeError.postValue(error.Message)
                        } catch (e1: IOException) {
                            e1.printStackTrace()
                        }
                    } else {
                        mensajeError.postValue(t.message)
                    }
                }
            })

    }

    private fun updateParteDiario(t: Mensaje) {
        roomRepository.updateParteDiario(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    Log.i("TAG", "UPDATE")
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    fun getEstados(): LiveData<List<Estado>> {
        return roomRepository.getEstados()
    }
}