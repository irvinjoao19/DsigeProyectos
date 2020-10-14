package com.dsige.dsigeproyectos.data.local.model.logistica

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class RequerimientoDetalle {

    @PrimaryKey(autoGenerate = true)
    var detalleId: Int = 0
    var requerimientoId: Int = 0
    var material: String = ""
    var descripionMaterial: String = ""
    var um: String = ""
    var cantidad: Double = 0.0
    var identity: Int = 0
}
