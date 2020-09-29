package com.dsige.dsigeproyectos.data.local.model.engie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class ParametroE {

    @PrimaryKey(autoGenerate = true)
    var id_Configuracion: Int = 0
    var nombre_parametro: String = ""
    var valor: Int = 0
}
