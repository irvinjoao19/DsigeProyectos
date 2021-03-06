package com.dsige.dsigeproyectos.ui.listeners

import android.view.View
import com.dsige.dsigeproyectos.data.local.model.*
import com.dsige.dsigeproyectos.data.local.model.engie.*
import com.dsige.dsigeproyectos.data.local.model.logistica.*
import com.dsige.dsigeproyectos.data.local.model.trinidad.*

interface OnItemClickListener {

    interface MenuListener {
        fun onItemClick(m: MenuPrincipal, view: View, position: Int)
    }

    interface MenuLogisticaListener {
        fun onItemClick(m: MenuLogistica, view: View, position: Int)
    }

    interface RegistroListener {
        fun onItemClick(r: Registro, view: View, position: Int)
    }

    interface DetalleListener {
        fun onItemClick(r: RegistroDetalle, view: View, position: Int)
    }

    interface PhotoListener {
        fun onItemClick(r: RegistroDetalle, view: View, position: Int)
    }

    interface VehiculoListener {
        fun onItemClick(v: Vehiculo, view: View, position: Int)
    }

    interface ControlListener {
        fun onItemClick(c: VehiculoControl, view: View, position: Int)
    }

    interface ParametroListener {
        fun onItemClick(p: ParametroE, view: View, position: Int)
    }

    interface ValeListener {
        fun onItemClick(c: VehiculoVales, view: View, position: Int)
    }

    interface EstadoListener {
        fun onItemClick(e: Estado, view: View, position: Int)
    }

    interface AlmacenListener {
        fun onItemClick(a: Almacen, view: View, position: Int)
    }

    interface AreaListener {
        fun onItemClick(a: Area, view: View, position: Int)
    }

    interface ArticuloListener {
        fun onItemClick(a: Articulo, view: View, position: Int)
    }

    interface BaremoListener {
        fun onItemClick(b: Baremo, view: View, position: Int)
    }

    interface RegistroBaremoListener {
        fun onItemClick(b: RegistroBaremo, view: View, position: Int)
    }

    interface CentroCostosListener {
        fun onItemClick(c: CentroCostos, view: View, position: Int)
    }

    interface CuadrillaListener {
        fun onItemClick(c: Cuadrilla, view: View, position: Int)
    }

    interface MaterialListener {
        fun onItemClick(ma: Material, view: View, position: Int)
    }

    interface MedidorListener {
        fun onItemClick(me: Medidor, view: View, position: Int)
    }

    interface RegistroMaterialListener {
        fun onItemClick(m: RegistroMaterial, view: View, position: Int)
    }

    interface RegistroPhotoListener {
        fun onItemClick(p: RegistroPhoto, v: View, position: Int)
    }

    interface CoordinadorListener {
        fun onItemClick(c: Coordinador, v: View, position: Int)
    }

    interface ParteDiarioListener {
        fun onItemClick(p: ParteDiario, position: Int, v: View)
    }

    interface ObraListener {
        fun onItemClick(o: Obra, view: View, position: Int)
    }

    interface SucursalListener {
        fun onItemClick(s: Sucursal, v: View, position: Int)
    }

    interface ActividadListener {
        fun onItemClick(a: Actividad, v: View, position: Int)
    }

    interface PersonalListener {
        fun onItemClick(p: Personal, v: View, position: Int)
    }

    interface TipoDevolucionListener {
        fun onItemClick(t: TipoDevolucion, v: View, position: Int)
    }

    interface SolicitudRegistroMaterialListener {
        fun onItemClick(m: RegistroSolicitudMaterial, view: View, position: Int)
    }

    interface SolicitudRegistroPhotoListener {
        fun onItemClick(p: RegistroSolicitudPhoto, v: View, position: Int)
    }

    interface SolicitudListener {
        fun onItemClick(s: Solicitud, v: View, position: Int)
    }

    interface PedidoListener {
        fun onItemClick(p: Pedido, v: View, position: Int)
    }

    interface OrdenListener {
        fun onItemClick(o: Orden, v: View, position: Int)
    }

    interface OrdenDetalleListener {
        fun onItemClick(o: OrdenDetalle, v: View, position: Int)
    }

    interface RequerimientoListener {
        fun onItemClick(r: Requerimiento, v: View, position: Int)
    }

    interface RequerimientoDetalleListener {
        fun onItemClick(r: RequerimientoDetalle, v: View, position: Int)
    }

    interface DelegacionListener {
        fun onItemClick(d: Delegacion, v: View, position: Int)
    }

    interface RequerimientoMaterialListener {
        fun onItemClick(r: RequerimientoMaterial, v: View, position: Int)
    }

    interface RequerimientoCentroCostoListener {
        fun onItemClick(c: RequerimientoCentroCosto, v: View, position: Int)
    }

    interface RequerimientoEstadoListener {
        fun onItemClick(c: RequerimientoEstado, v: View, position: Int)
    }

    interface RequerimientoTipoListener {
        fun onItemClick(c: RequerimientoTipo, v: View, position: Int)
    }

    interface ComboEstadoListener {
        fun onItemClick(c: ComboEstado, v: View, position: Int)
    }

    interface AnulacionListener {
        fun onItemClick(a: Anulacion, v: View, position: Int)
    }

    interface CampoJefeListener {
        fun onItemClick(c: CampoJefe, v: View, position: Int)
    }

    interface TiempoVidaListener {
        fun onItemClick(t: TiempoVida, v: View, position: Int)
    }

    interface LocalListener {
        fun onItemClick(l: Local, v: View, position: Int)
    }

    interface AlmacenLogisticaListener {
        fun onItemClick(l: AlmacenLogistica, v: View, position: Int)
    }

    interface OrdenEstadoListener {
        fun onItemClick(o: OrdenEstado, v: View, position: Int)
    }
}