package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.logistica.RequerimientoDetalle

@Dao
interface RequerimientoDetalleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRequerimientoDetalleTask(c: RequerimientoDetalle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRequerimientoDetalleListTask(c: List<RequerimientoDetalle>)

    @Update
    fun updateRequerimientoDetalleTask(vararg c: RequerimientoDetalle)

    @Delete
    fun deleteRequerimientoDetalleTask(c: RequerimientoDetalle)

    @Query("SELECT * FROM RequerimientoDetalle")
    fun getRequerimientoDetallees(): LiveData<List<RequerimientoDetalle>>

    @Query("SELECT * FROM RequerimientoDetalle WHERE requerimientoId =:id")
    fun getRequerimientoDetalleTask(id: Int): List<RequerimientoDetalle>

    @Query("SELECT * FROM RequerimientoDetalle")
    fun getRequerimientoDetalle(): RequerimientoDetalle

    @Query("DELETE FROM RequerimientoDetalle")
    fun deleteAll()

    @Query("SELECT * FROM RequerimientoDetalle WHERE detalleId =:id")
    fun getRequerimientoDetalleById(id: Int): LiveData<RequerimientoDetalle>

    @Query("SELECT * FROM RequerimientoDetalle WHERE requerimientoId =:id")
    fun getRequerimientoDetalles(id: Int): LiveData<List<RequerimientoDetalle>>
}