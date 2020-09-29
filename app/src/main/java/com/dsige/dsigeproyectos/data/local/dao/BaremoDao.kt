package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.Baremo

@Dao
interface BaremoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBaremoTask(c: Baremo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBaremoListTask(c: List<Baremo>)

    @Update
    fun updateBaremoTask(vararg c: Baremo)

    @Delete
    fun deleteBaremoTask(c: Baremo)

    @Query("SELECT * FROM Baremo")
    fun getBaremos(): LiveData<List<Baremo>>

    @Query("SELECT * FROM Baremo WHERE actividadId =:id")
    fun getBaremosById(id: Int): LiveData<List<Baremo>>

    @Query("SELECT * FROM Baremo")
    fun getBaremo(): Baremo

    @Query("SELECT * FROM Baremo WHERE baremoId =:id")
    fun getBaremoById(id: Int): LiveData<Baremo>


    @Query("DELETE FROM Baremo")
    fun deleteAll()
}