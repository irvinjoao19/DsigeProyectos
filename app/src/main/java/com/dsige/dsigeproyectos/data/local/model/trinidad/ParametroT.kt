package com.dsige.dsigeproyectos.data.local.model.trinidad

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class ParametroT {

    @PrimaryKey
    var campo1: String = ""
    var tipo: Int = 0 //1 -> grifo 2-> combustible
    var campo2: String = ""

}