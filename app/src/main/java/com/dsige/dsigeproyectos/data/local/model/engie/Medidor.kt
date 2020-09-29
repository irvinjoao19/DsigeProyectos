package com.dsige.dsigeproyectos.data.local.model.engie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Medidor {

    @PrimaryKey
    var medidorId: String = ""
    var sucursalCodigo: String = ""
    var almacenCodigo: String = ""
    var empleadoDni: String = ""
    var guiaNumero: String = ""
    var articuloCodigo: String = ""
}