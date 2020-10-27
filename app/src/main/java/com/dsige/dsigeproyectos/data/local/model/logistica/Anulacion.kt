package com.dsige.dsigeproyectos.data.local.model.logistica

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class Anulacion {

    @PrimaryKey
    var id: Int = 0
    var ordenId: Int = 0
    var tipoPedidoId: String = ""
    var toc: String = ""
    var nroOrden: String = ""
    var fechaEmisionOrden: String = ""
    var contableOt: String = ""
    var usuSolicitante: String = ""
    var usuCreaOrden: String = ""
    var fechaCreaOrden: String = ""
    var fechaAprobacion: String = ""
    var provee: String = ""
    var forma: String = ""
    var mone: String = ""
    var subtotalOc: Double = 0.0
    var igv: Double = 0.0
    var totalOc: Double = 0.0
    var codEstado: String = ""
    var estado: String = ""
    var item: Int = 0
    var articulo: String = ""
    var nombreArticulo: String = ""
    var cantidadAprobada: Double = 0.0
    var precio: Double = 0.0
    var importe: Double = 0.0
    var delegacion: String = ""
    var estadoId: Int = 1

    @Ignore
    var detalles: List<OrdenDetalle>? = null
}