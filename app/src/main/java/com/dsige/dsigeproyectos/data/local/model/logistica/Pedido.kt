package com.dsige.dsigeproyectos.data.local.model.logistica

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Pedido {
    @PrimaryKey
    var pedidoId: Int = 0    // int pedidoId
    var tipoPedidoId: String = ""   // string
    var nombreTipoPedido: String = ""   // string
    var nroPedido: String = ""   // string
    var fechaEnvio: String = ""   // string
    var delegacion: String = ""   // string
    var ccId: String = ""   // string
    var centroCostos: String = ""   // string
    var dni: String = ""   // string
    var nombreEmpleado: String = ""   // string
    var pubEstaCodigo: String = ""   // string
    var estado: String = ""   // string
    var moneda: String = ""   // string
    var item: Int = 0   // int
    var articulo: String = ""
    var nombreArticulo: String = ""
    var cantidad: Double = 0.0
    var cantidadAprobada: Double = 0.0
    var precio: Double = 0.0
}