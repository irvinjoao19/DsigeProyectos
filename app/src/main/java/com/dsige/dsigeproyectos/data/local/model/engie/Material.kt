package com.dsige.dsigeproyectos.data.local.model.engie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Material {

    @PrimaryKey
    var id: Int = 0
    var materialId: String = ""
    var descripcion: String = ""
    var unidadMedida: String = ""
    var abreviatura: String = ""
    var stock: Double = 0.0
    var obra: String = ""
    var cc: String = ""
    var almacenId: String = ""
    var guiaSalida: String = ""
    var fecha: String = ""
    var tipo: Int = 0 // 1 -> ParteDiario 2-> Solicitud
    var exigeSerie : String = ""
    var guiaIngresoId: String = ""
    var guiaIngreso: String = ""
}