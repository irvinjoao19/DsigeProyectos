package com.dsige.dsigeproyectos.data.local.model.logistica

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class MenuLogistica {
    @PrimaryKey
    var id : Int = 0
    var nombre : String = ""
    var orden : Int = 0
}