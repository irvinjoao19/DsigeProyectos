package com.dsige.dsigeproyectos.data.local.model

import com.dsige.dsigeproyectos.data.local.model.engie.Area
import com.dsige.dsigeproyectos.data.local.model.engie.CentroCostos
import com.dsige.dsigeproyectos.data.local.model.engie.Sucursal

open class Filtro {
    var sucursales: List<Sucursal> = ArrayList()
    var areas: List<Area> = ArrayList()
    var centros: List<CentroCostos> = ArrayList()
}