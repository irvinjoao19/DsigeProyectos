package com.dsige.dsigeproyectos.data.local.model.engie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Coordinador {
    @PrimaryKey
    var codigo: String = ""
    var nombre: String = ""
}