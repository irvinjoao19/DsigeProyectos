package com.dsige.dsigeproyectos.data.local.model

open class Query {

    var dni: String = ""
    var user: String = ""
    var pass: String = ""
    var areaId: String = ""
    var usuarioId: String = ""
    var cuadrillaId: String = ""
    var sucursalId: String = ""
    var centroCostoId: String = ""
    var filtro: Int = 0
    var fechaInicial: String = ""
    var fechaFinal: String = ""
    var estado: String = ""
    var tipoProceso: String = ""
    var tipoCliente: String = ""
    var codigoArticulo: String = ""
    var almacenId: String = ""
    var tipoSolicitud: Int = 0
    var tipoMaterialSolicitud: Int = 0
    var fechaRegistro: String = ""

    var pageIndex: Int = 0
    var pageSize: Int = 0
    var search: String = ""
    var obraId: String = ""
    var solicitudId: Int = 0
    var personalDni: String = ""

    var login: String = ""

    var imei: String = ""
    var version: String = ""

    constructor()

    constructor(user: String, pass: String) {
        this.user = user
        this.pass = pass
    }

    constructor(login: String, pass: String, imei: String, version: String) {
        this.login = login
        this.pass = pass
        this.imei = imei
        this.version = version
    }
}