package com.dsige.dsigeproyectos.data.local.model.engie

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class CentroCostos {

    @PrimaryKey
    var orden: String = ""
    var centroId: String = ""
    var descripcion: String = ""
    var sucursalId: String = ""
    var nombreSucursal: String = ""

    @Ignore
    var cuadrillas: List<Cuadrilla>? = null

}