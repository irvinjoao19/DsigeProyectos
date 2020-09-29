package com.dsige.dsigeproyectos.data.local.model.engie

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class RegistroGeneral {

    @PrimaryKey(autoGenerate = true)
    var registroGeneralId: Int = 0
    var empresaCodigo: String = ""
    var areaCodigo: String = ""
    var obraTd: String = ""
    var codigoInterno: String = ""
    var fecha: String = ""
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

    constructor() : super()

    @Ignore
    constructor(
        registroGeneralId: Int,
        empresaCodigo: String,
        areaCodigo: String,
        obraTd: String,
        codigoInterno: String,
        fecha: String,
        costoCodigo: String,
        cuadrillaCodigo: String,
        dniCuadrilla: String,
        suministro: String,
        sed: String,
        observacion: String,
        estadoCodigo: String,
        usuarioCreacion: String,
        fechaMovil: String
    ) : super() {
        this.registroGeneralId = registroGeneralId
        this.empresaCodigo = empresaCodigo
        this.areaCodigo = areaCodigo
        this.obraTd = obraTd
        this.codigoInterno = codigoInterno
        this.fecha = fecha
        this.costoCodigo = costoCodigo
        this.cuadrillaCodigo = cuadrillaCodigo
        this.dniCuadrilla = dniCuadrilla
        this.suministro = suministro
        this.sed = sed
        this.observacion = observacion
        this.estadoCodigo = estadoCodigo
        this.usuarioCreacion = usuarioCreacion
        this.fechaMovil = fechaMovil
    }
}