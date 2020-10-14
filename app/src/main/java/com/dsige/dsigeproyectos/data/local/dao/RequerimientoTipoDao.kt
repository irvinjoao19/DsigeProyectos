package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.Solicitud
import com.dsige.dsigeproyectos.data.local.model.logistica.RequerimientoTipo

@Dao
interface RequerimientoTipoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRequerimientoTipoTask(c: RequerimientoTipo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRequerimientoTipoListTask(c: List<RequerimientoTipo>)

    @Update
    fun updateRequerimientoTipoTask(vararg c: RequerimientoTipo)

    @Delete
    fun deleteRequerimientoTipoTask(c: RequerimientoTipo)

    @Query("SELECT * FROM RequerimientoTipo")
    fun getRequerimientoTipos(): LiveData<List<RequerimientoTipo>>

    @Query("SELECT * FROM RequerimientoTipo")
    fun getRequerimientoTipo(): RequerimientoTipo

    @Query("DELETE FROM RequerimientoTipo")
    fun deleteAll()
}