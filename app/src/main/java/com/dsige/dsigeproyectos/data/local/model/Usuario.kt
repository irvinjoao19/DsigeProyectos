package com.dsige.dsigeproyectos.data.local.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class Usuario {

    @PrimaryKey
    var usuarioId: String = ""
    var nombre: String = ""
    var mensaje: String = ""
    var areaId: String = ""
    var centroId: String = ""
    var descripcion: String = ""
    var cuadrillaId: String = ""
    var dniCuadrillaId: String = ""
    var estado: Int = 0
    var sucursalId: String = ""
    var sucursal: String = ""
    var servicio: String = ""

    @Ignore
    var filtro: Filtro = Filtro()
}