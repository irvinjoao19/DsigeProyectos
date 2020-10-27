package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.logistica.Anulacion

@Dao
interface AnulacionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnulacionTask(c: Anulacion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnulacionListTask(c: List<Anulacion>)

    @Update
    fun updateAnulacionTask(vararg c: Anulacion)

    @Delete
    fun deleteAnulacionTask(c: Anulacion)

    @Query("SELECT * FROM Anulacion")
    fun getAnulaciones(): LiveData<List<Anulacion>>

    @Query("SELECT * FROM Anulacion")
    fun getAnulacionTask(): List<Anulacion>

    @Query("SELECT * FROM Anulacion")
    fun getAnulacion(): Anulacion

    @Query("DELETE FROM Anulacion")
    fun deleteAll()

    @Query("SELECT * FROM Anulacion WHERE estadoId = 1 GROUP BY nroOrden")
    fun getAnulacionGroup(): LiveData<List<Anulacion>>

    @Query("SELECT * FROM Anulacion WHERE estadoId = 1 AND delegacion=:e GROUP BY nroOrden")
    fun getAnulacionGroup(e:String): LiveData<List<Anulacion>>

    @Query("SELECT * FROM Anulacion WHERE nroOrden =:c GROUP BY nroOrden")
    fun getAnulacionGroupOne(c:String): LiveData<Anulacion>

    @Query("SELECT * FROM Anulacion WHERE nroOrden =:c ")
    fun getAnulacionByCodigo(c: String): LiveData<List<Anulacion>>

    @Query("UPDATE Anulacion SET estadoId = 0 WHERE id=:i")
    fun updateAprobacion(i: Int)

    @Query("UPDATE Anulacion SET estadoId = 0 WHERE id=:i")
    fun updateAnulacion(i: Int)
}