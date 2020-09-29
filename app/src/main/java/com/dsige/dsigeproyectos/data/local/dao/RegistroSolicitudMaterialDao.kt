package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.RegistroSolicitudMaterial

@Dao
interface RegistroSolicitudMaterialDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRegistroSolicitudMaterialTask(c: RegistroSolicitudMaterial)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRegistroSolicitudMaterialListTask(c: List<RegistroSolicitudMaterial>)

    @Update
    fun updateRegistroSolicitudMaterialTask(vararg c: RegistroSolicitudMaterial)

    @Delete
    fun deleteRegistroSolicitudMaterialTask(c: RegistroSolicitudMaterial)

    @Query("SELECT * FROM RegistroSolicitudMaterial WHERE solicitudId=:id")
    fun getRegistroSolicitudMaterialTaskByFK(id: Int): LiveData<List<RegistroSolicitudMaterial>>

    @Query("SELECT * FROM RegistroSolicitudMaterial")
    fun getRegistroSolicitudMaterial(): RegistroSolicitudMaterial

    @Query("SELECT * FROM RegistroSolicitudMaterial WHERE solicitudId =:id AND tipoMaterial=:tipo AND codigoMaterial=:codigo ")
    fun getValidateRegistroSolicitudMaterial(id: Int, tipo: Int, codigo: String): RegistroSolicitudMaterial

    @Query("DELETE FROM RegistroSolicitudMaterial")
    fun deleteAll()

    @Query("SELECT * FROM RegistroSolicitudMaterial ORDER BY registroMaterialId DESC LIMIT 1")
    fun getMaxIdRegistroSolicitudMaterial(): LiveData<RegistroSolicitudMaterial>

    @Query("UPDATE RegistroSolicitudMaterial SET identity=:retorno , tipo = 1 WHERE registroMaterialId=:base")
    fun updateRegistroSolicitud(base: Int, retorno: Int)

    @Query("DELETE FROM RegistroSolicitudMaterial WHERE registroMaterialId=:base")
    fun deleteRegistroSolicitud(base: Int)

    @Query("SELECT * FROM RegistroSolicitudMaterial WHERE identityDetalle =:id")
    fun exitRegistroMaterial(id: Int): Boolean

    @Query("UPDATE RegistroSolicitudMaterial SET estado = '106' WHERE solicitudId=:id")
    fun updateRegistroSolicitudEstado(id: Int)

    @Query("UPDATE RegistroSolicitudMaterial SET estado = '106' , cantidadAprobada =:aprobada  WHERE identityDetalle=:id")
    fun updateRegistroSolicitud(id: Int, aprobada: Double)

}