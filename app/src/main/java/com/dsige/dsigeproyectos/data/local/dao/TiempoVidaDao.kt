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

    @Query("SELECT * FROM TiempoVida WHERE estadoId=1 GROUP BY id")
    fun getTiempoVidas(): LiveData<List<TiempoVida>>

    @Query("SELECT * FROM TiempoVida")
    fun getTiempoVidaTask(): List<TiempoVida>

    @Query("SELECT * FROM TiempoVida")
    fun getTiempoVida(): TiempoVida

    @Query("DELETE FROM TiempoVida")
    fun deleteAll()

    @Query("SELECT * FROM TiempoVida WHERE  id=:c GROUP BY id")
    fun getCampoTiempoVidaOne(c: Int): LiveData<TiempoVida>

    @Query("SELECT * FROM TiempoVida WHERE id=:c")
    fun getTiempoVidaByCodigo(c: Int): LiveData<List<TiempoVida>>

    @Query("SELECT * FROM TiempoVida WHERE id=:c")
    fun getTiempoVidaByCodigoTask(c: Int): List<TiempoVida>

    @Query("UPDATE TiempoVida SET estadoId = 0 WHERE id=:i")
    fun updateAprobacion(i: Int)

    @Query("UPDATE TiempoVida SET cantidadAprobada =:c WHERE id=:i")
    fun updateCantidad(i: Int, c: Double)
}