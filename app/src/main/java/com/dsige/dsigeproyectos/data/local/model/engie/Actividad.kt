package com.dsige.dsigeproyectos.data.local.model.engie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Actividad {

    @PrimaryKey
    var actividadId: Int = 0
    var descripcion: String = ""
}