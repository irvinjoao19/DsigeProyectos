package com.dsige.dsigeproyectos.data.local.model.trinidad

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class Vehiculo {

    @PrimaryKey
    var placa: String = ""
    var codigo: String = ""
    var tipoVehiculo: String = ""
    var marca: String = ""
    var modelo: String = ""
    var anio: String = ""
    var combustible: String = ""
    var condicion: String = ""
    var costo: String = ""
    var serie: String = ""
    var estado: Int = 0 // 0 -> por enviar , 1 -> enviado

    @Ignore
    var control: List<VehiculoControl>? = null

    @Ignore
    var registros: List<VehiculoVales>? = null
}