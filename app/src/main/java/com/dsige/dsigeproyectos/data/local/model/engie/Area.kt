package com.dsige.dsigeproyectos.data.local.model.engie

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class Area  {
    @PrimaryKey
    var areaId: String = ""
    var descripcion: String = ""
    var estado: String = ""
    var manual: String = ""
    var medidorEncontrado: String = ""
    var medidorInstalado:  String = ""
}