package com.dsige.dsigeproyectos.data.local.model.engie

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class Baremo {

    @PrimaryKey
    var baremoId: String = ""
    var descripcion: String = ""
    var unidadMedida: String = ""
    var abreviatura: String = ""
    var actividadId: Int = 0

}