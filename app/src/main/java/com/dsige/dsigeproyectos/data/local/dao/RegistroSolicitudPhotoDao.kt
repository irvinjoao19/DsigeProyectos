package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.RegistroSolicitudMaterial
import com.dsige.dsigeproyectos.data.local.model.engie.RegistroSolicitudPhoto

@Dao
interface RegistroSolicitudPhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRegistroSolicitudPhotoTask(c: RegistroSolicitudPhoto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRegistroSolicitudPhotoListTask(c: List<RegistroSolicitudPhoto>)

    @Update
    fun updateRegistroSolicitudPhotoTask(vararg c: RegistroSolicitudPhoto)

    @Delete
    fun deleteRegistroSolicitudPhotoTask(c: RegistroSolicitudPhoto)

    @Query("SELECT * FROM RegistroSolicitudPhoto WHERE solicitudId =:id")
    fun getRegistroSolicitudPhotoTaskByFK(id: Int): LiveData<List<RegistroSolicitudPhoto>>

    @Query("SELECT * FROM RegistroSolicitudPhoto")
    fun getRegistroSolicitudPhoto(): RegistroSolicitudPhoto

    @Query("SELECT * FROM RegistroSolicitudMaterial WHERE almacenId=:almacen AND codigoMaterial=:codigo AND solicitudId=:id")
    fun getValidateRegistroSolicitudMaterial(almacen: String, codigo: String, id: Int): RegistroSolicitudMaterial

    @Query("SELECT * FROM RegistroSolicitudMaterial WHERE almacenId=:almacen AND codigoMaterial=:codigo AND solicitudId=:id AND guiaSalida=:guia")
    fun getValidateRegistroSolicitudMaterialDevolucion(
        almacen: String,
        codigo: String,
        id: Int,
        guia: String
    ): RegistroSolicitudMaterial


    @Query("DELETE FROM RegistroSolicitudPhoto")
    fun deleteAll()

    @Query("UPDATE RegistroSolicitudPhoto SET identityFoto=:retorno , tipo = 1 WHERE registroPhotoId=:base")
    fun updateSolicitudPhoto(base: Int, retorno: Int)

    @Query("SELECT * FROM RegistroSolicitudPhoto ORDER BY registroPhotoId DESC LIMIT 1")
    fun getMaxIdRegistroSolicitudPhoto(): LiveData<RegistroSolicitudPhoto>

    @Query("SELECT * FROM RegistroSolicitudPhoto WHERE identityFoto =:id")
    fun exitRegistroPhoto(id: Int): Boolean
}