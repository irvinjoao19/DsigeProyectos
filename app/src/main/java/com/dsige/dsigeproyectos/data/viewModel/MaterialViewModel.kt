package com.dsige.dsigeproyectos.data.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dsige.dsigeproyectos.data.local.model.engie.Material
import com.dsige.dsigeproyectos.data.local.model.Query
import com.dsige.dsigeproyectos.data.local.repository.ApiError
import com.dsige.dsigeproyectos.data.local.repository.AppRepository
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MaterialViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val pagination = PublishProcessor.create<Int>()
    val material: MutableLiveData<List<Material>> = MutableLiveData()
    val mensajeError: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val pageNumber: MutableLiveData<Int> = MutableLiveData()

    val error: LiveData<String>
        get() = mensajeError

    val load: LiveData<Boolean>
        get() = loading


    fun setPageNumber(number: Int) {
        pageNumber.value = number
    }

    fun setLoading(load: Boolean) {
        loading.value = load
    }

    fun getMateriales(): LiveData<List<Material>> {
        return material
    }

    fun clearMateriales() {
        material.value = null
        loading.value = true
        mensajeError.value = null
        compositeDisposable.clear()
        pageNumber.value = 1
    }

    fun paginationStockMateriales(q: Query) {
        val disposable = pagination
                .onBackpressureDrop()
                .concatMap { page ->
                    q.pageIndex = page
                    q.pageSize = 20
                    val json = Gson().toJson(q)
                    Log.i("TAG", json)
                    val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
                    roomRepository.paginationStockMaterial(body)
                            .delay(600, TimeUnit.MILLISECONDS)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map { b -> b }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ b ->
                    material.postValue(b)
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
            pagination.onNext(n)
        }
    }

    fun clear() {
        compositeDisposable.clear()
    }

    fun next(page: Int) {
        pagination.onNext(page)
    }
}