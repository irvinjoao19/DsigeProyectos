package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.Solicitud

@Dao
interface SolicitudDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSolicitudTask(c: Solicitud)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSolicitudListTask(c: List<Solicitud>)

    @Update
    fun updateSolicitudTask(vararg c: Solicitud)

    @Delete
    fun deleteSolicitudTask(c: Solicitud)

    @Query("SELECT * FROM Solicitud WHERE tipoMaterialSol=:m AND tipoSolicitudId=:s AND fechaAtencion=:f AND estadoSol=:e")
    fun getSolicitudByFilter(m: Int, s: Int, f: String, e: String): LiveData<List<Solicitud>>

    @Query("SELECT * FROM Solicitud")
    fun getSolicitud(): Solicitud

    @Query("SELECT * FROM Solicitud WHERE solicitudId =:id")
    fun getSolicitudById(id: Int): LiveData<Solicitud>

    @Query("SELECT * FROM Solicitud WHERE tipoMaterial =:filter")
    fun getSolicitudByFilter(filter: Int): LiveData<List<Solicitud>>

    @Query("DELETE FROM Solicitud")
    fun deleteAll()

    @Query("SELECT * FROM Solicitud ORDER BY solicitudId DESC LIMIT 1")
    fun getMaxIdSolicitud(): LiveData<Solicitud>

    @Query("UPDATE Solicitud SET identity=:retorno , tipo = 1 WHERE solicitudId=:base")
    fun updateSolicitud(base: Int, retorno: Int)

    @Query("UPDATE Solicitud SET pubEstadoCodigo =:estado WHERE solicitudId=:id")
    fun updateSolicitudEstado(id: Int, estado: String)

    @Query("SELECT * FROM Solicitud WHERE solicitudId =:id")
    fun exitSolicitud(id: Int): Boolean
}