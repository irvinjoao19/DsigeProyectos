package com.dsige.dsigeproyectos.helper

open class Mensaje {
    var codigo: Int = 0
    var codigoBase: Int = 0
    var codigoRetorno: Int = 0
    var mensaje: String = ""
    var detalle: List<MensajeDetalle>? = null

    constructor(codigo: Int, mensaje: String) {
        this.codigo = codigo
        this.mensaje = mensaje
    }
}