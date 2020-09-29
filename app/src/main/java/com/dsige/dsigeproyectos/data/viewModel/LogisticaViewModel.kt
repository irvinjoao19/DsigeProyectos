package com.dsige.dsigeproyectos.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dsige.dsigeproyectos.data.local.model.logistica.*
import com.dsige.dsigeproyectos.data.local.repository.ApiError
import com.dsige.dsigeproyectos.data.local.repository.AppRepository
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LogisticaViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) :
    ViewModel() {

    val mensajeError: MutableLiveData<String> = MutableLiveData()
    val mensajeSuccess: MutableLiveData<String> = MutableLiveData()
    val search: MutableLiveData<String> = MutableLiveData()

    fun setError(s: String) {
        mensajeError.value = s
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

                override fun onError(e: Throwable) {

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

                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }

            })
    }

    fun getPedidoGroup(): LiveData<List<Pedido>> {
        return roomRepository.getPedidoGroup()
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

                override fun onError(e: Throwable) {

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
                override fun onSubscribe(d: Disposable) {

                }

                override fun onComplete() {

                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }

            })
    }
    fun getOrdenGroup(): LiveData<List<Orden>> {
        return roomRepository.getOrdenGroup()
    }
    fun getOrdenGroupOne(codigo:String) : LiveData<Orden>{
        return roomRepository.getOrdenGroupOne(codigo)
    }
    fun getOrdenByCodigo(codigo: String): LiveData<List<Orden>> {
        return roomRepository.getOrdenByCodigo(codigo)
    }
}