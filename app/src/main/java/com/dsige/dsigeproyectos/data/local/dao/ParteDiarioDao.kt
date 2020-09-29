package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.ParteDiario

@Dao
interface ParteDiarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertParteDiarioTask(c: ParteDiario)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPartediarioListTask(c: List<ParteDiario>)

    @Update
    fun updateParteDiarioTask(vararg c: ParteDiario)

    @Delete
    fun deleteParteDiarioTask(c: ParteDiario)

    @Query("SELECT * FROM ParteDiario ORDER BY parteDiarioId DESC")
    fun getParteDiarioTask(): List<ParteDiario>

    @Query("SELECT * FROM ParteDiario ORDER BY parteDiarioId DESC")
    fun getParteDiarios(): DataSource.Factory<Int, ParteDiario>

    @Query("SELECT * FROM ParteDiario WHERE fechaMovil =:f ORDER BY parteDiarioId DESC")
    fun getParteDiariosFecha(f: String): DataSource.Factory<Int, ParteDiario>

    @Query("SELECT * FROM ParteDiario WHERE fechaMovil =:f AND (direccion LIKE :s OR cliente LIKE :s) ORDER BY parteDiarioId DESC")
    fun getParteDiariosSearch(f: String, s: String): DataSource.Factory<Int, ParteDiario>

    @Query("SELECT * FROM ParteDiario WHERE fechaMovil =:f AND estadoCodigo=:e ORDER BY parteDiarioId DESC")
    fun getParteDiarioEstado(f: String, e: String): DataSource.Factory<Int, ParteDiario>

    @Query("SELECT * FROM ParteDiario WHERE fechaMovil =:f AND estadoCodigo=:e AND (direccion LIKE :s OR cliente LIKE :s) ORDER BY parteDiarioId DESC")
    fun getParteDiariosComplete(
        f: String, e: String, s: String
    ): DataSource.Factory<Int, ParteDiario>

    @Query("SELECT * FROM ParteDiario WHERE estadoCodigo=:e")
    fun getParteDiarioPendiente(e: String): LiveData<List<ParteDiario>>

    @Query("SELECT * FROM ParteDiario")
    fun getParteDiario(): ParteDiario

    @Query("SELECT * FROM ParteDiario WHERE parteDiarioId =:id")
    fun getParteDiarioById(id: Int): LiveData<ParteDiario>

    @Query("SELECT * FROM ParteDiario WHERE parteDiarioId =:id")
    fun getParteDiarioTaskById(id: Int): ParteDiario

    @Query("DELETE FROM ParteDiario")
    fun deleteAll()

    @Query("SELECT * FROM ParteDiario ORDER BY parteDiarioId DESC LIMIT 1")
    fun getMaxIdParteDiario(): LiveData<ParteDiario>

    @Query("UPDATE ParteDiario SET identity=:retorno , tipo = 1 , estado = 1 , estadoCodigo =:e WHERE parteDiarioId=:base")
    fun updateParteDiario(base: Int, retorno: Int, e: String)
}