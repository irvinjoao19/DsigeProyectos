package com.dsige.dsigeproyectos.data.module

import android.app.Application
import androidx.room.Room
import com.dsige.dsigeproyectos.data.local.AppDataBase
import com.dsige.dsigeproyectos.data.local.dao.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataBaseModule {

    @Provides
    @Singleton
    internal fun provideRoomDatabase(application: Application): AppDataBase {
        if (AppDataBase.INSTANCE == null) {
            synchronized(AppDataBase::class.java) {
                if (AppDataBase.INSTANCE == null) {
                    AppDataBase.INSTANCE = Room.databaseBuilder(
                        application.applicationContext,
                        AppDataBase::class.java, AppDataBase.DB_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
        }
        return AppDataBase.INSTANCE!!
    }

    @Provides
    internal fun provideUsuarioDao(appDataBase: AppDataBase): UsuarioDao {
        return appDataBase.usuarioDao()
    }

    @Provides
    internal fun provideParametroTDao(appDataBase: AppDataBase): ParametroTDao {
        return appDataBase.parametroTDao()
    }

    @Provides
    internal fun provideParametroEDao(appDataBase: AppDataBase): ParametroEDao {
        return appDataBase.parametroEDao()
    }

    @Provides
    internal fun provideRegistroDao(appDataBase: AppDataBase): RegistroDao {
        return appDataBase.registroDao()
    }

    @Provides
    internal fun provideRegistroDetalleDao(appDataBase: AppDataBase): RegistroDetalleDao {
        return appDataBase.registroDetalleDao()
    }

    @Provides
    internal fun provideVehiculoDao(appDataBase: AppDataBase): VehiculoDao {
        return appDataBase.vehiculoDao()
    }

    @Provides
    internal fun provideVehiculoControlDao(appDataBase: AppDataBase): VehiculoControlDao {
        return appDataBase.vehiculoControlDao()
    }

    @Provides
    internal fun provideVehiculoValesDao(appDataBase: AppDataBase): VehiculoValesDao {
        return appDataBase.vehiculoValesDao()
    }

    @Provides
    internal fun provideParteDiarioDao(appDataBase: AppDataBase): ParteDiarioDao {
        return appDataBase.parteDiarioDao()
    }

    @Provides
    internal fun provideRegistroMaterialDao(appDataBase: AppDataBase): RegistroMaterialDao {
        return appDataBase.registroMaterialDao()
    }

    @Provides
    internal fun provideRegistroBaremoDao(appDataBase: AppDataBase): RegistroBaremoDao {
        return appDataBase.registroBaremoDao()
    }

    @Provides
    internal fun provideRegistroPhotoDao(appDataBase: AppDataBase): RegistroPhotoDao {
        return appDataBase.registroPhotoDao()
    }

    @Provides
    internal fun provideObraDao(appDataBase: AppDataBase): ObraDao {
        return appDataBase.obraDao()
    }

    @Provides
    internal fun provideMaterialDao(appDataBase: AppDataBase): MaterialDao {
        return appDataBase.materialDao()
    }

    @Provides
    internal fun provideBaremoDao(appDataBase: AppDataBase): BaremoDao {
        return appDataBase.baremoDao()
    }

    @Provides
    internal fun provideAreaDao(appDataBase: AppDataBase): AreaDao {
        return appDataBase.areaDao()
    }

    @Provides
    internal fun provideCentroCostoDao(appDataBase: AppDataBase): CentroCostosDao {
        return appDataBase.centroCostosDao()
    }

    @Provides
    internal fun provideEstadoDao(appDataBase: AppDataBase): EstadoDao {
        return appDataBase.estadoDao()
    }

    @Provides
    internal fun providePersonalDao(appDataBase: AppDataBase): PersonalDao {
        return appDataBase.personalDao()
    }

    @Provides
    internal fun provideSolicitudDao(appDataBase: AppDataBase): SolicitudDao {
        return appDataBase.solicitudDao()
    }

    @Provides
    internal fun provideCuadrillaDao(appDataBase: AppDataBase): CuadrillaDao {
        return appDataBase.cuadrillaDao()
    }

    @Provides
    internal fun provideRegistroSolicitudMaterialDao(appDataBase: AppDataBase): RegistroSolicitudMaterialDao {
        return appDataBase.registroSolicitudMaterialDao()
    }

    @Provides
    internal fun provideRegistroSolicitudPhotoDao(appDataBase: AppDataBase): RegistroSolicitudPhotoDao {
        return appDataBase.registroSolicitudPhotoDao()
    }

    @Provides
    internal fun provideSucursalDao(appDataBase: AppDataBase): SucursalDao {
        return appDataBase.sucursalDao()
    }

    @Provides
    internal fun provideAlmacenDao(appDataBase: AppDataBase): AlmacenDao {
        return appDataBase.almacenDao()
    }

    @Provides
    internal fun provideTipoDevolucionDao(appDataBase: AppDataBase): TipoDevolucionDao {
        return appDataBase.tipoDevolucionDao()
    }

    @Provides
    internal fun provideCoordinadorDao(appDataBase: AppDataBase): CoordinadorDao {
        return appDataBase.coordinadorDao()
    }

    @Provides
    internal fun provideActividadDao(appDataBase: AppDataBase): ActividadDao {
        return appDataBase.actividadDao()
    }

    @Provides
    internal fun provideMedidorDao(appDataBase: AppDataBase): MedidorDao {
        return appDataBase.medidorDao()
    }

    @Provides
    internal fun providePedidoDao(appDataBase: AppDataBase): PedidoDao {
        return appDataBase.pedidoDao()
    }

    @Provides
    internal fun provideOrdenDao(appDataBase: AppDataBase): OrdenDao {
        return appDataBase.ordenDao()
    }

    @Provides
    internal fun provideOrdenDetalleDao(appDataBase: AppDataBase): OrdenDetalleDao {
        return appDataBase.ordenDetalleDao()
    }

    @Provides
    internal fun provideRequerimientoDao(appDataBase: AppDataBase): RequerimientoDao {
        return appDataBase.requerimientoDao()
    }

    @Provides
    internal fun provideRequerimientoDetalleDao(appDataBase: AppDataBase): RequerimientoDetalleDao {
        return appDataBase.requerimientoDetalleDao()
    }

    @Provides
    internal fun provideDelegacionDao(appDataBase: AppDataBase): DelegacionDao {
        return appDataBase.delegacionDao()
    }

    @Provides
    internal fun provideRequerimientoMaterialDao(appDataBase: AppDataBase): RequerimientoMaterialDao {
        return appDataBase.requerimientoMaterialDao()
    }

    @Provides
    internal fun provideRequerimientoEstadoDao(appDataBase: AppDataBase): RequerimientoEstadoDao {
        return appDataBase.requerimientoEstadoDao()
    }

    @Provides
    internal fun provideRequerimientoTipoDao(appDataBase: AppDataBase): RequerimientoTipoDao {
        return appDataBase.requerimientoTipoDao()
    }

    @Provides
    internal fun provideRequerimientoCentroCostoDao(appDataBase: AppDataBase): RequerimientoCentroCostoDao {
        return appDataBase.requerimientoCentroCostoDao()
    }
}