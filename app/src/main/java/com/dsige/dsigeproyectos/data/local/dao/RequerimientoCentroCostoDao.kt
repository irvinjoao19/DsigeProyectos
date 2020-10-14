package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.Solicitud
import com.dsige.dsigeproyectos.data.local.model.logistica.RequerimientoCentroCosto

@Dao
interface RequerimientoCentroCostoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRequerimientoCentroCostoTask(c: RequerimientoCentroCosto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRequerimientoCentroCostoListTask(c: List<RequerimientoCentroCosto>)

    @Update
    fun updateRequerimientoCentroCostoTask(vararg c: RequerimientoCentroCosto)

    @Delete
    fun deleteRequerimientoCentroCostoTask(c: RequerimientoCentroCosto)

    @Query("SELECT * FROM RequerimientoCentroCosto")
    fun getRequerimientoCentroCostos(): LiveData<List<RequerimientoCentroCosto>>

    @Query("SELECT * FROM RequerimientoCentroCosto")
    fun getRequerimientoCentroCosto(): RequerimientoCentroCosto

    @Query("DELETE FROM RequerimientoCentroCosto")
    fun deleteAll()
}