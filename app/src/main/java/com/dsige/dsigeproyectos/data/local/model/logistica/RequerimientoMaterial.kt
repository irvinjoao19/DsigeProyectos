package com.dsige.dsigeproyectos.data.local.model.logistica

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class RequerimientoMaterial {

    @PrimaryKey
    var codigo: String = ""
    var descripcion: String = ""
    var abreviatura: String = ""
    var tipoOrden: String = ""
}