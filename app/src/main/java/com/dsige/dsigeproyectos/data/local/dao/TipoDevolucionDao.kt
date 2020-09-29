package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.TipoDevolucion

@Dao
interface TipoDevolucionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTipoDevolucionTask(c: TipoDevolucion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTipoDevolucionListTask(c: List<TipoDevolucion>)

    @Update
    fun updateTipoDevolucionTask(vararg c: TipoDevolucion)

    @Delete
    fun deleteTipoDevolucionTask(c: TipoDevolucion)

    @Query("SELECT * FROM TipoDevolucion")
    fun getTipoDevolucionTask(): LiveData<List<TipoDevolucion>>

    @Query("SELECT * FROM TipoDevolucion")
    fun getTipoDevolucion(): TipoDevolucion

    @Query("DELETE FROM TipoDevolucion")
    fun deleteAll()
}