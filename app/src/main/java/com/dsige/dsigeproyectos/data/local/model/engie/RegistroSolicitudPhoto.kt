package com.dsige.dsigeproyectos.data.local.model.engie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class RegistroSolicitudPhoto {

    @PrimaryKey(autoGenerate = true)
    var registroPhotoId: Int = 0
    var solicitudId: Int = 0
    var nombre: String = ""
    var fecha: String = ""
    var tipo: Int = 0 // Para Envio
    var identity: Int = 0
    var identityFoto: Int = 0
}