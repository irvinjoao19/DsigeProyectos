package com.dsige.dsigeproyectos.data.local.model.engie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class TipoDevolucion {
    @PrimaryKey
    var tipo: Int = 0
    var descripcion: String = ""
    var estado: Int = 0
}