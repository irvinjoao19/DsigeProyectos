package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.logistica.Orden

@Dao
interface OrdenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrdenTask(c: Orden)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrdenListTask(c: List<Orden>)

    @Update
    fun updateOrdenTask(vararg c: Orden)

    @Delete
    fun deleteOrdenTask(c: Orden)

    @Query("SELECT * FROM Orden")
    fun getOrdenes(): LiveData<List<Orden>>

    @Query("SELECT * FROM Orden")
    fun getOrdenTask(): List<Orden>

    @Query("SELECT * FROM Orden")
    fun getOrden(): Orden

    @Query("DELETE FROM Orden")
    fun deleteAll()

    @Query("SELECT * FROM Orden WHERE estadoId = 1 GROUP BY nroOrden")
    fun getOrdenGroup(): LiveData<List<Orden>>

    @Query("SELECT * FROM Orden WHERE estadoId = 1 AND delegacion=:e GROUP BY nroOrden")
    fun getOrdenGroup(e:String): LiveData<List<Orden>>

    @Query("SELECT * FROM Orden WHERE nroOrden =:c GROUP BY nroOrden")
    fun getOrdenGroupOne(c:String): LiveData<Orden>

    @Query("SELECT * FROM Orden WHERE nroOrden =:c ")
    fun getOrdenByCodigo(c: String): LiveData<List<Orden>>

    @Query("UPDATE Orden SET estadoId = 0 WHERE ordenId=:i")
    fun updateAprobacion(i: Int)
}