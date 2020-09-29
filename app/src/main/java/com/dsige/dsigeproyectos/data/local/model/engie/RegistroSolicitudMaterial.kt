package com.dsige.dsigeproyectos.data.local.model.engie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class RegistroSolicitudMaterial {

    @PrimaryKey(autoGenerate = true)
    var registroMaterialId: Int = 0
    var solicitudId: Int = 0
    var tipoSolicitudId: Int = 0
    var tipoMaterial: Int = 0
    var codigoMaterial: String = ""
    var cantidadMovil: Double = 0.0
    var cantidadOk: Double = 0.0
    var fecha: String = ""
    var descripcion: String = ""
    var unidadMedida: String = ""
    var abreviatura: String = ""
    var tipo: Int = 0
    var stock: Double = 0.0 // validationMovil
    var identity: Int = 0
    var identityDetalle: Int = 0
    var filtro: Int = 0
    var almacenId: String = ""
    var guiaSalida: String = ""
    var usuarioId: String = ""
    var cantidadAprobada: Double = 0.0
    var estado: String = ""
    var guiaIngresoId: String = ""
    var guiaIngreso: String = ""
}