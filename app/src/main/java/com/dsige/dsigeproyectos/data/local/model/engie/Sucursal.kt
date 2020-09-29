package com.dsige.dsigeproyectos.data.local.model.engie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Sucursal {
    @PrimaryKey
    var codigo: String = ""
    var nombre: String = ""
}