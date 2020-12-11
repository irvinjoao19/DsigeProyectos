package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.logistica.TiempoVida

@Dao
interface TiempoVidaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTiempoVidaTask(c: TiempoVida)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTiempoVidaListTask(c: List<TiempoVida>)

    @Update
    fun updateTiempoVidaTask(vararg c: TiempoVida)

    @Delete
    fun deleteTiempoVidaTask(c: TiempoVida)

    @Query("SELECT * FROM TiempoVida WHERE estadoId=1")
    fun getTiempoVidas(): LiveData<List<TiempoVida>>

    @Query("SELECT * FROM TiempoVida")
    fun getTiempoVidaTask(): List<TiempoVida>

    @Query("SELECT * FROM TiempoVida")
    fun getTiempoVida(): TiempoVida

    @Query("DELETE FROM TiempoVida")
    fun deleteAll()

    @Query("SELECT * FROM TiempoVida WHERE  obraCodigo=:c GROUP BY obraCodigo")
    fun getCampoTiempoVidaOne(c: String): LiveData<TiempoVida>

    @Query("SELECT * FROM TiempoVida WHERE obraCodigo=:c")
    fun getTiempoVidaByCodigo(c: String): LiveData<List<TiempoVida>>

    @Query("UPDATE TiempoVida SET estadoId = 0 WHERE id=:i")
    fun updateAprobacion(i: Int)

    @Query("UPDATE TiempoVida SET cantidadAprobada =:c WHERE id=:i")
    fun updateCantidad(i: Int, c: Double)
}