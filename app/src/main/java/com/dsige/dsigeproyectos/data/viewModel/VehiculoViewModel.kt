package com.dsige.dsigeproyectos.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dsige.dsigeproyectos.data.local.model.trinidad.ParametroT
import com.dsige.dsigeproyectos.data.local.model.trinidad.Vehiculo
import com.dsige.dsigeproyectos.data.local.model.trinidad.VehiculoControl
import com.dsige.dsigeproyectos.data.local.model.trinidad.VehiculoVales
import com.dsige.dsigeproyectos.data.local.repository.AppRepository
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class VehiculoViewModel @Inject
internal constructor(private val roomRepository: AppRepository) :
    ViewModel() {

    val mensajeError: MutableLiveData<String> = MutableLiveData()
    val mensajeSuccess: MutableLiveData<String> = MutableLiveData()

    fun setError(s: String) {
        mensajeError.value = s
    }

    fun populateVehiculo(): LiveData<List<Vehiculo>> {
        return roomRepository.populateVehiculo()
    }

    fun getVehiculo(placa: String): LiveData<Vehiculo> {
        return roomRepository.getVehiculo(placa)
    }

    fun getControlVehiculo(placa: String): LiveData<List<VehiculoControl>> {
        return roomRepository.getControlVehiculo(placa)
    }

    fun getValeVehiculo(placa: String): LiveData<List<VehiculoVales>> {
        return roomRepository.getValeVehiculo(placa)
    }

    fun validateControl(v: VehiculoControl) {
        if (v.controlId == 0) {
            if (v.kmSalida == 0.0) {
                mensajeError.value = "Ingrese Kilometraje"
                return
            }
        } else {
            if (v.kmIngreso == 0.0) {
                mensajeError.value = "Ingrese Kilometraje"
                return
            }
        }
        saveControl(v)
    }

    private fun saveControl(v: VehiculoControl) {
        roomRepository.saveControl(v)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    mensajeSuccess.value = "Ok"

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    fun getControVehiculoById(controlId: Int): LiveData<VehiculoControl> {
        return roomRepository.getControVehiculoById(controlId)
    }

    fun getComboByTipo(tipo: Int): LiveData<List<ParametroT>> {
        return roomRepository.getComboByTipo(tipo)
    }

    fun validateVales(c: VehiculoVales) {

        if (c.fecha.isEmpty()) {
            mensajeError.value = "Ingresar Fecha Emisión"
            return
        }
        if (c.nroVale.isEmpty()) {
            mensajeError.value = "Ingresar Nro Voucher"
            return
        }
        if (c.tipo.isEmpty()) {
            mensajeError.value = "Seleccionar Tipo de Combutible"
            return
        }
        if (c.cantidadGalones == 0.0) {
            mensajeError.value = "Ingresar Cantidad"
            return
        }

        if (c.precioIGV == 0.0) {
            mensajeError.value = "Ingresar Precio"
            return
        }

        if (c.kmValeCombustible == 0.0) {
            mensajeError.value = "Ingresar Kilometraje"
            return
        }

        if (c.rucGrifo.isEmpty()) {
            mensajeError.value = "Seleccionar Grifo"
            return
        }

        if (c.foto.isEmpty()) {
            mensajeError.value = "Tomar Foto de Voucher"
            return
        }

        saveVales(c)
    }

    private fun saveVales(c: VehiculoVales) {
        roomRepository.saveVales(c)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    mensajeSuccess.value = "Ok"

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    fun getValeVehiculoById(id: Int): LiveData<VehiculoVales> {
        return roomRepository.getValeVehiculoById(id)
    }

    fun closeVerificationVehiculo(placa: String) {
        roomRepository.closeVerificationVehiculo(placa)
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<String> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: String) {
                    mensajeSuccess.value = t
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }
}