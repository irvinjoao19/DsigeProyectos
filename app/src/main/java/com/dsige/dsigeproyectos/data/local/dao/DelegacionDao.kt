package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.Solicitud
import com.dsige.dsigeproyectos.data.local.model.logistica.Delegacion

@Dao
interface DelegacionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDelegacionTask(c: Delegacion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDelegacionListTask(c: List<Delegacion>)

    @Update
    fun updateDelegacionTask(vararg c: Delegacion)

    @Delete
    fun deleteDelegacionTask(c: Delegacion)

    @Query("SELECT * FROM Delegacion")
    fun getDelegacions(): LiveData<List<Delegacion>>

    @Query("SELECT * FROM Delegacion")
    fun getDelegacion(): Delegacion

    @Query("DELETE FROM Delegacion")
    fun deleteAll()
}