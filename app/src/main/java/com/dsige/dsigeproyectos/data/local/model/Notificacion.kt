package com.dsige.dsigeproyectos.data.local.model

open class Notificacion {
    var cant_ot: Int = 0
    var mensaje: String = ""
    var id_personal: Int = 0
    var tipo: String = ""

    constructor()

    constructor(cant_ot: Int, mensaje: String, id_personal: Int, tipo: String) {
        this.cant_ot = cant_ot
        this.mensaje = mensaje
        this.id_personal = id_personal
        this.tipo = tipo
    }
}