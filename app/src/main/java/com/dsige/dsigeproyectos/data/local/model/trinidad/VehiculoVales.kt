package com.dsige.dsigeproyectos.data.local.model.trinidad

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class VehiculoVales {

    @PrimaryKey(autoGenerate = true)
    var valeId: Int = 0
    var placa: String = ""
    var gesCodigo: String = ""
    var dni: String = ""
    var nroVale: String = ""
    var fecha: String = ""
    var tipo: String = "" // combustible
    var nombreTipo: String = ""
    var precioIGV: Double = 0.0
    var cantidadGalones: Double = 0.0
    var kmValeCombustible: Double = 0.0
    var rucGrifo: String = ""
    var nombreGrifo: String = ""
    var pubCodigo: String = ""
    var foto: String = ""
    var estado: Int = 0 // 0 -> por enviar 1 -> enviado
}