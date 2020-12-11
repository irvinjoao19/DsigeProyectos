package com.dsige.dsigeproyectos.data.local.model.logistica

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class OrdenEstado {
    @PrimaryKey
    var codigo: String = ""
    var nombre: String = ""
}