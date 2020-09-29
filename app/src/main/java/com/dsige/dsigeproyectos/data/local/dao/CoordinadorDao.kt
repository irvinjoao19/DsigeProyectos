package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.Coordinador

@Dao
interface CoordinadorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoordinadorTask(c: Coordinador)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoordinadorListTask(c: List<Coordinador>)

    @Update
    fun updateCoordinadorTask(vararg c: Coordinador)

    @Delete
    fun deleteCoordinadorTask(c: Coordinador)

    @Query("SELECT * FROM Coordinador")
    fun getCoordinadorTask(): LiveData<List<Coordinador>>

    @Query("SELECT * FROM Coordinador")
    fun getCoordinador(): Coordinador

    @Query("DELETE FROM Coordinador")
    fun deleteAll()
}