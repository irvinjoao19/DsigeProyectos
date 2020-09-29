package com.dsige.dsigeproyectos.data.local.model.engie

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class Solicitud {

    @PrimaryKey(autoGenerate = true)
    var solicitudId: Int = 0
    var tipoMaterialSol: Int = 0
    var tipoSolicitudId: Int = 0

    var tipoMaterial: Int = 0
    var nombreTipoMaterial: String = ""

    var nroInterno: String = ""
    var fechaAtencion: String = "" //
    var obraTd: String = ""
    var codigoEstadoSol: String = ""
    var estadoSol: String = ""
    var codigoEstadoObra: String = ""
    var estadoObra: String = ""
    var descripcionObra: String = ""
    var direccionObra: String = ""
    var clienteObra: String = ""
    var fechaAsignacion: String = "" // fecha movil
    var fechaVencimiento: String = ""

    var observacion: String = ""
    var centroCosto: String = ""
    var cuadrillaCodigo: String = ""
    var dniCuadrilla: String = ""  // dni usuario
    var pubEstadoCodigo: String = ""
    var numeroGuia: String = ""
    var usuario: String = ""

    var filtro: Int = 0 // filtro
    var tipo: Int = 0 // filtro
    var identity: Int = 0

    var dniCoordinador: String = ""
    var nombreCoordinador: String = ""
    var dniPersonal: String = ""
    var nombrePersonal: String = ""
    var sucursalId: String = ""

    @Ignore
    var materiales: List<RegistroSolicitudMaterial>? = null
    @Ignore
    var photos: List<RegistroSolicitudPhoto>? = null
}