package com.dsige.dsigeproyectos.data.local.model.trinidad

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class VehiculoControl {

    @PrimaryKey(autoGenerate = true)
    var controlId: Int = 0
    var placa: String = ""
    var fecha: String = ""
    var kmIngreso: Double = 0.0
    var kmSalida: Double = 0.0
    var choferDni: String = ""
    var gesCodigo: String = ""
    var pubCodigo: String = ""
    var estado: Int = 0 // 0 -> por enviar 1 -> enviado
}