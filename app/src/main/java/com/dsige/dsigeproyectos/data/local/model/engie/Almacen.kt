package com.dsige.dsigeproyectos.data.local.model.engie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Almacen {
    @PrimaryKey
    var codigo: String = ""
    var tipoCodigo: String = ""
    var sucursalCodigo: String = ""
    var descripcion: String = ""
    var opCodigo: String = ""
    var inventario: String = ""
    var equipamiento: String = ""
    var seraEdelnor: String = ""
}