package com.dsige.dsigeproyectos.data.local.repository

import com.dsige.dsigeproyectos.helper.Mensaje
import com.dsige.dsigeproyectos.data.local.model.*
import com.dsige.dsigeproyectos.data.local.model.engie.*
import com.dsige.dsigeproyectos.data.local.model.logistica.*
import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Query

interface ApiService {

    @Headers("Cache-Control: no-cache")
    @POST("Login")
    fun getLogin(@Body body: RequestBody): Observable<Usuario>

    @Headers("Cache-Control: no-cache")
    @POST("SaveRegistro")
    fun save(@Body body: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("SaveVehiculo")
    fun saveVehiculo(@Body body: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("Verification")
    fun verification(@Body body: RequestBody): Observable<String>

    @Headers("Cache-Control: no-cache")
    @POST("SaveGps")
    fun saveGps(@Body body: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("SaveGps")
    fun saveGpsTask(@Body body: RequestBody): Call<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("SaveMovil")
    fun saveMovil(@Body body: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("SaveMovil")
    fun saveMovilTask(@Body body: RequestBody): Call<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("SaveInspeccionAsync")
    fun saveInspection(@Body body: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("SaveInspeccionDetalle")
    fun saveInspeccionDetalle(@Body body: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @GET("VerificateInspecciones")
    fun verificateInspecciones(
        @Query("operarioId") operarioId: Int,
        @Query("fecha") fecha: String
    ): Observable<Mensaje>

    // parte diario

    @Headers("Cache-Control: no-cache")
    @POST("SaveParteDiario")
    fun saveParteDiario(@Body query: RequestBody): Observable<Mensaje>

    // TODO SERVICES

    @POST("SaveEstadoMovil")
    fun saveEstadoMovil(@Body movil: RequestBody): Call<Mensaje>

    @POST("SaveOperarioGps")
    fun saveOperarioGps(@Body gps: RequestBody): Call<Mensaje>

    // TODO SOLICITUD

    @Headers("Cache-Control: no-cache")
    @POST("SaveGeneralSolicitud")
    fun saveRegistroSolicitud(@Body query: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("SaveRegistroSolicitudMaterial")
    fun saveRegistroSolicitudMaterial(@Body query: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("Aprobation")
    fun sendAprobation(@Body query: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("SaveRegistroPhoto")
    fun saveRegistroPhoto(@Body query: RequestBody): Observable<Mensaje>

    // TODO ENGIE

    @Headers("Cache-Control: no-cache")
    @POST("SyncComplete")
    fun getSyncComplete(@Body query: RequestBody): Observable<Sync>

    @Headers("Cache-Control: no-cache")
    @GET("Sync")
    fun getSync(): Observable<Sync>

    @Headers("Cache-Control: no-cache")
    @POST("GetPaginationSolicitud")
    fun getPaginationSolicitud(@Body query: RequestBody): Flowable<List<Solicitud>>

    @Headers("Cache-Control: no-cache")
    @POST("GetStockMaterial")
    fun getStockMaterial(@Body query: RequestBody): Flowable<List<Material>>

    // Logistica
    @Headers("Cache-Control: no-cache")
    @GET("PedidosCompra")
    fun getPedidosCompra(@Query("usuario") u: String): Observable<List<Pedido>>

    @Headers("Cache-Control: no-cache")
    @POST("OrdenCompra")
    fun getOrdenCompra(@Body query: RequestBody): Observable<List<Orden>>

    @Headers("Cache-Control: no-cache")
    @GET("GetAnulacion")
    fun getAnulacion(
        @Query("usuario") u: String,
        @Query("fi") fi: String,
        @Query("ff") ff: String
    ): Observable<List<Anulacion>>

    @Headers("Cache-Control: no-cache")
    @POST("SaveRequerimiento")
    fun saveRequerimiento(@Body query: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("UpdateCantidad")
    fun updateCantidad(@Body query: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("UpdateCantidadCampoJefeTiempoVida")
    fun UpdateCantidadCampoJefeTiempoVida(@Body query: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("AprobacionOrdenCompra")
    fun aprobacionOrdenCompra(@Body query: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("AprobacionPedido")
    fun aprobacionPedido(@Body query: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("AprobacionCampoJefeTiempoVida")
    fun aprobacionCampoJefeTiempoVida(@Body query: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("AnularEvento")
    fun sendAnulacion(@Body query: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("CampoJefe")
    fun getCampoJefe(@Body query: RequestBody): Observable<List<CampoJefe>>

    @Headers("Cache-Control: no-cache")
    @POST("TiempoVida")
    fun getTiempoVida(@Body query: RequestBody): Observable<List<TiempoVida>>

    @Headers("Cache-Control: no-cache")
    @POST("AprobarItemsCampoJefeTiempoVida")
    fun aprobarItemsCampoJefeTiempoVida(@Body body: RequestBody): Observable<Mensaje>
}