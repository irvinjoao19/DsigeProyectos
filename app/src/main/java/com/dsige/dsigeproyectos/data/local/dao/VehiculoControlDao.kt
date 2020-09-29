package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.trinidad.VehiculoControl

@Dao
interface VehiculoControlDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVehiculoControlTask(c: VehiculoControl)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVehiculoControlListTask(c: List<VehiculoControl>)

    @Update
    fun updateVehiculoControlTask(vararg c: VehiculoControl)

    @Delete
    fun deleteVehiculoControlTask(c: VehiculoControl)

    @Query("SELECT * FROM VehiculoControl")
    fun getVehiculoControl(): LiveData<List<VehiculoControl>>

    @Query("SELECT * FROM VehiculoControl WHERE placa =:id")
    fun getVehiculoControlById(id: String): LiveData<VehiculoControl>

    @Query("DELETE FROM VehiculoControl")
    fun deleteAll()

    @Query("SELECT * FROM VehiculoControl")
    fun getVehiculoControlsTask(): List<VehiculoControl>

    @Query("SELECT * FROM VehiculoControl WHERE placa =:id")
    fun getControlVehiculo(id: String): LiveData<List<VehiculoControl>>

    @Query("SELECT * FROM VehiculoControl WHERE controlId =:id")
    fun getControVehiculoById(id: Int): LiveData<VehiculoControl>

    @Query("SELECT * FROM VehiculoControl WHERE placa =:id")
    fun getVehiculoControlTaskById(id: String): List<VehiculoControl>

    @Query("UPDATE VehiculoControl SET estado=:i")
    fun updateEnabledControl(i: Int)
}