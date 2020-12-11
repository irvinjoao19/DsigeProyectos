package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.logistica.OrdenEstado

@Dao
interface OrdenEstadoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrdenEstadoTask(c: OrdenEstado)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrdenEstadoListTask(c: List<OrdenEstado>)

    @Update
    fun updateOrdenEstadoTask(vararg c: OrdenEstado)

    @Delete
    fun deleteOrdenEstadoTask(c: OrdenEstado)

    @Query("SELECT * FROM OrdenEstado")
    fun getOrdenEstados(): LiveData<List<OrdenEstado>>

    @Query("SELECT * FROM OrdenEstado")
    fun getOrdenEstadoTask(): List<OrdenEstado>

    @Query("SELECT * FROM OrdenEstado")
    fun getOrdenEstado(): OrdenEstado

    @Query("DELETE FROM OrdenEstado")
    fun deleteAll()

    @Query("SELECT * FROM OrdenEstado")
    fun getOrdenEstadoByOne(): LiveData<OrdenEstado>
}