package com.dsige.dsigeproyectos.data.local.model.logistica

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class CampoJefe {

    @PrimaryKey
    var idDet: Int = 0
    var id: Int = 0
    var pubSucuCodigo: String = ""
    var almaCodigo: String = ""
    var obraCodigo: String = ""
    var pedcNumero: String = ""
    var pedcFechaEnvio: String = ""
    var pedcFolioOrigen: String = ""
    var nomApellidos: String = ""
    var pedcFechaCreada: String = ""
    var usuarioCrea: String = ""
    var nombreOt: String = ""
    var almaDescripcion: String = ""
    var nombreEstado: String = ""
    var articulo: String = ""
    var nombreArticulo: String = ""
    var cantidadPedida: Double = 0.0
    var cantidadAprobada: Double = 0.0
    var guicGuiaRemision: String = ""
    var guiaRemision: String = ""
    var estadoId: Int = 1
}