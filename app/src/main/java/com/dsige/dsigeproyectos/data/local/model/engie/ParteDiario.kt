package com.dsige.dsigeproyectos.data.local.model.engie

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class ParteDiario {

    @PrimaryKey(autoGenerate = true)
    var parteDiarioId: Int = 0
    var fecha: String = ""
    var obraTd: String = ""
    var codigoEstadoPd: String = ""
    var estadoTd: String = ""
    var codigoEstadoObra: String = ""
    var estadoObra: String = ""
    var descripcion: String = ""
    var direccion: String = ""
    var cliente: String = ""
    var fechaAsignacion: String = ""
    var fechaVencimiento: String = ""

    var empresaCodigo: String = ""
    var areaCodigo: String = ""
    var codigoInterno: String = ""
    var costoCodigo: String = ""
    var cuadrillaCodigo: String = ""
    var dniCuadrilla: String = ""
    var suministro: String = ""
    var sed: String = ""
    var observacion: String = ""
    var medencontrado_nro: String = ""
    var medencontrado_marca: String = ""
    var medencontrado_fase: String = ""
    var medencontrado_estado: String = ""
    var medencontrado_modelo: String = ""
    var medinstalado_nro: String = ""
    var medinstalado_marca: String = ""
    var medinstalado_fase: String = ""
    var medinstalado_estado: String = ""
    var medinstalado_modelo: String = ""
    var latitud: String = ""
    var longitud: String = ""
    var estadoCodigo: String = ""
    var usuarioCreacion: String = ""
    var fechaMovil: String = ""
    var firmaMovil: String = ""
    var coordinadorDni: String = ""
    var descripcionCoordinador : String = ""
    var sucursalId: String = ""

    var tipo: Int = 0
    var identity: Int = 0

    @Ignore
    var baremos: List<RegistroBaremo>? = null

    @Ignore
    var materiales: List<RegistroMaterial>? = null

    @Ignore
    var photos: List<RegistroPhoto>? = null

    /**
     * estado -> 1 se volvera para que no se pueda volver a enviarse hasta que sea aprobado
     */
    var estado: Int = 0
}