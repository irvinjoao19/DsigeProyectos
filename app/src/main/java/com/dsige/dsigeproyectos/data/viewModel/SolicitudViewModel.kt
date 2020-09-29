package com.dsige.dsigeproyectos.data.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dsige.dsigeproyectos.data.local.model.*
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
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SolicitudViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) :
    ViewModel() {

    val mensajeError: MutableLiveData<String> = MutableLiveData()
    val mensajeSuccess: MutableLiveData<String> = MutableLiveData()

    fun setError(s: String) {
        mensajeError.value = s
    }

    val error: LiveData<String>
        get() = mensajeError

    val success: LiveData<String>
        get() = mensajeSuccess

    // TODO : SOLICITUD GENERAL

    fun getMaxIdSolicitud(): LiveData<Solicitud> {
        return roomRepository.getMaxIdSolicitud()
    }

    fun getSolicitudByFilter(filtro: Int): LiveData<List<Solicitud>> {
        return roomRepository.getSolicitudByFilter(filtro)
    }

    fun getSolicitudById(id: Int): LiveData<Solicitud> {
        return roomRepository.getSolicitudById(id)
    }

    fun validateSolicitud(s: Solicitud): Boolean {

        if (s.obraTd.isEmpty()) {
            mensajeError.value = "Ingrese Obra"
            return false
        }

        if (s.tipoMaterialSol == 2) {
            if (s.nombrePersonal.isEmpty()) {
                mensajeError.value = "Ingrese Personal"
                return false
            }
        }

        if (s.nombreCoordinador.isEmpty()) {
            mensajeError.value = "Ingrese Coordinador"
            return false
        }

        if (s.tipoSolicitudId == 2) {
            if (s.nombreTipoMaterial.isEmpty()) {
                mensajeError.value = "Ingrese Tipo Devolucion"
                return false
            }
        }

        sendRegistroSolicitud(s)
        return true
    }

    private fun insertOrUpdateSolicitud(s: Solicitud, t: Mensaje) {
        roomRepository.insertOrUpdateSolicitud(s, t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    mensajeSuccess.value = "Solicitud Generado"
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }


    private fun sendRegistroSolicitud(s: Solicitud) {
        roomRepository.sendRegistroSolicitud(s)
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {

                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Mensaje) {
                    insertOrUpdateSolicitud(s, t)
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


    fun getObras(): LiveData<List<Obra>> {
        return roomRepository.getObras()
    }

    fun getAlmacenByTipo(tipoMaterialSolicitud: Int): LiveData<List<Almacen>> {
        return roomRepository.getAlmacenByTipo(tipoMaterialSolicitud)
    }

    fun getPersonal(): LiveData<List<Personal>> {
        return roomRepository.getPersonal()
    }

    fun getCoordinador(): LiveData<List<Coordinador>> {
        return roomRepository.getCoordinador()
    }

    fun getTipoDevolucion(): LiveData<List<TipoDevolucion>> {
        return roomRepository.getTipoDevolucion()
    }

    fun getEstados(): LiveData<List<Estado>> {
        return roomRepository.getEstados()
    }

    // TODO : SOLICITUD MATERIAL

    fun sendAprobation(q: Query, solicitud: Int) {
        roomRepository.sendAprobation(q)
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {
                override fun onComplete() {
                    updateSolicitudEstado(solicitud, q.estado)
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Mensaje) {

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

    private fun updateSolicitudEstado(solicitudId: Int, estado: String) {
        roomRepository.updateSolicitudEstado(solicitudId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    when (estado) {
                        "106" -> mensajeSuccess.value = "Solicitud Aprobada"
                        "110" -> mensajeSuccess.value = "Solicitud Anulada"
                    }
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    fun getRegistroMaterialByFK(id: Int): LiveData<List<RegistroSolicitudMaterial>> {
        return roomRepository.getRegistroMaterialByFK(id)
    }

    fun validateRegistroSolicitudMaterial(m: RegistroSolicitudMaterial): Boolean {
        if (m.solicitudId == 0) {
            mensajeError.value = "Llenar el primer formulario"
            return false
        }

        if (m.cantidadMovil == 0.0) {
            mensajeError.value = "Ingrese Cantidad"
            return false
        }

        exitRegistroSolicitudMaterial(m)
        return true
    }

    private fun exitRegistroSolicitudMaterial(m: RegistroSolicitudMaterial) {
        roomRepository.exitRegistroSolicitudMaterial(m)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Boolean> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Boolean) {
                    if (t) {
                        sendRegistroSolicitudMaterial(m)
                    } else {
                        mensajeSuccess.value = "Material existe"
                    }
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    private fun sendRegistroSolicitudMaterial(m: RegistroSolicitudMaterial) {
        roomRepository.sendRegistroSolicitudMaterial(m)
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Mensaje) {
                    if (t.mensaje.isEmpty()) {
                        insertOrUpdateRegistroSolicitudMaterial(m, t)
                    } else {
                        mensajeSuccess.value = t.mensaje
                    }
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

    private fun insertOrUpdateRegistroSolicitudMaterial(m: RegistroSolicitudMaterial, t: Mensaje) {
        roomRepository.insertOrUpdateRegistroSolicitudMaterial(m, t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    if (m.tipo != 2) {
                        if (m.registroMaterialId == 0) {
                            mensajeSuccess.value = "Material Guardado"
                        } else {
                            mensajeSuccess.value = "Material Actualizado"
                        }
                    } else {
                        mensajeSuccess.value = "Material Eliminado"
                    }
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    fun validateEditSolicitudMaterial(m: RegistroSolicitudMaterial): Boolean {
        if (m.cantidadMovil == 0.0) {
            mensajeError.value = "Ingrese Cantidad"
            return false
        }
        sendRegistroSolicitudMaterial(m)
        return true
    }

    fun validateDeleteSolicitudMaterial(m: RegistroSolicitudMaterial): Boolean {
        m.tipo = 2
        sendRegistroSolicitudMaterial(m)
        return true
    }


    // TODO : SOLICITUD PHOTO

    fun getSolicitudRegistroPhotosByFK(id: Int): LiveData<List<RegistroSolicitudPhoto>> {
        return roomRepository.getSolicitudRegistroPhotosByFK(id)
    }


    fun deleteRegistroSolicitudPhoto(p: RegistroSolicitudPhoto, context: Context) {
        roomRepository.deleteRegistroSolicitudPhoto(p)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    sendFoto(p, context)
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    fun sendFoto(r: RegistroSolicitudPhoto, context: Context) {
        val filePaths: ArrayList<String> = ArrayList()
        val json = Gson().toJson(r)
        Log.i("TAG", json)
        filePaths.add(File(Util.getFolder(context), r.nombre).toString())
        val b = MultipartBody.Builder()
        b.setType(MultipartBody.FORM)
        b.addFormDataPart("data", json)
        for (i in 0 until filePaths.size) {
            val file = File(filePaths[i])
            b.addFormDataPart(
                "files",
                file.name,
                RequestBody.create(MediaType.parse("multipart/form-data"), file)
            )
        }
        val body = b.build()
        val observableEnvio: Observable<Mensaje> = roomRepository.sendRegistroSolicitudPhoto(body)
        observableEnvio.subscribeOn(Schedulers.io())
            .delay(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Mensaje) {
                    insertOrUpdateSolicitudPhoto(r, t)
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    private fun insertOrUpdateSolicitudPhoto(p: RegistroSolicitudPhoto, t: Mensaje) {
        roomRepository.insertOrUpdateSolicitudPhoto(p, t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    if (p.registroPhotoId == 0) {
                        mensajeSuccess.value = "Foto Guardada"
                    } else {
                        mensajeSuccess.value = "Foto Eliminada"
                    }
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }


    // TODO PAGINATION

    val compositeDisposable = CompositeDisposable()
    val paginator = PublishProcessor.create<Int>()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val pageNumber: MutableLiveData<Int> = MutableLiveData()
    val solicitud: MutableLiveData<List<Solicitud>> = MutableLiveData()

    val load: LiveData<Boolean>
        get() = loading

    fun getPageNumber(number: Int) {
        pageNumber.value = number
    }

    fun getLoading(load: Boolean) {
        loading.value = load
    }

    private fun insertSolicitud(s: List<Solicitud>) {
        roomRepository.insertSolicitud(s)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    Log.i("TAG", "ACTUALIZADO")
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    fun getSolicitud(): LiveData<List<Solicitud>> {
        return solicitud
    }

    fun clearSolicitud() {
        solicitud.value = null
        loading.value = true
        mensajeError.value = null
        compositeDisposable.clear()
        pageNumber.value = 1
    }

    fun paginationSolicitud(q: Query) {
        val disposable = paginator
            .onBackpressureDrop()
            .delay(1000, TimeUnit.MILLISECONDS)
            .concatMap { page ->
                q.pageIndex = page
                q.pageSize = 20
                val json = Gson().toJson(q)
                Log.i("TAG", json)
                val body =
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
                roomRepository.paginationSolicitud(body)
                    .delay(600, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { s -> s }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ s ->
                solicitud.postValue(s)
                insertSolicitud(s)
                loading.postValue(false)
            }, { t ->
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
                pageNumber.postValue(1)
            })

        compositeDisposable.add(disposable)
        pageNumber.observeForever { n ->
            paginator.onNext(n)
        }
    }

    fun clear() {
        compositeDisposable.clear()
    }

    fun next(page: Int) {
        paginator.onNext(page)
    }
}