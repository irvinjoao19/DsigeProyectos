package com.dsige.dsigeproyectos.data.local.model.engie

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity
open class RegistroBaremo {

    @PrimaryKey(autoGenerate = true)
    var registroBaremoId: Int = 0
    var parteDiarioId: Int = 0
    var codigoBaremo: String = ""
    var cantidadMovil: Double = 0.0
    var cantidadOk: Double = 0.0
    var fecha: String = ""
    var descripcion: String = ""
    var unidadMedida: String = ""
    var abreviatura: String = ""
    var tipo: Int = 0
    var identity: Int = 0
    var identityDetalle: Int = 0
    var actividadId: Int = 0
    var estado: String = ""
}