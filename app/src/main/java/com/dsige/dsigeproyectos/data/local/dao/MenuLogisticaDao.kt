package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.logistica.MenuLogistica

@Dao
interface MenuLogisticaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMenuLogisticaTask(c: MenuLogistica)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMenuLogisticaListTask(c: List<MenuLogistica>)

    @Update
    fun updateMenuLogisticaTask(vararg c: MenuLogistica)

    @Delete
    fun deleteMenuLogisticaTask(c: MenuLogistica)

    @Query("SELECT * FROM MenuLogistica")
    fun getMenuLogisticas(): LiveData<List<MenuLogistica>>

    @Query("DELETE FROM MenuLogistica")
    fun deleteAll()
}