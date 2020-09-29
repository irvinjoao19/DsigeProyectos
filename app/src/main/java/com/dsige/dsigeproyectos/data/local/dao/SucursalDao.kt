package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.Sucursal

@Dao
interface SucursalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSucursalTask(c: Sucursal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSucursalListTask(c: List<Sucursal>)

    @Update
    fun updateSucursalTask(vararg c: Sucursal)

    @Delete
    fun deleteSucursalTask(c: Sucursal)

    @Query("SELECT * FROM Sucursal")
    fun getSucursalTask(): LiveData<List<Sucursal>>

    @Query("SELECT * FROM Sucursal")
    fun getSucursal(): Sucursal

    @Query("SELECT * FROM Sucursal WHERE codigo=:id")
    fun getSucursalById(id: Int): LiveData<Sucursal>

    @Query("DELETE FROM Sucursal")
    fun deleteAll()
}