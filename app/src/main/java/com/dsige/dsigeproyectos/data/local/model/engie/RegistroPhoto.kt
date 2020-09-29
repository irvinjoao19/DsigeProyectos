package com.dsige.dsigeproyectos.data.local.model.engie

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class RegistroPhoto {

    @PrimaryKey(autoGenerate = true)
    var registroPhotoId: Int = 0
    var parteDiarioId: Int = 0
    var nombre: String = ""
    var fecha: String = ""
    var tipo: Int = 0 // Para Envio
    var identity: Int = 0
    var identityFoto: Int = 0
}