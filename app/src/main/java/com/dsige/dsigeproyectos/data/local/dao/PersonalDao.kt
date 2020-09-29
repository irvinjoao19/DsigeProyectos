package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.Personal

@Dao
interface PersonalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPersonalTask(c: Personal)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPersonalListTask(c: List<Personal>)

    @Update
    fun updatePersonalTask(vararg c: Personal)

    @Delete
    fun deletePersonalTask(c: Personal)

    @Query("SELECT * FROM Personal")
    fun getPersonalTask(): LiveData<List<Personal>>

    @Query("SELECT * FROM Personal")
    fun getPersonal(): Personal

    @Query("SELECT * FROM Personal WHERE personalId =:id")
    fun getPersonalById(id: Int): LiveData<Personal>

    @Query("DELETE FROM Personal")
    fun deleteAll()
}