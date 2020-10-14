package com.dsige.dsigeproyectos.data.local.model.logistica

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class Requerimiento {

    @PrimaryKey(autoGenerate = true)
    var requerimientoId: Int = 0
    var nroSolicitud: String = ""
    var tipoSolicitud: String = ""
    var nombreTipoSolicitud: String = ""
    var fecha: String = ""
    var codigoDelegacion: String = ""
    var nombreDelegacion: String = ""
    var codigoCentroCosto: String = ""
    var nombreCentroCosto: String = ""
    var observaciones: String = ""
    var estado: String = ""
    var estadoId: Int = 0
    var identity : Int = 0
    var usuario:String = ""

    @Ignore
    var detalle: List<RequerimientoDetalle>? = null
}