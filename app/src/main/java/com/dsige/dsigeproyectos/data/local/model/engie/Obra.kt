package com.dsige.dsigeproyectos.data.local.model.engie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Obra {

    @PrimaryKey
    var obraId: String = ""
    var descripcion: String = ""
    var estado: String = ""
    var direccion: String = ""
    var cliente: String = ""
    var fechaAsignacion: String = ""
    var fechaVencimiento: String = ""
    var usuarioCreacion: String = ""
}