package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.Area

@Dao
interface AreaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAreaTask(c: Area)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAreaListTask(c: List<Area>)

    @Update
    fun updateAreaTask(vararg c: Area)

    @Delete
    fun deleteAreaTask(c: Area)

    @Query("SELECT * FROM Area")
    fun getAreaTask(): LiveData<List<Area>>

    @Query("SELECT * FROM Area")
    fun getArea(): Area

    @Query("SELECT * FROM Area WHERE areaId =:id")
    fun getAreaById(id: Int): LiveData<Area>

    @Query("DELETE FROM Area")
    fun deleteAll()
}