package com.dsige.dsigeproyectos.data.local.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.dsige.dsigeproyectos.helper.Mensaje
import com.dsige.dsigeproyectos.data.local.model.*
import com.dsige.dsigeproyectos.data.local.model.engie.*
import com.dsige.dsigeproyectos.data.local.model.logistica.*
import com.dsige.dsigeproyectos.data.local.model.trinidad.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Call

interface AppRepository {

    fun getUsuario(): LiveData<Usuario>
    fun getUsuarioId(): LiveData<String>
    fun getUsuarioIdTask(): String
    fun getUsuarioService(
        usuario: String,
        password: String,
        imei: String,
        version: String
    ): Observable<Usuario>

    fun clearUsuario(): Completable
    fun insertUsuario(u: Usuario): Completable
    fun deleteUsuario(): Completable
    fun deleteTotal(context: Context): Completable
    fun getSync(id: String, version: String): Observable<Sync>
    fun saveSync(s: Sync): Completable

    // TODO ENVIOS

    fun getSizeRegistro(): LiveData<Int>
    fun saveData(body: RequestBody): Observable<Mensaje>
    fun saveVehiculo(body: RequestBody): Observable<Mensaje>
    fun verification(body: RequestBody): Observable<String>
    fun verificateInspecciones(id: Int, fecha: String): Observable<Mensaje>
    fun saveInspection(body: RequestBody): Observable<Mensaje>
    fun saveInspectionDetail(body: RequestBody): Observable<Mensaje>
    fun saveGps(body: RequestBody): Observable<Mensaje>
    fun saveGpsTask(body: RequestBody): Call<Mensaje>
    fun saveMovil(body: RequestBody): Observable<Mensaje>
    fun saveMovilTask(body: RequestBody): Call<Mensaje>
    fun updateRegistro(m: Mensaje): Completable
    fun getDataRegistro(estado: Int): Observable<List<Registro>>
    fun getDataVehiculo(estado: Int): Observable<List<Vehiculo>>

    // TODO REGISTRO

    fun insertOrUpdateRegistro(r: Registro, id: Int): Completable
    fun insertOrUpdateRegistroPhoto(p: RegistroDetalle): Completable
    fun getIdentity(): LiveData<Int>
    fun getRegistroPhotoById(id: Int): LiveData<PagedList<RegistroDetalle>>
    fun getRegistroById(id: Int): LiveData<Registro>
    fun deleteGaleria(d: MenuPrincipal, c: Context): Completable
    fun getRegistroByTipo(tipo: Int): LiveData<List<Registro>>
    fun getRegistroPagingByTipo(tipo: Int): LiveData<PagedList<Registro>>
    fun getRegistroPagingByTipo(tipo: Int, s: String): LiveData<PagedList<Registro>>
    fun getRegistroByObra(o: String): LiveData<List<Registro>>
    fun getDetalleIdentity(): LiveData<Int>
    fun updatePhoto(tipo: Int, name: String, detalleId: Int, id: Int): Completable
    fun getRegistroDetalle(tipo: Int, id: Int): LiveData<RegistroDetalle>
    fun getRegistroDetalleById(id: Int): LiveData<List<RegistroDetalle>>
    fun populateVehiculo(): LiveData<List<Vehiculo>>
    fun getVehiculo(placa: String): LiveData<Vehiculo>
    fun getControlVehiculo(placa: String): LiveData<List<VehiculoControl>>
    fun saveControl(v: VehiculoControl): Completable
    fun getControVehiculoById(controlId: Int): LiveData<VehiculoControl>
    fun getComboByTipo(tipo: Int): LiveData<List<ParametroT>>
    fun saveVales(c: VehiculoVales): Completable
    fun getValeVehiculo(placa: String): LiveData<List<VehiculoVales>>
    fun getValeVehiculoById(id: Int): LiveData<VehiculoVales>
    fun closeRegistroDetalle(
        registroId: Int,
        detalleId: Int,
        tipoDetalle: Int,
        tipo: Int
    ): Completable

    fun validarRegistro(registroId: Int): Observable<Int>
    fun updateVehiculo(messages: Mensaje): Completable
    fun closeVerificationVehiculo(placa: String): Observable<String>
    fun getEstados(): LiveData<List<Estado>>

    // TODO PARTE DIARIO

    fun saveParteDiario(body: RequestBody): Observable<Mensaje>
    fun getParteDiario(id: Int): Observable<ParteDiario>
    fun updateParteDiario(t: Mensaje): Completable
    fun getMaxIdParteDiario(): LiveData<ParteDiario>
    fun getParteDiarios(): LiveData<PagedList<ParteDiario>>
    fun getParteDiariosFecha(fecha: String): LiveData<PagedList<ParteDiario>>
    fun getParteDiariosSearch(f: String, s: String): LiveData<PagedList<ParteDiario>>
    fun getParteDiarioEstado(f: String, e: String): LiveData<PagedList<ParteDiario>>
    fun getParteDiariosComplete(
        f: String, e: String, s: String
    ): LiveData<PagedList<ParteDiario>>

    fun getParteDiarioPendiente(estado: String): LiveData<List<ParteDiario>>
    fun getParteDiarioById(id: Int): LiveData<ParteDiario>
    fun insertOrUpdateParteDiario(p: ParteDiario): Completable
    fun getRegistroMaterialTaskByFK(id: Int): LiveData<List<RegistroMaterial>>
    fun getRegistroBaremos(id: Int): LiveData<List<RegistroBaremo>>
    fun getRegistroPhotos(id: Int): LiveData<List<RegistroPhoto>>
    fun insertOrUpdateRegistroBaremo(b: RegistroBaremo): Observable<Boolean>
    fun deleteRegistroBaremo(b: RegistroBaremo): Completable
    fun insertRegistroMaterial(m: RegistroMaterial): Observable<Boolean>
    fun updateRegistroMaterial(m: RegistroMaterial): Observable<Boolean>
    fun deleteRegistroMaterial(m: RegistroMaterial): Completable
    fun deleteRegistroPhoto(p: RegistroPhoto): Completable
    fun insertOrUpdatePhoto(p: RegistroPhoto): Completable

    // TODO LOS DIALOGS

    fun getBaremosById(id: Int): LiveData<List<Baremo>>
    fun getActividad(): LiveData<List<Actividad>>
    fun getMateriales(o: String, a: String): LiveData<List<Material>>
    fun getArticuloByTipo(t: Int): LiveData<List<Articulo>>
    fun getAreas(): LiveData<List<Area>>
    fun getCentroCostos(): LiveData<List<CentroCostos>>
    fun getCuadrillaByFK(id: String): LiveData<List<Cuadrilla>>
    fun getSucursal(): LiveData<List<Sucursal>>

    // todo obras

    fun getObras(): LiveData<List<Obra>>
    fun getAlmacenEdelnor(): LiveData<List<Almacen>>
    fun getMedidor(): LiveData<List<Medidor>>
    fun getAlmacenByTipo(tipoMaterialSolicitud: Int): LiveData<List<Almacen>>
    fun getCoordinador(): LiveData<List<Coordinador>>

    // todo engie sync

    fun getSyncComplete(q: Query): Observable<Sync>
    fun updateUsuario(u: Usuario): Completable

    // todo solicitud

    fun insertSolicitud(s: List<Solicitud>): Completable
    fun getMaxIdSolicitud(): LiveData<Solicitud>
    fun getSolicitudByFilter(filtro: Int): LiveData<List<Solicitud>>
    fun getSolicitudById(id: Int): LiveData<Solicitud>
    fun insertOrUpdateSolicitud(s: Solicitud, t: Mensaje): Completable
    fun sendRegistroSolicitud(s: Solicitud): Observable<Mensaje>
    fun getPersonal(): LiveData<List<Personal>>
    fun getTipoDevolucion(): LiveData<List<TipoDevolucion>>
    fun sendAprobation(q: Query): Observable<Mensaje>
    fun updateSolicitudEstado(id: Int): Completable
    fun getRegistroMaterialByFK(id: Int): LiveData<List<RegistroSolicitudMaterial>>
    fun exitRegistroSolicitudMaterial(m: RegistroSolicitudMaterial): Observable<Boolean>
    fun sendRegistroSolicitudMaterial(s: RegistroSolicitudMaterial): Observable<Mensaje>
    fun insertOrUpdateRegistroSolicitudMaterial(
        m: RegistroSolicitudMaterial,
        t: Mensaje
    ): Completable

    fun getSolicitudRegistroPhotosByFK(id: Int): LiveData<List<RegistroSolicitudPhoto>>
    fun deleteRegistroSolicitudPhoto(p: RegistroSolicitudPhoto): Completable
    fun sendRegistroSolicitudPhoto(body: RequestBody): Observable<Mensaje>
    fun insertOrUpdateSolicitudPhoto(p: RegistroSolicitudPhoto, t: Mensaje): Completable
    fun paginationSolicitud(body: RequestBody): Flowable<List<Solicitud>>

    // todo Material

    fun paginationStockMaterial(body: RequestBody): Flowable<List<Material>>

    // TODO Logistica
    // pedido
    fun getSyncPedido(u: String): Observable<List<Pedido>>
    fun insertPedido(t: List<Pedido>): Completable
    fun getPedidoGroup(): LiveData<List<Pedido>>
    fun getPedidoGroup(e: String): LiveData<List<Pedido>>
    fun getPedidoGroupOne(codigo: String): LiveData<Pedido>
    fun getPedidoByCodigo(codigo: String): LiveData<List<Pedido>>
    fun sendUpdateCantidadPedido(q: Query, tipo: Int): Observable<Mensaje>
    fun updateCantidadPedido(id: Int, cantidad: Double, tipo: Int): Completable

    //orden
    fun getSyncOrden(q: Query): Observable<List<Orden>>
    fun insertOrden(t: List<Orden>): Completable
    fun getOrdenGroup(): LiveData<List<Orden>>
    fun getOrdenGroup(e: String): LiveData<List<Orden>>
    fun getOrdenGroupOne(codigo: String): LiveData<Orden>
    fun getOrdenByCodigo(codigo: String): LiveData<List<Orden>>
    fun getOrdenDetalleByCodigo(articulo: String): LiveData<List<OrdenDetalle>>

    //anulacion
    fun getSyncAnulacion(u: String, fi: String, ff: String): Observable<List<Anulacion>>
    fun insertAnulacion(t: List<Anulacion>): Completable
    fun getAnulacionGroup(): LiveData<List<Anulacion>>
    fun getAnulacionGroupOne(codigo: String): LiveData<Anulacion>
    fun getAnulacionByCodigo(codigo: String): LiveData<List<Anulacion>>
    fun sendAnulacion(q: Query): Observable<Mensaje>
    fun updateAnulacion(tipo: Int, id: Int): Completable

    //ambos pedido y orden
    fun sendUpdateAprobacionOrRechazo(q: Query): Observable<Mensaje>
    fun sendAprobacionPedido(q: Query): Observable<Mensaje>
    fun sendAprobacionCampoJefeTiempoVida(q: Query): Observable<Mensaje>
    fun updateAprobacionOrRechazo(tipo: Int, id: Int): Completable
    fun getCombosEstados(): LiveData<List<ComboEstado>>


    fun insertRequerimiento(r: Requerimiento): Completable
    fun getRequerimientos(): LiveData<List<Requerimiento>>
    fun getRequerimientoById(id: Int): LiveData<Requerimiento>
    fun insertRequerimientoDetalle(r: RequerimientoDetalle): Completable
    fun getRequerimientoDetalleById(id: Int): LiveData<RequerimientoDetalle>
    fun getRequerimientoDetalles(id: Int): LiveData<List<RequerimientoDetalle>>
    fun getRequerimientoId(): LiveData<Int>
    fun getDelegacion(): LiveData<List<Delegacion>>
    fun deleteRequerimiento(r: Requerimiento): Completable

    fun getRequerimientoTask(): Observable<List<Requerimiento>>
    fun sendRequerimiento(body: RequestBody): Observable<Mensaje>
    fun updateEnabledRequerimiento(t: Mensaje): Completable

    fun getRequerimientoMaterial(codigo: String): LiveData<RequerimientoMaterial>
    fun deleteRequerimientoDetalle(d: RequerimientoDetalle): Completable
    fun getRequerimientoMateriales(): LiveData<List<RequerimientoMaterial>>
    fun getRequerimientoCentroCostos(): LiveData<List<RequerimientoCentroCosto>>
    fun getRequerimientoEstado(): LiveData<List<RequerimientoEstado>>
    fun getTipos(): LiveData<List<RequerimientoTipo>>
    fun getCampoJefes(): LiveData<List<CampoJefe>>
    fun getTiempoVida(): LiveData<List<TiempoVida>>
    fun getLocales(): LiveData<List<Local>>
    fun getAlmacenLogistica(codigo:String): LiveData<List<AlmacenLogistica>>
    fun getSyncCampoJefe(q: Query): Observable<List<CampoJefe>>
    fun getSyncTiempoVida(q: Query): Observable<List<TiempoVida>>
    fun insertCampoJefe(t: List<CampoJefe>): Completable
    fun insertTiempoVida(t: List<TiempoVida>): Completable
    fun getCampoJefeGroupOne(codigo: Int): LiveData<CampoJefe>
    fun getCampoJefeByCodigo(codigo: Int): LiveData<List<CampoJefe>>
    fun getCampoTiempoVidaOne(codigo: Int): LiveData<TiempoVida>
    fun getTiempoVidaByCodigo(codigo: Int): LiveData<List<TiempoVida>>
    fun getOrdenEstados(): LiveData<List<OrdenEstado>>
    fun getOrdenEstadoByOne(): LiveData<OrdenEstado>
    fun getClearOrden(): Completable
    fun getComboEstado(): LiveData<ComboEstado>
    fun getClearPedido(): Completable
    fun aprobarItemsCampoJefeTiempoVida(q: Query): Observable<Mensaje>
    fun updateItemsCampoJefeTiempoVida(formato: Int, codigo: Int): Completable
    fun clearCampoJefe(): Completable
    fun clearTiempoVida(): Completable
}
