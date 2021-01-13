package com.dsige.dsigeproyectos.data.local.model

import com.dsige.dsigeproyectos.data.local.model.engie.*
import com.dsige.dsigeproyectos.data.local.model.trinidad.*
import com.dsige.dsigeproyectos.data.local.model.logistica.*

open class Sync {
    // trinidad
    var parametrosT: List<ParametroT> = ArrayList()
    var vehiculos: List<Vehiculo> = ArrayList()
    var estados: List<Estado> = ArrayList()
    var almacens: List<Almacen> = ArrayList()
    var parametrosE: List<ParametroE> = ArrayList()
    var parteDiarios: List<ParteDiario> = ArrayList()
    var baremos: List<Baremo> = ArrayList()
    var materiales: List<Material> = ArrayList()
    var obras: List<Obra> = ArrayList()
    var articulos: List<Articulo> = ArrayList()
    var coordinadors: List<Coordinador> = ArrayList()
    var actividades: List<Actividad> = ArrayList()
    var medidores: List<Medidor> = ArrayList()
    var devoluciones: List<TipoDevolucion> = ArrayList()
    var personals: List<Personal> = ArrayList()


    var delegaciones: List<Delegacion> = ArrayList()
    var requerimientoMateriales: List<RequerimientoMaterial> = ArrayList()
    var requerimientoEstado: List<RequerimientoEstado> = ArrayList()
    var comboEstados: List<ComboEstado> = ArrayList()
    var requerimientoTipo: List<RequerimientoTipo> = ArrayList()
    var requerimientoCentroCostos: List<RequerimientoCentroCosto> = ArrayList()
    var almacenes: List<AlmacenLogistica> = ArrayList()
    var locales: List<Local> = ArrayList()
    var ordenEstados: List<OrdenEstado> = ArrayList()
    var menu: List<MenuLogistica> = ArrayList()

}