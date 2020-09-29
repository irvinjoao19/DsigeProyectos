package com.dsige.dsigeproyectos.data.local.model.engie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Cuadrilla {

    @PrimaryKey
    var centroId: String = ""
    var orden: String = ""
    var cuadrillaId: String = ""
    var descripcion: String = ""
    var dni: String = ""
}