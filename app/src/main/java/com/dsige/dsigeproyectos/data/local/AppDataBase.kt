package com.dsige.dsigeproyectos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dsige.dsigeproyectos.data.local.dao.*
import com.dsige.dsigeproyectos.data.local.model.*
import com.dsige.dsigeproyectos.data.local.model.engie.*
import com.dsige.dsigeproyectos.data.local.model.logistica.*
import com.dsige.dsigeproyectos.data.local.model.trinidad.*

@Database(
    entities = [
        Usuario::class,
        Registro::class,
        RegistroDetalle::class,
        ParametroE::class,
        Vehiculo::class,
        VehiculoControl::class,
        VehiculoVales::class,
        Estado::class,
        ParametroT::class,
        Obra::class,
        Material::class,
        Cuadrilla::class,
        CentroCostos::class,
        Baremo::class,
        Articulo::class,
        Area::class,
        Sucursal::class,
        Almacen::class,
        Coordinador::class,
        Actividad::class,
        Medidor::class,
        ParteDiario::class,
        RegistroMaterial::class,
        RegistroPhoto::class,
        RegistroBaremo::class,
        Solicitud::class,
        RegistroSolicitudMaterial::class,
        RegistroSolicitudPhoto::class,
        Personal::class,
        TipoDevolucion::class,
        Pedido::class,
        Orden::class,
        OrdenDetalle::class,
        Requerimiento::class,
        RequerimientoDetalle::class,
        RequerimientoMaterial::class,
        Delegacion::class,
        RequerimientoEstado::class,
        RequerimientoTipo::class,
        RequerimientoCentroCosto::class
    ],
    version = 18        ,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun parametroEDao(): ParametroEDao
    abstract fun parametroTDao(): ParametroTDao
    abstract fun registroDao(): RegistroDao
    abstract fun registroDetalleDao(): RegistroDetalleDao
    abstract fun vehiculoDao(): VehiculoDao
    abstract fun vehiculoControlDao(): VehiculoControlDao
    abstract fun vehiculoValesDao(): VehiculoValesDao
    abstract fun estadoDao(): EstadoDao
    abstract fun parteDiarioDao(): ParteDiarioDao
    abstract fun registroMaterialDao(): RegistroMaterialDao
    abstract fun registroBaremoDao(): RegistroBaremoDao
    abstract fun registroPhotoDao(): RegistroPhotoDao

    abstract fun obraDao(): ObraDao
    abstract fun materialDao(): MaterialDao
    abstract fun articuloDao(): ArticuloDao
    abstract fun baremoDao(): BaremoDao
    abstract fun areaDao(): AreaDao
    abstract fun centroCostosDao(): CentroCostosDao


    abstract fun cuadrillaDao(): CuadrillaDao
    abstract fun almacenDao(): AlmacenDao
    abstract fun coordinadorDao(): CoordinadorDao
    abstract fun actividadDao(): ActividadDao
    abstract fun medidorDao(): MedidorDao
    abstract fun sucursalDao(): SucursalDao

    abstract fun personalDao(): PersonalDao
    abstract fun solicitudDao(): SolicitudDao
    abstract fun registroSolicitudMaterialDao(): RegistroSolicitudMaterialDao
    abstract fun registroSolicitudPhotoDao(): RegistroSolicitudPhotoDao
    abstract fun tipoDevolucionDao(): TipoDevolucionDao

    abstract fun pedidoDao(): PedidoDao
    abstract fun ordenDao(): OrdenDao
    abstract fun ordenDetalleDao(): OrdenDetalleDao
    abstract fun requerimientoDao(): RequerimientoDao
    abstract fun requerimientoDetalleDao(): RequerimientoDetalleDao
    abstract fun delegacionDao(): DelegacionDao
    abstract fun requerimientoMaterialDao(): RequerimientoMaterialDao

    abstract fun requerimientoEstadoDao(): RequerimientoEstadoDao
    abstract fun requerimientoTipoDao(): RequerimientoTipoDao
    abstract fun requerimientoCentroCostoDao(): RequerimientoCentroCostoDao

    companion object {
        @Volatile
        var INSTANCE: AppDataBase? = null
        val DB_NAME = "proyectos_db"
    }

    fun getDatabase(context: Context): AppDataBase {
        if (INSTANCE == null) {
            synchronized(AppDataBase::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java, "proyectos_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
        }
        return INSTANCE!!
    }
}