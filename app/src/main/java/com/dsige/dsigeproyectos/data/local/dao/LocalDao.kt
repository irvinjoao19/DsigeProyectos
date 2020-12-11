package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.logistica.Local

@Dao
interface LocalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalTask(c: Local)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalListTask(c: List<Local>)

    @Update
    fun updateLocalTask(vararg c: Local)

    @Delete
    fun deleteLocalTask(c: Local)

    @Query("SELECT * FROM Local")
    fun getLocales(): LiveData<List<Local>>

    @Query("SELECT * FROM Local")
    fun getLocalTask(): List<Local>

    @Query("SELECT * FROM Local")
    fun getLocal(): Local

    @Query("DELETE FROM Local")
    fun deleteAll()
}