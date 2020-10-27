package com.dsige.dsigeproyectos.data.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.dsige.dsigeproyectos.data.local.model.Query
import com.dsige.dsigeproyectos.data.local.model.logistica.*
import com.dsige.dsigeproyectos.data.local.repository.ApiError
import com.dsige.dsigeproyectos.data.local.repository.AppRepository
import com.dsige.dsigeproyectos.helper.Mensaje
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LogisticaViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) :
    ViewModel() {

    val mensajeError: MutableLiveData<String> = MutableLiveData()
    val mensajeSuccess: MutableLiveData<String> = MutableLiveData()
    val pedidoSearch: MutableLiveData<String> = MutableLiveData()
    val ordenSearch: MutableLiveData<String> = MutableLiveData()
    var loading: MutableLiveData<Boolean> = MutableLiveData()

    fun setError(s: String) {
        mensajeError.value = s
    }

    fun setLoading(b: Boolean) {
        loading.value = b
    }

    // pedido
    fun getSyncPedido(u: String) {
        roomRepository.getSyncPedido(u)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Pedido>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: List<Pedido>) {
                    insertPedido(t)
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
                    loading.value = false
                }

                override fun onComplete() {

                }
            })
    }

    private fun insertPedido(t: List<Pedido>) {
        roomRepository.insertPedido(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onComplete() {
                    loading.value = false
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                    loading.value = false
                }

            })
    }

    fun getPedidoGroup(): LiveData<List<Pedido>> {
        return Transformations.switchMap(pedidoSearch) { input ->
            if (input == null || input.isEmpty()) {
                roomRepository.getPedidoGroup()
            } else {
                val f = Gson().fromJson(input, Query::class.java)
                roomRepository.getPedidoGroup(f.estado)
            }
        }
    }

    fun getPedidoGroupOne(codigo: String): LiveData<Pedido> {
        return roomRepository.getPedidoGroupOne(codigo)
    }

    fun getPedidoByCodigo(codigo: String): LiveData<List<Pedido>> {
        return roomRepository.getPedidoByCodigo(codigo)
    }

    // orden

    fun getSyncOrden(u: String) {
        roomRepository.getSyncOrden(u)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Orden>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: List<Orden>) {
                    insertOrden(t)
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
                    loading.value = false
                }

                override fun onComplete() {

                }
            })
    }

    private fun insertOrden(t: List<Orden>) {
        roomRepository.insertOrden(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    loading.value = false
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                    loading.value = false
                }
            })
    }

    fun getOrdenGroup(): LiveData<List<Orden>> {
        return Transformations.switchMap(ordenSearch) { input ->
            if (input == null || input.isEmpty()) {
                roomRepository.getOrdenGroup()
            } else {
                val f = Gson().fromJson(input, Query::class.java)
                roomRepository.getOrdenGroup(f.estado)
            }
        }
    }

    fun getOrdenGroupOne(codigo: String): LiveData<Orden> {
        return roomRepository.getOrdenGroupOne(codigo)
    }

    fun getOrdenByCodigo(codigo: String): LiveData<List<Orden>> {
        return roomRepository.getOrdenByCodigo(codigo)
    }

    fun getOrdenDetalleByCodigo(articulo: String): LiveData<List<OrdenDetalle>> {
        return roomRepository.getOrdenDetalleByCodigo(articulo)
    }

    //Anulacion

    fun getSyncAnulacion(u: String, fi: String, ff: String) {
        roomRepository.getSyncAnulacion(u, fi, ff)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Anulacion>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: List<Anulacion>) {
                    insertAnulacion(t)
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
                    loading.value = false
                }

                override fun onComplete() {

                }
            })
    }

    private fun insertAnulacion(t: List<Anulacion>) {
        roomRepository.insertAnulacion(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    loading.value = false
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun getAnulacionGroup(): LiveData<List<Anulacion>> {
        return roomRepository.getAnulacionGroup()
    }

    fun getAnulacionGroupOne(codigo: String): LiveData<Anulacion> {
        return roomRepository.getAnulacionGroupOne(codigo)
    }

    fun getAnulacionByCodigo(codigo: String): LiveData<List<Anulacion>> {
        return roomRepository.getAnulacionByCodigo(codigo)
    }

    // TODO REQUERIMIENTO

    fun validateRequerimiento(r: Requerimiento) {

        insertRequerimiento(r)
    }

    private fun insertRequerimiento(r: Requerimiento) {
        roomRepository.insertRequerimiento(r)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    mensajeSuccess.value = "Generado"
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun getRequerimientoById(id: Int): LiveData<Requerimiento> {
        return roomRepository.getRequerimientoById(id)
    }

    fun validateRequerimientoDetalle(r: RequerimientoDetalle) {

        insertRequerimientoDetalle(r)
    }

    private fun insertRequerimientoDetalle(r: RequerimientoDetalle) {
        roomRepository.insertRequerimientoDetalle(r)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun getRequerimientoDetalleById(id: Int): LiveData<RequerimientoDetalle> {
        return roomRepository.getRequerimientoDetalleById(id)
    }

    fun getRequerimientoDetalles(id: Int): LiveData<List<RequerimientoDetalle>> {
        return roomRepository.getRequerimientoDetalles(id)
    }

    fun getRequerimientos(): LiveData<List<Requerimiento>> {
        return roomRepository.getRequerimientos()
    }

    fun getRequerimientoId(): LiveData<Int> {
        return roomRepository.getRequerimientoId()
    }

    fun getCentroCostos(): LiveData<List<RequerimientoCentroCosto>> {
        return roomRepository.getRequerimientoCentroCostos()
    }

    fun getDelegacion(): LiveData<List<Delegacion>> {
        return roomRepository.getDelegacion()
    }

    fun deleteRequerimiento(r: Requerimiento) {
        roomRepository.deleteRequerimiento(r)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    mensajeSuccess.value = "Eliminado"
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun sendRequerimiento() {
        val ots: Observable<List<Requerimiento>> = roomRepository.getRequerimientoTask()
        ots.flatMap { observable ->
            Observable.fromIterable(observable).flatMap { a ->
                val json = Gson().toJson(a)
                Log.i("TAG", json)
                val body =
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
                Observable.zip(
                    Observable.just(a), roomRepository.sendRequerimiento(body), { _, mensaje ->
                        mensaje
                    })
            }
        }.subscribeOn(Schedulers.io())
            .delay(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {
                override fun onComplete() {
                    mensajeSuccess.value = "Enviado"
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: Mensaje) {
                    updateEnabledRequerimiento(t)
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

    private fun updateEnabledRequerimiento(t: Mensaje) {
        roomRepository.updateEnabledRequerimiento(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun getMaterial(codigo: String): LiveData<RequerimientoMaterial> {
        return roomRepository.getRequerimientoMaterial(codigo)
    }

    fun deleteRequerimientoDetalle(d: RequerimientoDetalle) {
        roomRepository.deleteRequerimientoDetalle(d)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun getRequerimientoMateriales(): LiveData<List<RequerimientoMaterial>> {
        return roomRepository.getRequerimientoMateriales()
    }

    fun getEstados(): LiveData<List<RequerimientoEstado>> {
        return roomRepository.getRequerimientoEstado()
    }

    fun getTipos(): LiveData<List<RequerimientoTipo>> {
        return roomRepository.getTipos()
    }

    fun sendUpdateCantidad(q: Query, id: Int) {
        val json = Gson().toJson(q)
        Log.i("TAG", json)
        val body =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        roomRepository.sendUpdateCantidadPedido(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Mensaje) {
                    updateCantidadPedido(id, q.cantidad)
                }

                override fun onError(t: Throwable) {
                    if (t is HttpException) {
                        val b = t.response().errorBody()
                        try {
                            val error = retrofit.errorConverter.convert(b!!)
                            mensajeError.postValue(error.Message)
                        } catch (e1: IOException) {
                            e1.printStackTrace()
                        }
                    } else {
                        mensajeError.postValue(t.message)
                    }
                }

                override fun onComplete() {}
            })
    }

    private fun updateCantidadPedido(id: Int, cantidad: Double) {
        roomRepository.updateCantidadPedido(id, cantidad)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    //tipo -> orden o pedido
    fun sendAprobarORechazar(tipo: Int, q: Query, cabeceraId: Int) {
        val json = Gson().toJson(q)
        Log.i("TAG", json)
        val body =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        roomRepository.sendUpdateAprobacionOrRechazo(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Mensaje) {
                    updateAprobacionOrRechazo(tipo, cabeceraId)
                }

                override fun onError(t: Throwable) {
                    if (t is HttpException) {
                        val b = t.response().errorBody()
                        try {
                            val error = retrofit.errorConverter.convert(b!!)
                            mensajeError.postValue(error.Message)
                        } catch (e1: IOException) {
                            e1.printStackTrace()
                        }
                    } else {
                        mensajeError.postValue(t.message)
                    }
                }

                override fun onComplete() {}
            })
    }

    private fun updateAprobacionOrRechazo(tipo: Int, id: Int) {
        roomRepository.updateAprobacionOrRechazo(tipo, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun getCombosEstados(): LiveData<List<ComboEstado>> {
        return roomRepository.getCombosEstados()
    }

    fun sendAnulacion(tipo: Int, q: Query, cabeceraId: Int) {
        val json = Gson().toJson(q)
        Log.i("TAG", json)
        val body =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        roomRepository.sendAnulacion(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Mensaje) {
                    updateAnulacion(tipo, cabeceraId)
                }

                override fun onError(t: Throwable) {
                    if (t is HttpException) {
                        val b = t.response().errorBody()
                        try {
                            val error = retrofit.errorConverter.convert(b!!)
                            mensajeError.postValue(error.Message)
                        } catch (e1: IOException) {
                            e1.printStackTrace()
                        }
                    } else {
                        mensajeError.postValue(t.message)
                    }
                }

                override fun onComplete() {}
            })
    }

    private fun updateAnulacion(tipo: Int, id: Int) {
        roomRepository.updateAnulacion(tipo, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }
}