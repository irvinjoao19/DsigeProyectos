package com.dsige.dsigeproyectos.data.local.model.logistica

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class RequerimientoTipo {

    @PrimaryKey
    var codigo: String = ""
    var nombre: String = ""
}