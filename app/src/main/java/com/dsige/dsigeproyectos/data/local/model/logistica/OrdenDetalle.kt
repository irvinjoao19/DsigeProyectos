package com.dsige.dsigeproyectos.data.local.model.logistica

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class OrdenDetalle {

    @PrimaryKey(autoGenerate = true)
    var detalleId : Int = 0
    var id: Int = 0
    var materialId: String = ""
    var fecha: String = ""
    var proveedor: String = ""
    var razonSocial: String = ""
    var precio: String = ""
}
