package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.logistica.CampoJefe

@Dao
interface CampoJefeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCampoJefeTask(c: CampoJefe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCampoJefeListTask(c: List<CampoJefe>)

    @Update
    fun updateCampoJefeTask(vararg c: CampoJefe)

    @Delete
    fun deleteCampoJefeTask(c: CampoJefe)

    @Query("SELECT * FROM CampoJefe WHERE estadoId=1 GROUP BY id")
    fun getCampoJefes(): LiveData<List<CampoJefe>>

    @Query("SELECT * FROM CampoJefe")
    fun getCampoJefeTask(): List<CampoJefe>

    @Query("SELECT * FROM CampoJefe")
    fun getCampoJefe(): CampoJefe

    @Query("DELETE FROM CampoJefe")
    fun deleteAll()

    @Query("SELECT * FROM CampoJefe WHERE id=:c GROUP BY id")
    fun getCampoJefeGroupOne(c: Int): LiveData<CampoJefe>

    @Query("SELECT * FROM CampoJefe WHERE id=:c")
    fun getCampoJefeByCodigo(c: Int): LiveData<List<CampoJefe>>

    @Query("SELECT * FROM CampoJefe WHERE id=:c")
    fun getCampoJefeByCodigoTask(c: Int): List<CampoJefe>

    @Query("UPDATE CampoJefe SET estadoId = 0 WHERE id=:i")
    fun updateAprobacion(i: Int)

    @Query("UPDATE CampoJefe SET cantidadAprobada =:c WHERE id=:i")
    fun updateCantidad(i: Int, c: Double)

}