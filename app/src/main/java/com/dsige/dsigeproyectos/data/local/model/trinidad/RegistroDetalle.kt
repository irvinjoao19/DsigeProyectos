package com.dsige.dsigeproyectos.data.local.model.trinidad

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class RegistroDetalle {
    @PrimaryKey(autoGenerate = true)
    var detalleId: Int = 0
    var registroId: Int = 0
    var tipo: Int = 0 // 1 -> antes 2 -> despues
    var nombrePunto: String = ""

    var largo: Double = 0.0
    var ancho: Double = 0.0
    var totalM3: Double = 0.0
    var observacion: String = ""

    var foto1PuntoAntes: String = ""
    var foto2PuntoAntes: String = ""
    var foto3PuntoAntes: String = ""

    var foto1PuntoDespues: String = ""
    var foto2PuntoDespues: String = ""
    var foto3PuntoDespues: String = ""
    var estado: Int = 0

    var active1: Int = 0
    var active2: Int = 0
}