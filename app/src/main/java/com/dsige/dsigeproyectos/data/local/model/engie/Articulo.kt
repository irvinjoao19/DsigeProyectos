package com.dsige.dsigeproyectos.data.local.model.engie

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class Articulo {

    @PrimaryKey(autoGenerate = true)
    var articuloId: Int = 0
    var descripcion: String = ""
    var tipo: Int = 0

    constructor() : super()

    @Ignore
    constructor(articuloId: Int, descripcion: String, tipo: Int) : super() {
        this.articuloId = articuloId
        this.descripcion = descripcion
        this.tipo = tipo
    }
}