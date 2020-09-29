package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.logistica.OrdenDetalle

@Dao
interface OrdenDetalleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrdenDetalleTask(c: OrdenDetalle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrdenDetalleListTask(c: List<OrdenDetalle>)

    @Update
    fun updateOrdenDetalleTask(vararg c: OrdenDetalle)

    @Delete
    fun deleteOrdenDetalleTask(c: OrdenDetalle)

    @Query("SELECT * FROM OrdenDetalle")
    fun getOrdenDetallees(): LiveData<List<OrdenDetalle>>

    @Query("SELECT * FROM OrdenDetalle")
    fun getOrdenDetalleTask(): List<OrdenDetalle>

    @Query("SELECT * FROM OrdenDetalle")
    fun getOrdenDetalle(): OrdenDetalle

    @Query("SELECT id FROM OrdenDetalle ORDER BY id DESC")
    fun getIdentity(): Int

    @Query("DELETE FROM OrdenDetalle")
    fun deleteAll()
}