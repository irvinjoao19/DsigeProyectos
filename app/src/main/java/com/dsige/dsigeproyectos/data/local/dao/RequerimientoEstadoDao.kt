package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.Solicitud
import com.dsige.dsigeproyectos.data.local.model.logistica.RequerimientoEstado

@Dao
interface RequerimientoEstadoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRequerimientoEstadoTask(c: RequerimientoEstado)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRequerimientoEstadoListTask(c: List<RequerimientoEstado>)

    @Update
    fun updateRequerimientoEstadoTask(vararg c: RequerimientoEstado)

    @Delete
    fun deleteRequerimientoEstadoTask(c: RequerimientoEstado)

    @Query("SELECT * FROM RequerimientoEstado")
    fun getRequerimientoEstados(): LiveData<List<RequerimientoEstado>>

    @Query("SELECT * FROM RequerimientoEstado")
    fun getRequerimientoEstado(): RequerimientoEstado

    @Query("DELETE FROM RequerimientoEstado")
    fun deleteAll()
}