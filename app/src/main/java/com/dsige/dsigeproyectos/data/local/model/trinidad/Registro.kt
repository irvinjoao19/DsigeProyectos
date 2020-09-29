package com.dsige.dsigeproyectos.data.local.model.trinidad

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class Registro {
    @PrimaryKey(autoGenerate = true)
    var registroId: Int = 0
    var tipo: Int = 0
    var nroObra: String = ""
    var nroPoste: String = ""
    var foto: String = ""
    var latitud: String = ""
    var longitud: String = ""
    var usuarioId: String = ""
    var estado: String = ""
    var fecha: String = ""
    var active: Int = 0
    var identity: Int = 0
    var punto: String = ""

    @Ignore
    var detalles: RegistroDetalle? = null

    @Ignore
    var list: List<RegistroDetalle>? = null

}