package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.Solicitud
import com.dsige.dsigeproyectos.data.local.model.logistica.Requerimiento

@Dao
interface RequerimientoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRequerimientoTask(c: Requerimiento)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRequerimientoListTask(c: List<Requerimiento>)

    @Update
    fun updateRequerimientoTask(vararg c: Requerimiento)

    @Delete
    fun deleteRequerimientoTask(c: Requerimiento)

    @Query("SELECT * FROM Requerimiento")
    fun getRequerimientos(): LiveData<List<Requerimiento>>

    @Query("SELECT * FROM Requerimiento WHERE requerimientoId=:id")
    fun getRequerimientoTask(id:Int): Requerimiento

    @Query("SELECT * FROM Requerimiento")
    fun getRequerimiento(): Requerimiento

    @Query("DELETE FROM Requerimiento")
    fun deleteAll()

    @Query("SELECT * FROM Requerimiento WHERE requerimientoId =:id")
    fun getRequerimientoById(id: Int): LiveData<Requerimiento>

    @Query("SELECT requerimientoId FROM Requerimiento ORDER BY requerimientoId DESC LIMIT 1")
    fun getMaxIdRequerimiento(): LiveData<Int>

    @Query("SELECT * FROM Requerimiento")
    fun getRequerimientosTask(): List<Requerimiento>
}