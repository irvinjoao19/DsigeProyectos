package com.dsige.dsigeproyectos.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Estado {

    @PrimaryKey
    var codigo: String = ""
    var nombre: String = ""
}