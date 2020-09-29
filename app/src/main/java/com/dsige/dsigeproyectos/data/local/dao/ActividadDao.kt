package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.Actividad

@Dao
interface ActividadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertActividadTask(c: Actividad)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertActividadListTask(c: List<Actividad>)

    @Update
    fun updateActividadTask(vararg c: Actividad)

    @Delete
    fun deleteActividadTask(c: Actividad)

    @Query("SELECT * FROM Actividad")
    fun getActividades(): LiveData<List<Actividad>>

    @Query("SELECT * FROM Actividad")
    fun getActividadTask(): List<Actividad>

    @Query("SELECT * FROM Actividad")
    fun getActividad(): Actividad

    @Query("DELETE FROM Actividad")
    fun deleteAll()
}