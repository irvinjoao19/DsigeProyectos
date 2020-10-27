package com.dsige.dsigeproyectos.data.local.repository

import android.content.Context
import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.dsige.dsigeproyectos.helper.Mensaje
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.data.local.AppDataBase
import com.dsige.dsigeproyectos.data.local.model.*
import com.dsige.dsigeproyectos.data.local.model.engie.*
import com.dsige.dsigeproyectos.data.local.model.logistica.*
import com.dsige.dsigeproyectos.data.local.model.trinidad.ParametroT
import com.dsige.dsigeproyectos.data.local.model.trinidad.*
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call

class AppRepoImp(private val apiService: ApiService, private val dataBase: AppDataBase) :
    AppRepository {

    override fun getUsuario(): LiveData<Usuario> {
        return dataBase.usuarioDao().getUsuario()
    }

    override fun getUsuarioId(): LiveData<String> {
        return dataBase.usuarioDao().getUsuarioId()
    }

    override fun getUsuarioIdTask(): String {
        return dataBase.usuarioDao().getUsuarioIdTask()
    }

    override fun getUsuarioService(
        usuario: String, password: String, imei: String, version: String
    ): Observable<Usuario> {
        val u = Query(usuario, password, imei, version)
        val json = Gson().toJson(u)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.getLogin(body)
    }

    override fun clearUsuario(): Completable {
        return Completable.fromAction {
            dataBase.usuarioDao().deleteAll()
            dataBase.sucursalDao().deleteAll()
            dataBase.areaDao().deleteAll()
            dataBase.centroCostosDao().deleteAll()
            dataBase.cuadrillaDao().deleteAll()
        }
    }

    override fun insertUsuario(u: Usuario): Completable {
        return Completable.fromAction {
            dataBase.usuarioDao().insertUsuarioTask(u)
            val f = u.filtro

            val s: List<Sucursal>? = f.sucursales
            if (s != null) {
                dataBase.sucursalDao().insertSucursalListTask(s)
            }

            val a: List<Area>? = f.areas
            if (a != null) {
                dataBase.areaDao().insertAreaListTask(a)
            }

            val c: List<CentroCostos>? = f.centros
            if (c != null) {
                dataBase.centroCostosDao().insertCentroCostosListTask(c)
                for (center: CentroCostos in c) {
                    val x: List<Cuadrilla>? = center.cuadrillas
                    if (x != null) {
                        dataBase.cuadrillaDao().insertCuadrillaListTask(x)
                    }
                }
            }
        }
    }

    override fun deleteUsuario(): Completable {
        return Completable.fromAction {
            dataBase.usuarioDao().deleteAll()
            dataBase.registroDao().deleteAll()
            dataBase.registroDetalleDao().deleteAll()
            dataBase.parametroTDao().deleteAll()
            dataBase.parametroEDao().deleteAll()
            dataBase.sucursalDao().deleteAll()
            dataBase.centroCostosDao().deleteAll()
            dataBase.areaDao().deleteAll()
            dataBase.almacenDao().deleteAll()

            dataBase.parteDiarioDao().deleteAll()
            dataBase.registroBaremoDao().deleteAll()
            dataBase.registroMaterialDao().deleteAll()
            dataBase.registroPhotoDao().deleteAll()
            dataBase.baremoDao().deleteAll()
            dataBase.materialDao().deleteAll()
            dataBase.obraDao().deleteAll()
            dataBase.estadoDao().deleteAll()
            dataBase.articuloDao().deleteAll()

            dataBase.coordinadorDao().deleteAll()
            dataBase.actividadDao().deleteAll()
            dataBase.medidorDao().deleteAll()
        }
    }

    override fun deleteTotal(context: Context): Completable {
        return Completable.fromAction {
            Util.deleteDirectory(context)
            dataBase.registroDao().deleteAll()
            dataBase.registroDetalleDao().deleteAll()
            dataBase.parametroTDao().deleteAll()
        }
    }

    override fun getSync(id: String, version: String): Observable<Sync> {
        return apiService.getSync()
    }

    override fun saveSync(s: Sync): Completable {
        return Completable.fromAction {
            val p: List<Vehiculo>? = s.vehiculos
            if (p != null) {
                dataBase.vehiculoDao().insertVehiculoListTask(p)
            }

            val c: List<ParametroT>? = s.parametrosT
            if (c != null) {
                dataBase.parametroTDao().insertParametroListTask(c)
            }

            val ce: List<ParametroE>? = s.parametrosE
            if (ce != null) {
                dataBase.parametroEDao().insertParametroListTask(ce)
            }

            val e: List<Estado>? = s.estados
            if (e != null) {
                dataBase.estadoDao().insertEstadoListTask(e)
            }

            val alm: List<Almacen>? = s.almacens
            if (alm != null) {
                dataBase.almacenDao().insertAlmacenListTask(alm)
            }

            val pD: List<ParteDiario>? = s.parteDiarios
            if (pD != null) {
                dataBase.parteDiarioDao().insertPartediarioListTask(pD)
                for (parteDiario: ParteDiario in pD) {
                    val ba: List<RegistroBaremo>? = parteDiario.baremos
                    if (ba != null) {
                        dataBase.registroBaremoDao().insertRegistroBaremoListTask(ba)
                    }

                    val ma: List<RegistroMaterial>? = parteDiario.materiales
                    if (ma != null) {
                        dataBase.registroMaterialDao().insertRegistroMaterialListTask(ma)
                    }

                    val photo: List<RegistroPhoto>? = parteDiario.photos
                    if (photo != null) {
                        dataBase.registroPhotoDao().insertRegistroPhotoListTask(photo)
                    }
                }
            }

            val b: List<Baremo>? = s.baremos
            if (b != null) {
                dataBase.baremoDao().insertBaremoListTask(b)
            }

            val m: List<Material>? = s.materiales
            if (m != null) {
                dataBase.materialDao().insertMaterialListTask(m)
            }

            val o: List<Obra>? = s.obras
            if (o != null) {
                dataBase.obraDao().insertObraListTask(o)
            }

            val a: List<Articulo>? = s.articulos
            if (a != null) {
                dataBase.articuloDao().insertArticuloListTask(a)
            }

            val co: List<Coordinador>? = s.coordinadors
            if (co != null) {
                dataBase.coordinadorDao().insertCoordinadorListTask(co)
            }

            val actividad: List<Actividad>? = s.actividades
            if (actividad != null) {
                dataBase.actividadDao().insertActividadListTask(actividad)
            }

            val medidor: List<Medidor>? = s.medidores
            if (medidor != null) {
                dataBase.medidorDao().insertMedidorListTask(medidor)
            }

            val personal: List<Personal>? = s.personals
            if (personal != null) {
                dataBase.personalDao().insertPersonalListTask(personal)
            }
            val de: List<TipoDevolucion>? = s.devoluciones
            if (de != null) {
                dataBase.tipoDevolucionDao().insertTipoDevolucionListTask(de)
            }
            val d: List<Delegacion>? = s.delegaciones
            if (d != null) {
                dataBase.delegacionDao().insertDelegacionListTask(d)
            }
            val re: List<RequerimientoMaterial>? = s.requerimientoMateriales
            if (re != null) {
                dataBase.requerimientoMaterialDao().insertRequerimientoMaterialListTask(re)
            }
            val rE: List<RequerimientoEstado>? = s.requerimientoEstado
            if (rE != null) {
                dataBase.requerimientoEstadoDao().insertRequerimientoEstadoListTask(rE)
            }
            val rt: List<RequerimientoTipo>? = s.requerimientoTipo
            if (rt != null) {
                dataBase.requerimientoTipoDao().insertRequerimientoTipoListTask(rt)
            }
            val rc: List<RequerimientoCentroCosto>? = s.requerimientoCentroCostos
            if (rc != null) {
                dataBase.requerimientoCentroCostoDao().insertRequerimientoCentroCostoListTask(rc)
            }
            val c1: List<ComboEstado>? = s.comboEstados
            if (c1 != null) {
                dataBase.comboEstadoDao().insertComboEstadoListTask(c1)
            }
        }
    }

    override fun getSizeRegistro(): LiveData<Int> {
        return dataBase.registroDao().getSizeRegistro()
    }

    override fun saveData(body: RequestBody): Observable<Mensaje> {
        return apiService.save(body)
    }

    override fun saveVehiculo(body: RequestBody): Observable<Mensaje> {
        return apiService.saveVehiculo(body)
    }

    override fun verification(body: RequestBody): Observable<String> {
        return apiService.verification(body)
    }

    override fun verificateInspecciones(id: Int, fecha: String): Observable<Mensaje> {
        return apiService.verificateInspecciones(id, fecha)
    }

    override fun saveInspection(body: RequestBody): Observable<Mensaje> {
        return apiService.saveInspection(body)
    }

    override fun saveInspectionDetail(body: RequestBody): Observable<Mensaje> {
        return apiService.saveInspeccionDetalle(body)
    }

    override fun saveGps(body: RequestBody): Observable<Mensaje> {
        return apiService.saveGps(body)
    }

    override fun saveGpsTask(body: RequestBody): Call<Mensaje> {
        return apiService.saveGpsTask(body)
    }

    override fun saveMovil(body: RequestBody): Observable<Mensaje> {
        return apiService.saveMovil(body)
    }

    override fun saveMovilTask(body: RequestBody): Call<Mensaje> {
        return apiService.saveMovilTask(body)
    }

    override fun updateRegistro(m: Mensaje): Completable {
        return Completable.fromAction {
            dataBase.registroDao().updateRegistroEstado(m.codigoBase, m.codigoRetorno, "067")
//            dataBase.registroDetalleDao().updateRegistroPhotoEstado(m.codigoBase)
        }
    }

    /**
     * estado = 1 -> activos por enviar
     * 0 -> faltantes
     */
    override fun getDataRegistro(estado: Int): Observable<List<Registro>> {
        return Observable.create { emitter ->
            val v: List<Registro> = dataBase.registroDao().getAllRegistroTask(estado)
            if (v.isNotEmpty()) {
                val list: ArrayList<Registro> = ArrayList()
                for (r: Registro in v) {
                    val detalle =
                        dataBase.registroDetalleDao().getAllRegistroDetalleTask(r.registroId)
                    r.list = detalle
                    list.add(r)
                }
                emitter.onNext(list)
            } else
                emitter.onError(Throwable("No hay datos disponibles por enviar"))

            emitter.onComplete()
        }
    }

    override fun getDataVehiculo(estado: Int): Observable<List<Vehiculo>> {
        return Observable.create { emitter ->
            val v: List<Vehiculo> = dataBase.vehiculoDao().getAllVehiculoTask(estado)
            if (v.isNotEmpty()) {
                val list: ArrayList<Vehiculo> = ArrayList()
                for (c: Vehiculo in v) {
                    val control: List<VehiculoControl> =
                        dataBase.vehiculoControlDao().getVehiculoControlTaskById(c.placa)
                    c.control = control
                    val vale: List<VehiculoVales> =
                        dataBase.vehiculoValesDao().getVehiculoValeTaskById(c.placa)
                    c.registros = vale
                    list.add(c)
                }
                emitter.onNext(list)
            } else
                emitter.onError(Throwable("No hay datos disponibles por enviar"))

            emitter.onComplete()
        }
    }

    override fun insertOrUpdateRegistro(r: Registro, id: Int): Completable {
        return Completable.fromAction {
            if (r.registroId == 0) {
                if (id == 0) {
                    dataBase.registroDao().insertRegistroTask(r)
                    val identity = dataBase.registroDao().getTaskIdentity()
                    r.detalles!!.registroId = identity
                    dataBase.registroDetalleDao().insertRegistroPhotoTask(r.detalles!!)
                } else {
                    dataBase.registroDetalleDao()
                        .updateObservacion(r.detalles!!.detalleId, r.detalles!!.observacion)
                }
            } else {
                if (r.tipo == 1) {
                    if (id == 0) {
                        r.detalles!!.registroId = r.registroId
                        dataBase.registroDetalleDao().insertRegistroPhotoTask(r.detalles!!)
                    }
                }
            }
        }
    }

    override fun insertOrUpdateRegistroPhoto(p: RegistroDetalle): Completable {
        return Completable.fromAction {
            if (p.detalleId == 0) {
                dataBase.registroDetalleDao().insertRegistroPhotoTask(p)
            } else {
                dataBase.registroDetalleDao().updateRegistroPhotoTask(p)
            }
        }
    }

    override fun getIdentity(): LiveData<Int> {
        return dataBase.registroDao().getIdentity()
    }

    override fun getRegistroPhotoById(id: Int): LiveData<PagedList<RegistroDetalle>> {
        return dataBase.registroDetalleDao().getRegistroPhotoPaging(id).toLiveData(
            Config(pageSize = 20, enablePlaceholders = true)
        )
    }

    override fun getRegistroById(id: Int): LiveData<Registro> {
        return dataBase.registroDao().getRegistroById(id)
    }

    override fun deleteGaleria(d: MenuPrincipal, c: Context): Completable {
        return Completable.fromAction {
            Util.deleteImage(d.title, c)
            val a = dataBase.registroDetalleDao().getRegistroDetalleById(d.id)
            when (d.image) {
                1 -> a.foto1PuntoAntes = ""
                2 -> a.foto2PuntoAntes = ""
                3 -> a.foto3PuntoAntes = ""
                4 -> a.foto1PuntoDespues = ""
                5 -> a.foto2PuntoDespues = ""
                6 -> a.foto3PuntoDespues = ""
            }
            dataBase.registroDetalleDao().updateRegistroPhotoTask(a)
        }
    }

    override fun getRegistroByTipo(tipo: Int): LiveData<List<Registro>> {
        return dataBase.registroDao().getRegistroByTipo(tipo)
    }

    override fun getRegistroPagingByTipo(tipo: Int): LiveData<PagedList<Registro>> {
        return dataBase.registroDao().getRegistroPagingByTipo(tipo).toLiveData(
            Config(pageSize = 20, enablePlaceholders = true)
        )
    }

    override fun getRegistroPagingByTipo(tipo: Int, s: String): LiveData<PagedList<Registro>> {
        return dataBase.registroDao().getRegistroPagingByTipo(tipo, s).toLiveData(
            Config(
                pageSize = 20,
                enablePlaceholders = true
            )
        )
    }

    override fun getRegistroByObra(o: String): LiveData<List<Registro>> {
        return dataBase.registroDao().getRegistroByObra(o)
    }

    override fun getDetalleIdentity(): LiveData<Int> {
        return dataBase.registroDetalleDao().getIdentity()
    }

    override fun updatePhoto(tipo: Int, name: String, detalleId: Int, id: Int): Completable {
        return Completable.fromAction {
            when (tipo) {
                1 -> {
                    val d: RegistroDetalle =
                        dataBase.registroDetalleDao().getRegistroDetalleById(detalleId)
                    when {
                        d.foto1PuntoAntes.isEmpty() -> d.foto1PuntoAntes = name
                        d.foto2PuntoAntes.isEmpty() -> d.foto2PuntoAntes = name
                        else -> d.foto3PuntoAntes = name
                    }
                    dataBase.registroDetalleDao().updateRegistroPhotoTask(d)
                }
                2 -> {
                    val d: RegistroDetalle =
                        dataBase.registroDetalleDao().getRegistroDetalleById(detalleId)
                    when {
                        d.foto1PuntoDespues.isEmpty() -> d.foto1PuntoDespues = name
                        d.foto2PuntoDespues.isEmpty() -> d.foto2PuntoDespues = name
                        else -> d.foto3PuntoDespues = name
                    }
                    dataBase.registroDetalleDao().updateRegistroPhotoTask(d)
                }
                else -> dataBase.registroDao().updatePhoto(id, name)
            }
        }
    }

    override fun getRegistroDetalle(tipo: Int, id: Int): LiveData<RegistroDetalle> {
        return if (tipo == 1) {
            dataBase.registroDetalleDao().getRegistroDetalle(id)
        } else {
            dataBase.registroDetalleDao().getRegistroDetalleFk(id)
        }
    }

    override fun getRegistroDetalleById(id: Int): LiveData<List<RegistroDetalle>> {
        return dataBase.registroDetalleDao().getRegistroDetalleByRegistroId(id)
    }

    override fun populateVehiculo(): LiveData<List<Vehiculo>> {
        return dataBase.vehiculoDao().getVehiculo()
    }

    override fun getVehiculo(placa: String): LiveData<Vehiculo> {
        return dataBase.vehiculoDao().getVehiculoById(placa)
    }

    override fun getControlVehiculo(placa: String): LiveData<List<VehiculoControl>> {
        return dataBase.vehiculoControlDao().getControlVehiculo(placa)
    }

    override fun saveControl(v: VehiculoControl): Completable {
        return Completable.fromAction {
            if (v.controlId == 0) {
                dataBase.vehiculoControlDao().insertVehiculoControlTask(v)
            } else {
                v.estado = 1
                dataBase.vehiculoControlDao().updateVehiculoControlTask(v)
            }

        }
    }

    override fun getControVehiculoById(controlId: Int): LiveData<VehiculoControl> {
        return dataBase.vehiculoControlDao().getControVehiculoById(controlId)
    }

    override fun getComboByTipo(tipo: Int): LiveData<List<ParametroT>> {
        return dataBase.parametroTDao().getComboByTipo(tipo)
    }

    override fun saveVales(c: VehiculoVales): Completable {
        return Completable.fromAction {
            if (c.valeId == 0) {
                dataBase.vehiculoValesDao().insertVehiculoValesTask(c)
            } else
                dataBase.vehiculoValesDao().updateVehiculoValesTask(c)
        }
    }

    override fun getValeVehiculo(placa: String): LiveData<List<VehiculoVales>> {
        return dataBase.vehiculoValesDao().getValeVehiculo(placa)
    }

    override fun getValeVehiculoById(id: Int): LiveData<VehiculoVales> {
        return dataBase.vehiculoValesDao().getValeVehiculoById(id)
    }

    override fun closeRegistroDetalle(
        registroId: Int, detalleId: Int, tipoDetalle: Int, tipo: Int
    ): Completable {
        return Completable.fromAction {
            when (tipo) {
                1 -> when (tipoDetalle) {
                    1 -> dataBase.registroDetalleDao().closeRegistroDetalle1(detalleId)
                    2 -> dataBase.registroDetalleDao().closeRegistroDetalle2(detalleId)
                }
                2 -> {
                    dataBase.registroDetalleDao().closeRegistroDetalle1(detalleId)
                    dataBase.registroDao().closeRegistro(registroId, "066")
                }
            }
        }
    }

    override fun validarRegistro(registroId: Int): Observable<Int> {
        return Observable.create { emitter ->
            val v: List<RegistroDetalle>? =
                dataBase.registroDetalleDao().getAllRegistroDetalleTask(registroId)
            if (v != null) {
                val size = v.size
                var count = 0
                for (r: RegistroDetalle in v) {
                    if (r.active1 == 1 && r.active2 == 1) {
                        count++
                    }
                }
                if (size == count) {
                    emitter.onNext(1)
                } else
                    emitter.onNext(2)
            } else {
                emitter.onNext(3)
            }
            emitter.onComplete()
        }
    }

    override fun updateVehiculo(messages: Mensaje): Completable {
        return Completable.fromAction {
            dataBase.vehiculoDao().updateEnabledVehiculo(2)
            dataBase.vehiculoValesDao().updateEnabledVale(0)
            dataBase.vehiculoControlDao().updateEnabledControl(0)
        }
    }

    override fun closeVerificationVehiculo(placa: String): Observable<String> {
        return Observable.create { emitter ->
            val va: List<VehiculoVales> = dataBase.vehiculoValesDao().getVehiculoValeTaskById(placa)
            if (va.isEmpty()) {
                emitter.onError(Throwable("No hay registros en Combustibles"))
                emitter.onComplete()
                return@create
            }
            val v: List<VehiculoControl> =
                dataBase.vehiculoControlDao().getVehiculoControlTaskById(placa)
            if (v.isNotEmpty()) {
                var count = 0
                val size = v.size
                for (r: VehiculoControl in v) {
                    if (r.estado == 1) {
                        count++
                    }
                }
                if (size == count) {
                    dataBase.vehiculoDao().updateEnabledVehiculoByPlaca(placa)
                    emitter.onNext("Cerrado")
                    emitter.onComplete()
                    return@create
                } else {
                    emitter.onError(Throwable("Cerrar Kilometraje"))
                    emitter.onComplete()
                    return@create
                }
            } else {
                emitter.onError(Throwable("No hay registros en Kilometraje"))
                emitter.onComplete()
                return@create
            }

        }
    }

    override fun getEstados(): LiveData<List<Estado>> {
        return dataBase.estadoDao().getEstado()
    }

    override fun saveParteDiario(body: RequestBody): Observable<Mensaje> {
        return apiService.saveParteDiario(body)
    }

    override fun getParteDiario(id: Int): Observable<ParteDiario> {
        return Observable.create { emitter ->
            val p: ParteDiario = dataBase.parteDiarioDao().getParteDiarioTaskById(id)
            val b: List<RegistroBaremo>? =
                dataBase.registroBaremoDao().getRegistroBaremoTaskByFK(id)
            if (b != null) {
                p.baremos = b
            }
            val m: List<RegistroMaterial>? =
                dataBase.registroMaterialDao().getRegistroMaterialTaskByFK(id)
            if (m != null) {
                p.materiales = m
            }
            val f: List<RegistroPhoto>? = dataBase.registroPhotoDao().getRegistroPhotoTaskByFK(id)
            if (f != null) {
                p.photos = f
            }
            emitter.onNext(p)
            emitter.onComplete()
        }
    }

    override fun updateParteDiario(t: Mensaje): Completable {
        return Completable.fromAction {
            dataBase.parteDiarioDao().updateParteDiario(t.codigoBase, t.codigoRetorno, "121")
            dataBase.registroBaremoDao().updateRegistroBaremo(t.codigoBase, t.codigoRetorno, "121")
            dataBase.registroMaterialDao()
                .updateRegistroMaterial(t.codigoBase, t.codigoRetorno, "121")
            dataBase.registroPhotoDao().updateRegistroPhoto(t.codigoBase, t.codigoRetorno)
        }
    }

    override fun getMaxIdParteDiario(): LiveData<ParteDiario> {
        return dataBase.parteDiarioDao().getMaxIdParteDiario()
    }

    override fun getParteDiarios(): LiveData<PagedList<ParteDiario>> {
        return dataBase.parteDiarioDao().getParteDiarios().toLiveData(
            Config(pageSize = 20, enablePlaceholders = true)
        )
    }

    override fun getParteDiariosFecha(fecha: String): LiveData<PagedList<ParteDiario>> {
        return dataBase.parteDiarioDao().getParteDiariosFecha(fecha).toLiveData(
            Config(pageSize = 20, enablePlaceholders = true)
        )
    }

    override fun getParteDiariosSearch(f: String, s: String): LiveData<PagedList<ParteDiario>> {
        return dataBase.parteDiarioDao().getParteDiariosSearch(f, s).toLiveData(
            Config(pageSize = 20, enablePlaceholders = true)
        )
    }

    override fun getParteDiarioEstado(f: String, e: String): LiveData<PagedList<ParteDiario>> {
        return dataBase.parteDiarioDao().getParteDiarioEstado(f, e).toLiveData(
            Config(pageSize = 20, enablePlaceholders = true)
        )
    }

    override fun getParteDiariosComplete(
        f: String, e: String, s: String
    ): LiveData<PagedList<ParteDiario>> {
        return dataBase.parteDiarioDao().getParteDiariosComplete(f, e, s).toLiveData(
            Config(pageSize = 20, enablePlaceholders = true)
        )
    }

    override fun getParteDiarioPendiente(estado: String): LiveData<List<ParteDiario>> {
        return dataBase.parteDiarioDao().getParteDiarioPendiente("120")
    }


    override fun getParteDiarioById(id: Int): LiveData<ParteDiario> {
        return dataBase.parteDiarioDao().getParteDiarioById(id)
    }

    override fun insertOrUpdateParteDiario(p: ParteDiario): Completable {
        return Completable.fromAction {
            dataBase.parteDiarioDao().insertParteDiarioTask(p)
//            if (p.parteDiarioId == 0) {
//                p.parteDiarioId = 1
//                dataBase.parteDiarioDao().insertParteDiarioTask(p)
//            } else {
//                dataBase.parteDiarioDao().updateParteDiarioTask(p)
//            }
        }
    }

    override fun getRegistroMaterialTaskByFK(id: Int): LiveData<List<RegistroMaterial>> {
        return dataBase.registroMaterialDao().getRegistroMaterialByFK(id)
    }

    override fun getRegistroBaremos(id: Int): LiveData<List<RegistroBaremo>> {
        return dataBase.registroBaremoDao().getRegistroBaremoByFK(id)
    }

    override fun getRegistroPhotos(id: Int): LiveData<List<RegistroPhoto>> {
        return dataBase.registroPhotoDao().getRegistroPhotoByFK(id)
    }

    override fun insertOrUpdateRegistroBaremo(b: RegistroBaremo): Observable<Boolean> {
        return Observable.create { e ->
            if (b.registroBaremoId == 0) {
                val r: RegistroBaremo? =
                    dataBase.registroBaremoDao()
                        .getValidateRegistroBaremo(b.parteDiarioId, b.codigoBaremo)
                if (r != null) {
                    e.onNext(false)
                } else {
                    dataBase.registroBaremoDao().insertRegistroBaremoTask(b)
                    e.onNext(true)
                }
            } else {
                dataBase.registroBaremoDao().updateRegistroBaremoTask(b)
                e.onNext(true)
            }
            e.onComplete()
        }
    }

    override fun deleteRegistroBaremo(b: RegistroBaremo): Completable {
        return Completable.fromAction {
            dataBase.registroBaremoDao().deleteRegistroBaremoTask(b)
        }
    }

    override fun insertRegistroMaterial(m: RegistroMaterial): Observable<Boolean> {
        return Observable.create { e ->
            if (m.registroMaterialId == 0) {
                val r: RegistroMaterial? =
                    dataBase.registroMaterialDao()
                        .getValidateRegistroMaterial(
                            m.parteDiarioId,
                            m.codigoMaterial,
                            m.guiaSalida
                        )
                if (r != null) {
                    e.onNext(false)
                } else {
                    dataBase.registroMaterialDao().insertRegistroMaterialTask(m)
                    val ma = dataBase.materialDao().getMaterialById(m.materialId)
                    ma.stock = ma.stock - m.cantidadMovil
                    dataBase.materialDao().updateMaterialTask(ma)
                    e.onNext(true)
                }
            } else {
                dataBase.registroMaterialDao().updateRegistroMaterialTask(m)
//                val ma = dataBase.materialDao().getMaterialById(m.materialId)
//                ma.stock = ma.stock + m.stock
//                dataBase.materialDao().updateMaterialTask(ma)
                e.onNext(true)
            }
            e.onComplete()
        }
    }

    override fun updateRegistroMaterial(m: RegistroMaterial): Observable<Boolean> {
        return Observable.create { e ->
            val ma = dataBase.materialDao().getMaterialById(m.materialId)
            if (m.cantidadMovil > m.stock) {
                e.onNext(false)
            } else {
                if (m.cantidadMovil > m.cantidadOk) {
                    val subtotal = (m.cantidadMovil - m.cantidadOk)
                    val total = m.cantidadOk + subtotal
                    ma.stock = m.stock - total
                    m.cantidadOk = m.cantidadMovil
                } else {
                    val total = (m.cantidadOk - m.cantidadMovil)
                    ma.stock = ma.stock + total
                    m.cantidadOk = m.cantidadMovil
                }
                dataBase.registroMaterialDao().updateRegistroMaterialTask(m)
                dataBase.materialDao().updateMaterialTask(ma)
                e.onNext(true)
            }
            e.onComplete()
        }
    }

    override fun deleteRegistroMaterial(m: RegistroMaterial): Completable {
        return Completable.fromAction {
            val ma = dataBase.materialDao().getMaterialById(m.materialId)
            ma.stock = ma.stock + m.cantidadMovil
            dataBase.materialDao().updateMaterialTask(ma)
            dataBase.registroMaterialDao().deleteRegistroMaterialTask(m)
        }
    }

    override fun deleteRegistroPhoto(p: RegistroPhoto): Completable {
        return Completable.fromAction {
            dataBase.registroPhotoDao().deleteRegistroPhotoTask(p)
        }
    }

    override fun insertOrUpdatePhoto(p: RegistroPhoto): Completable {
        return Completable.fromAction {
            if (p.registroPhotoId == 0) {
                dataBase.registroPhotoDao().insertRegistroPhotoTask(p)
            } else {
                dataBase.registroPhotoDao().updateRegistroPhotoTask(p)
            }
        }
    }


    override fun getBaremosById(id: Int): LiveData<List<Baremo>> {
        return dataBase.baremoDao().getBaremosById(id)
    }

    override fun getActividad(): LiveData<List<Actividad>> {
        return dataBase.actividadDao().getActividades()
    }

    override fun getMateriales(o: String, a: String): LiveData<List<Material>> {
        return dataBase.materialDao().getMaterialesByObraAlmacen(o, a)
    }

    override fun getArticuloByTipo(t: Int): LiveData<List<Articulo>> {
        return dataBase.articuloDao().getArticuloByTipo(t)
    }

    override fun getAreas(): LiveData<List<Area>> {
        return dataBase.areaDao().getAreaTask()
    }

    override fun getCentroCostos(): LiveData<List<CentroCostos>> {
        return dataBase.centroCostosDao().getCentroCostosTask()
    }

    override fun getCuadrillaByFK(id: String): LiveData<List<Cuadrilla>> {
        return dataBase.cuadrillaDao().getCuadrillaByFK(id)
    }

    override fun getSucursal(): LiveData<List<Sucursal>> {
        return dataBase.sucursalDao().getSucursalTask()
    }

    override fun getObras(): LiveData<List<Obra>> {
        return dataBase.obraDao().getObraTask()
    }

    override fun getAlmacenEdelnor(): LiveData<List<Almacen>> {
        return dataBase.almacenDao().getAlmacenBySeraEdelnor()
    }

    override fun getMedidor(): LiveData<List<Medidor>> {
        return dataBase.medidorDao().getMedidor()
    }

    override fun getAlmacenByTipo(tipoMaterialSolicitud: Int): LiveData<List<Almacen>> {
        return if (tipoMaterialSolicitud == 1) {
            dataBase.almacenDao().getAlmacenBySeraEdelnor()
        } else {
            dataBase.almacenDao().getAlmacenByEquipamiento()
        }
    }

    override fun getCoordinador(): LiveData<List<Coordinador>> {
        return dataBase.coordinadorDao().getCoordinadorTask()
    }

    override fun getSyncComplete(q: Query): Observable<Sync> {
        val json = Gson().toJson(q)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.getSyncComplete(body)
    }

    override fun updateUsuario(u: Usuario): Completable {
        return Completable.fromAction {
            dataBase.usuarioDao().updateUsuarioTask(u)
        }
    }

    override fun insertSolicitud(s: List<Solicitud>): Completable {
        return Completable.fromAction {
            for (c: Solicitud in s) {
                if (!dataBase.solicitudDao().exitSolicitud(c.solicitudId)) {
                    dataBase.solicitudDao().insertSolicitudTask(c)
                } else {
                    dataBase.solicitudDao().updateSolicitudEstado(c.solicitudId, c.pubEstadoCodigo)
                }

                val d: List<RegistroSolicitudMaterial>? = c.materiales
                if (d != null) {
                    for (m: RegistroSolicitudMaterial in d) {
                        dataBase.registroSolicitudMaterialDao()
                            .insertRegistroSolicitudMaterialTask(m)
                    }
                }
                val p: List<RegistroSolicitudPhoto>? = c.photos
                if (p != null) {
                    for (f: RegistroSolicitudPhoto in p) {
                        if (!dataBase.registroSolicitudPhotoDao()
                                .exitRegistroPhoto(f.identityFoto)
                        ) {
                            dataBase.registroSolicitudPhotoDao().insertRegistroSolicitudPhotoTask(f)
                        }
                    }
                }
            }
        }
    }

    override fun getMaxIdSolicitud(): LiveData<Solicitud> {
        return dataBase.solicitudDao().getMaxIdSolicitud()
    }

    override fun getSolicitudByFilter(filtro: Int): LiveData<List<Solicitud>> {
        return dataBase.solicitudDao().getSolicitudByFilter(filtro)
    }

    override fun getSolicitudById(id: Int): LiveData<Solicitud> {
        return dataBase.solicitudDao().getSolicitudById(id)
    }

    override fun insertOrUpdateSolicitud(s: Solicitud, t: Mensaje): Completable {
        return Completable.fromAction {
            if (s.identity == 0) {
                s.identity = t.codigoRetorno
                s.tipo = 1
                dataBase.solicitudDao().insertSolicitudTask(s)
            } else {
                dataBase.solicitudDao().updateSolicitudTask(s)
            }
        }
    }

    override fun sendRegistroSolicitud(s: Solicitud): Observable<Mensaje> {
        val json = Gson().toJson(s)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.saveRegistroSolicitud(body)
    }

    override fun getPersonal(): LiveData<List<Personal>> {
        return dataBase.personalDao().getPersonalTask()
    }

    override fun getTipoDevolucion(): LiveData<List<TipoDevolucion>> {
        return dataBase.tipoDevolucionDao().getTipoDevolucionTask()
    }

    override fun sendAprobation(q: Query): Observable<Mensaje> {
        val json = Gson().toJson(q)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.sendAprobation(body)
    }

    override fun updateSolicitudEstado(id: Int): Completable {
        return Completable.fromAction {
            dataBase.solicitudDao().updateSolicitudEstado(id, "106")
            dataBase.registroSolicitudMaterialDao().updateRegistroSolicitudEstado(id)
        }
    }

    override fun getRegistroMaterialByFK(id: Int): LiveData<List<RegistroSolicitudMaterial>> {
        return dataBase.registroSolicitudMaterialDao().getRegistroSolicitudMaterialTaskByFK(id)
    }

    override fun exitRegistroSolicitudMaterial(m: RegistroSolicitudMaterial): Observable<Boolean> {
        return Observable.create { e ->
            if (m.tipoSolicitudId == 1) {
                val r: RegistroSolicitudMaterial? = dataBase.registroSolicitudPhotoDao()
                    .getValidateRegistroSolicitudMaterial(
                        m.almacenId,
                        m.codigoMaterial,
                        m.solicitudId
                    )
                if (r != null) {
                    e.onNext(false)
                } else {
                    e.onNext(true)
                }
            } else {
                val r: RegistroSolicitudMaterial? = dataBase.registroSolicitudPhotoDao()
                    .getValidateRegistroSolicitudMaterialDevolucion(
                        m.almacenId,
                        m.codigoMaterial,
                        m.solicitudId,
                        m.guiaSalida
                    )
                if (r != null) {
                    e.onNext(false)
                } else {
                    e.onNext(true)
                }
            }
            e.onComplete()
        }
    }

    override fun sendRegistroSolicitudMaterial(s: RegistroSolicitudMaterial): Observable<Mensaje> {
        val json = Gson().toJson(s)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.saveRegistroSolicitudMaterial(body)
    }

    override fun insertOrUpdateRegistroSolicitudMaterial(
        m: RegistroSolicitudMaterial,
        t: Mensaje
    ): Completable {
        return Completable.fromAction {
            if (m.tipo != 2) {
                if (m.registroMaterialId == 0) {
                    m.identityDetalle = t.codigoRetorno
                    m.tipo = 1
                    dataBase.registroSolicitudMaterialDao().insertRegistroSolicitudMaterialTask(m)
                } else {
                    dataBase.registroSolicitudMaterialDao().updateRegistroSolicitudMaterialTask(m)
                }
            } else {
                dataBase.registroSolicitudMaterialDao().deleteRegistroSolicitud(t.codigoBase)
            }
        }
    }

    override fun getSolicitudRegistroPhotosByFK(id: Int): LiveData<List<RegistroSolicitudPhoto>> {
        return dataBase.registroSolicitudPhotoDao().getRegistroSolicitudPhotoTaskByFK(id)
    }

    override fun deleteRegistroSolicitudPhoto(p: RegistroSolicitudPhoto): Completable {
        return Completable.fromAction {
            dataBase.registroSolicitudPhotoDao().deleteRegistroSolicitudPhotoTask(p)
        }
    }

    override fun sendRegistroSolicitudPhoto(body: RequestBody): Observable<Mensaje> {
        return apiService.saveRegistroPhoto(body)
    }

    override fun insertOrUpdateSolicitudPhoto(p: RegistroSolicitudPhoto, t: Mensaje): Completable {
        return Completable.fromAction {
            p.identityFoto = t.codigoRetorno
            p.tipo = 1
            if (p.registroPhotoId == 0) {
                dataBase.registroSolicitudPhotoDao().insertRegistroSolicitudPhotoTask(p)
            } else {
                dataBase.registroSolicitudPhotoDao().deleteRegistroSolicitudPhotoTask(p)
            }
        }
    }

    override fun paginationSolicitud(body: RequestBody): Flowable<List<Solicitud>> {
        return apiService.getPaginationSolicitud(body)
    }

    override fun paginationStockMaterial(body: RequestBody): Flowable<List<Material>> {
        return apiService.getStockMaterial(body)
    }

    override fun getSyncPedido(u: String): Observable<List<Pedido>> {
        return apiService.getPedidosCompra(u)
    }

    override fun insertPedido(t: List<Pedido>): Completable {
        return Completable.fromAction {
            dataBase.pedidoDao().insertPedidoListTask(t)
        }
    }

    override fun getPedidoGroup(): LiveData<List<Pedido>> {
        return dataBase.pedidoDao().getPedidoGroup()
    }

    override fun getPedidoGroup(e: String): LiveData<List<Pedido>> {
        return dataBase.pedidoDao().getPedidoGroup(e)
    }

    override fun getPedidoGroupOne(codigo: String): LiveData<Pedido> {
        return dataBase.pedidoDao().getPedidoGroupOne(codigo)
    }

    override fun getPedidoByCodigo(codigo: String): LiveData<List<Pedido>> {
        return dataBase.pedidoDao().getPedidoByCodigo(codigo)
    }

    override fun sendUpdateCantidadPedido(body: RequestBody): Observable<Mensaje> {
        return apiService.updateCantidad(body)
    }

    override fun updateCantidadPedido(id: Int, cantidad: Double): Completable {
        return Completable.fromAction {
            dataBase.pedidoDao().updateCantidad(id, cantidad)
        }
    }

    override fun getSyncOrden(u: String): Observable<List<Orden>> {
        return apiService.getOrdenCompra(u)
    }

    override fun insertOrden(t: List<Orden>): Completable {
        return Completable.fromAction {
            dataBase.ordenDetalleDao().deleteAll()

            dataBase.ordenDao().insertOrdenListTask(t)
            for (o: Orden in t) {
                val detalle: List<OrdenDetalle>? = o.detalles
                if (detalle != null) {
                    for (d: OrdenDetalle in detalle) {
                        dataBase.ordenDetalleDao().insertOrdenDetalleTask(d)
                    }
                }
            }

        }
    }

    override fun getOrdenGroup(): LiveData<List<Orden>> {
        return dataBase.ordenDao().getOrdenGroup()
    }

    override fun getOrdenGroup(e: String): LiveData<List<Orden>> {
        return dataBase.ordenDao().getOrdenGroup(e)
    }

    override fun getOrdenGroupOne(codigo: String): LiveData<Orden> {
        return dataBase.ordenDao().getOrdenGroupOne(codigo)
    }

    override fun getOrdenByCodigo(codigo: String): LiveData<List<Orden>> {
        return dataBase.ordenDao().getOrdenByCodigo(codigo)
    }

    override fun getOrdenDetalleByCodigo(articulo: String): LiveData<List<OrdenDetalle>> {
        return dataBase.ordenDetalleDao().getOrdenDetalleByCodigo(articulo)
    }

    override fun getSyncAnulacion(u: String, fi: String, ff: String): Observable<List<Anulacion>> {
        return apiService.getAnulacion(u, fi, ff)
    }

    override fun insertAnulacion(t: List<Anulacion>): Completable {
        return Completable.fromAction {
            dataBase.anulacionDao().deleteAll()
            dataBase.anulacionDao().insertAnulacionListTask(t)
        }
    }

    override fun getAnulacionGroup(): LiveData<List<Anulacion>> {
        return dataBase.anulacionDao().getAnulacionGroup()
    }

    override fun getAnulacionGroupOne(codigo: String): LiveData<Anulacion> {
        return dataBase.anulacionDao().getAnulacionGroupOne(codigo)
    }

    override fun getAnulacionByCodigo(codigo: String): LiveData<List<Anulacion>> {
        return dataBase.anulacionDao().getAnulacionByCodigo(codigo)
    }

    override fun sendAnulacion(body: RequestBody): Observable<Mensaje> {
        return apiService.sendAnulacion(body)
    }

    override fun updateAnulacion(tipo: Int, id: Int): Completable {
        return Completable.fromAction {
            dataBase.anulacionDao().updateAnulacion(id)
        }
    }

    override fun sendUpdateAprobacionOrRechazo(body: RequestBody): Observable<Mensaje> {
        return apiService.aprobacionOrdenCompra(body)
    }

    override fun updateAprobacionOrRechazo(tipo: Int, id: Int): Completable {
        return Completable.fromAction {
            if (tipo == 1) {
                dataBase.pedidoDao().updateAprobacion(id)
            } else {
                dataBase.ordenDao().updateAprobacion(id)
            }
        }
    }

    override fun getCombosEstados(): LiveData<List<ComboEstado>> {
        return dataBase.comboEstadoDao().getComboEstados()
    }

    override fun insertRequerimiento(r: Requerimiento): Completable {
        return Completable.fromAction {
            val a: Requerimiento? =
                dataBase.requerimientoDao().getRequerimientoTask(r.requerimientoId)
            if (a == null) {
                dataBase.requerimientoDao().insertRequerimientoTask(r)
            } else
                dataBase.requerimientoDao().updateRequerimientoTask(r)

        }
    }

    override fun getRequerimientos(): LiveData<List<Requerimiento>> {
        return dataBase.requerimientoDao().getRequerimientos()
    }

    override fun getRequerimientoById(id: Int): LiveData<Requerimiento> {
        return dataBase.requerimientoDao().getRequerimientoById(id)
    }

    override fun insertRequerimientoDetalle(r: RequerimientoDetalle): Completable {
        return Completable.fromAction {
            if (r.detalleId == 0)
                dataBase.requerimientoDetalleDao().insertRequerimientoDetalleTask(r)
            else
                dataBase.requerimientoDetalleDao().updateRequerimientoDetalleTask(r)
        }
    }

    override fun getRequerimientoDetalleById(id: Int): LiveData<RequerimientoDetalle> {
        return dataBase.requerimientoDetalleDao().getRequerimientoDetalleById(id)
    }

    override fun getRequerimientoDetalles(id: Int): LiveData<List<RequerimientoDetalle>> {
        return dataBase.requerimientoDetalleDao().getRequerimientoDetalles(id)
    }

    override fun getRequerimientoId(): LiveData<Int> {
        return dataBase.requerimientoDao().getMaxIdRequerimiento()
    }

    override fun getDelegacion(): LiveData<List<Delegacion>> {
        return dataBase.delegacionDao().getDelegacions()
    }

    override fun deleteRequerimiento(r: Requerimiento): Completable {
        return Completable.fromAction {
            dataBase.requerimientoDao().deleteRequerimientoTask(r)
        }
    }

    override fun getRequerimientoTask(): Observable<List<Requerimiento>> {
        return Observable.create { e ->
            val a: ArrayList<Requerimiento> = ArrayList()
            val data: List<Requerimiento> = dataBase.requerimientoDao().getRequerimientosTask()
            if (data.isEmpty()) {
                e.onError(Throwable("No hay datos por enviar"))
                e.onComplete()
                return@create
            }
            for (r: Requerimiento in data) {
                val detalle = dataBase.requerimientoDetalleDao()
                    .getRequerimientoDetalleTask(r.requerimientoId)
                r.detalle = detalle
                a.add(r)
            }
            e.onNext(a)
            e.onComplete()
        }
    }

    override fun sendRequerimiento(body: RequestBody): Observable<Mensaje> {
        return apiService.saveRequerimiento(body)
    }

    override fun updateEnabledRequerimiento(t: Mensaje): Completable {
        return Completable.fromAction {


        }
    }

    override fun getRequerimientoMaterial(codigo: String): LiveData<RequerimientoMaterial> {
        return dataBase.requerimientoMaterialDao().getRequerimientoMaterial(codigo)
    }

    override fun deleteRequerimientoDetalle(d: RequerimientoDetalle): Completable {
        return Completable.fromAction {
            dataBase.requerimientoDetalleDao().deleteRequerimientoDetalleTask(d)
        }
    }

    override fun getRequerimientoMateriales(): LiveData<List<RequerimientoMaterial>> {
        return dataBase.requerimientoMaterialDao().getRequerimientoMateriales()
    }

    override fun getRequerimientoCentroCostos(): LiveData<List<RequerimientoCentroCosto>> {
        return dataBase.requerimientoCentroCostoDao().getRequerimientoCentroCostos()
    }

    override fun getRequerimientoEstado(): LiveData<List<RequerimientoEstado>> {
        return dataBase.requerimientoEstadoDao().getRequerimientoEstados()
    }

    override fun getTipos(): LiveData<List<RequerimientoTipo>> {
        return dataBase.requerimientoTipoDao().getRequerimientoTipos()
    }
}