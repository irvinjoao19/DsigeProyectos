package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.trinidad.VehiculoVales

@Dao
interface VehiculoValesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVehiculoValesTask(c: VehiculoVales)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVehiculoValesListTask(c: List<VehiculoVales>)

    @Update
    fun updateVehiculoValesTask(vararg c: VehiculoVales)

    @Delete
    fun deleteVehiculoValesTask(c: VehiculoVales)

    @Query("SELECT * FROM VehiculoVales")
    fun getVehiculoVales(): LiveData<List<VehiculoVales>>

    @Query("SELECT * FROM VehiculoVales WHERE placa =:id")
    fun getVehiculoValesById(id: String): LiveData<VehiculoVales>

    @Query("DELETE FROM VehiculoVales")
    fun deleteAll()

    @Query("SELECT * FROM VehiculoVales")
    fun getVehiculoValessTask(): List<VehiculoVales>

    @Query("SELECT * FROM VehiculoVales WHERE placa =:id")
    fun getValeVehiculo(id: String): LiveData<List<VehiculoVales>>

    @Query("SELECT * FROM VehiculoVales WHERE valeId =:id")
    fun getValeVehiculoById(id: Int): LiveData<VehiculoVales>

    @Query("SELECT * FROM VehiculoVales WHERE placa =:id")
    fun getVehiculoValeTaskById(id: String): List<VehiculoVales>

    @Query("UPDATE VehiculoVales SET estado=:i")
    fun updateEnabledVale(i: Int)
}