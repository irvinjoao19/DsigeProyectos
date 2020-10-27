package com.dsige.dsigeproyectos.data.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dsige.dsigeproyectos.data.local.model.*
import com.dsige.dsigeproyectos.data.local.model.engie.Area
import com.dsige.dsigeproyectos.data.local.model.engie.CentroCostos
import com.dsige.dsigeproyectos.data.local.model.engie.Sucursal
import com.dsige.dsigeproyectos.data.local.model.trinidad.Registro
import com.dsige.dsigeproyectos.data.local.model.trinidad.RegistroDetalle
import com.dsige.dsigeproyectos.data.local.model.trinidad.Vehiculo
import com.dsige.dsigeproyectos.data.local.model.trinidad.VehiculoVales
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

class UsuarioViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) :
    ViewModel() {

    private val mensajeError = MutableLiveData<String>()
    private val mensajeSuccess: MutableLiveData<String> = MutableLiveData()

    val success: LiveData<String>
        get() = mensajeSuccess

    val error: LiveData<String>
        get() = mensajeError

    val user: LiveData<Usuario>
        get() = roomRepository.getUsuario()

    fun setError(s: String) {
        mensajeError.value = s
    }

    fun getLogin(usuario: String, pass: String, imei: String, version: String) {
        roomRepository.clearUsuario()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    roomRepository.getUsuarioService(usuario, pass, imei, version)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<Usuario> {
                            override fun onSubscribe(d: Disposable) {

                            }

                            override fun onNext(usuario: Usuario) {
                                insertUsuario(usuario, version)
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

                            override fun onComplete() {
                            }
                        })
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    fun insertUsuario(u: Usuario, v: String) {
        roomRepository.insertUsuario(u)
            .delay(3, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onComplete() {
                    val f = u.filtro
                    val centro: List<CentroCostos> = f.centros
                    val area: List<Area> = f.areas
                    if (centro.size == 1 && area.size == 1) {
                        val c = centro[0]
                        val a = area[0]
                        val q = Query()
                        q.areaId = a.areaId
                        q.centroCostoId = c.orden
                        q.sucursalId = c.sucursalId
                        q.usuarioId = u.usuarioId
                        q.version = v
                        u.servicio = a.descripcion
                        u.sucursalId = c.sucursalId
                        u.sucursal = c.nombreSucursal
                        u.centroId = q.centroCostoId
                        u.areaId = q.areaId
                        u.estado = 1
                        updateUsuario(u, q)
                    } else {
                        sync(u.usuarioId, v)
                    }
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }


    private fun sync(id: String, v: String) {
        roomRepository.getSync(id, v)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Sync> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Sync) {
                    saveSync(t)
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    fun syncComplete(q: Query) {
        roomRepository.getSyncComplete(q)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Sync> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}

                override fun onNext(t: Sync) {
                    saveSync(t)
                }
                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    private fun saveSync(s: Sync) {
        roomRepository.saveSync(s)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onComplete() {
                    mensajeSuccess.value = "Logeado"
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    fun logout() {
        roomRepository.deleteUsuario()
            .delay(3, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onComplete() {
                    mensajeSuccess.value = "Close"
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    fun getSizeRegistro(): LiveData<Int> {
        return roomRepository.getSizeRegistro()
    }

    fun sendData(context: Context) {
        val auditorias: Observable<List<Registro>> = roomRepository.getDataRegistro(1)
        auditorias.flatMap { observable ->
            Observable.fromIterable(observable).flatMap { a ->
                val b = MultipartBody.Builder()
                if (a.foto.isNotEmpty()) {
                    val file = File(Util.getFolder(context), a.foto)
                    if (file.exists()) {
                        b.addFormDataPart(
                            "files", file.name,
                            RequestBody.create(MediaType.parse("multipart/form-data"), file)
                        )
                    }
                }
                val detalles: List<RegistroDetalle>? = a.list
                if (detalles != null) {
                    for (p: RegistroDetalle in detalles) {
                        if (p.foto1PuntoAntes.isNotEmpty()) {
                            val file = File(Util.getFolder(context), p.foto1PuntoAntes)
                            if (file.exists()) {
                                b.addFormDataPart(
                                    "files", file.name,
                                    RequestBody.create(
                                        MediaType.parse("multipart/form-data"),
                                        file
                                    )
                                )
                            }
                        }

                        if (p.foto2PuntoAntes.isNotEmpty()) {
                            val file = File(Util.getFolder(context), p.foto2PuntoAntes)
                            if (file.exists()) {
                                b.addFormDataPart(
                                    "files", file.name,
                                    RequestBody.create(
                                        MediaType.parse("multipart/form-data"),
                                        file
                                    )
                                )
                            }
                        }

                        if (p.foto3PuntoAntes.isNotEmpty()) {
                            val file = File(Util.getFolder(context), p.foto3PuntoAntes)
                            if (file.exists()) {
                                b.addFormDataPart(
                                    "files", file.name,
                                    RequestBody.create(
                                        MediaType.parse("multipart/form-data"),
                                        file
                                    )
                                )
                            }
                        }

                        if (p.foto1PuntoDespues.isNotEmpty()) {
                            val file = File(Util.getFolder(context), p.foto1PuntoDespues)
                            if (file.exists()) {
                                b.addFormDataPart(
                                    "files", file.name,
                                    RequestBody.create(
                                        MediaType.parse("multipart/form-data"),
                                        file
                                    )
                                )
                            }
                        }

                        if (p.foto2PuntoDespues.isNotEmpty()) {
                            val file = File(Util.getFolder(context), p.foto2PuntoDespues)
                            if (file.exists()) {
                                b.addFormDataPart(
                                    "files", file.name,
                                    RequestBody.create(
                                        MediaType.parse("multipart/form-data"),
                                        file
                                    )
                                )
                            }
                        }

                        if (p.foto3PuntoDespues.isNotEmpty()) {
                            val file = File(Util.getFolder(context), p.foto3PuntoDespues)
                            if (file.exists()) {
                                b.addFormDataPart(
                                    "files", file.name,
                                    RequestBody.create(
                                        MediaType.parse("multipart/form-data"),
                                        file
                                    )
                                )
                            }
                        }
                    }
                }

                val json = Gson().toJson(a)
                Log.i("TAG", json)
                b.setType(MultipartBody.FORM)
                b.addFormDataPart("data", json)

                val body = b.build()
                Observable.zip(
                    Observable.just(a), roomRepository.saveData(body),
                    BiFunction<Registro, Mensaje, Mensaje> { _, mensaje ->
                        mensaje
                    })
            }
        }.subscribeOn(Schedulers.io())
            .delay(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {

                override fun onSubscribe(d: Disposable) {
                    Log.i("TAG", d.toString())
                }

                override fun onNext(m: Mensaje) {
                    Log.i("TAG", "RECIBIENDO LOS DATOS")
                    updateRegistro(m)
                }

                override fun onError(e: Throwable) {
                    if (e is HttpException) {
                        val body = e.response().errorBody()
                        try {
                            val error = retrofit.errorConverter.convert(body!!)
                            mensajeError.postValue(error.Message)
                        } catch (e1: IOException) {
                            e1.printStackTrace()
                            Log.i("TAG", e1.toString())
                        }
                    } else {
                        mensajeError.postValue(e.message)
                    }
                }

                override fun onComplete() {
                }
            })
    }

    private fun updateRegistro(messages: Mensaje) {
        roomRepository.updateRegistro(messages)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    mensajeSuccess.value = messages.mensaje
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.i("TAG", e.toString())
                }
            })
    }

    fun sendDataVehiculo(context: Context) {
        val auditorias: Observable<List<Vehiculo>> = roomRepository.getDataVehiculo(1)
        auditorias.flatMap { observable ->
            Observable.fromIterable(observable).flatMap { a ->
                val b = MultipartBody.Builder()
                val vale: List<VehiculoVales>? = a.registros
                if (vale != null) {
                    for (v: VehiculoVales in vale) {
                        if (v.foto.isNotEmpty()) {
                            val file = File(Util.getFolder(context), v.foto)
                            if (file.exists()) {
                                b.addFormDataPart(
                                    "files", file.name,
                                    RequestBody.create(
                                        MediaType.parse("multipart/form-data"),
                                        file
                                    )
                                )
                            }
                        }
                    }
                }

                val json = Gson().toJson(a)
                Log.i("TAG", json)
                b.setType(MultipartBody.FORM)
                b.addFormDataPart("data", json)

                val body = b.build()
                Observable.zip(
                    Observable.just(a), roomRepository.saveVehiculo(body),
                    BiFunction<Vehiculo, Mensaje, Mensaje> { _, mensaje ->
                        mensaje
                    })
            }
        }.subscribeOn(Schedulers.io())
            .delay(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {

                override fun onSubscribe(d: Disposable) {
                    Log.i("TAG", d.toString())
                }

                override fun onNext(m: Mensaje) {
                    Log.i("TAG", "RECIBIENDO LOS DATOS")
                    updateVehiculo(m)
                }

                override fun onError(e: Throwable) {
                    if (e is HttpException) {
                        val body = e.response().errorBody()
                        try {
                            val error = retrofit.errorConverter.convert(body!!)
                            mensajeError.postValue(error.Message)
                        } catch (e1: IOException) {
                            e1.printStackTrace()
                            Log.i("TAG", e1.toString())
                        }
                    } else {
                        mensajeError.postValue(e.message)
                    }
                }

                override fun onComplete() {
                }
            })
    }

    private fun updateVehiculo(messages: Mensaje) {
        roomRepository.updateVehiculo(messages)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    mensajeSuccess.value = messages.mensaje
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.i("TAG", e.toString())
                }
            })
    }

    fun insertMigracion(m: Sync) {
        roomRepository.saveSync(m)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {

                }
            })
    }

    fun updateUsuario(u: Usuario, q: Query) {
        roomRepository.updateUsuario(u)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    syncComplete(q)
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    fun getAreas(): LiveData<List<Area>> {
        return roomRepository.getAreas()
    }

    fun getCentroCostos(): LiveData<List<CentroCostos>> {
        return roomRepository.getCentroCostos()
    }

    fun getSucursal(): LiveData<List<Sucursal>> {
        return roomRepository.getSucursal()
    }
}