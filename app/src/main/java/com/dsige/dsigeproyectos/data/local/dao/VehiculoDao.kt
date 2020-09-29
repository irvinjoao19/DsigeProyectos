package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.trinidad.Vehiculo

@Dao
interface VehiculoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVehiculoTask(c: Vehiculo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVehiculoListTask(c: List<Vehiculo>)

    @Update
    fun updateVehiculoTask(vararg c: Vehiculo)

    @Delete
    fun deleteVehiculoTask(c: Vehiculo)

    @Query("SELECT * FROM Vehiculo")
    fun getVehiculo(): LiveData<List<Vehiculo>>

    @Query("SELECT * FROM Vehiculo WHERE placa =:id")
    fun getVehiculoById(id: String): LiveData<Vehiculo>

    @Query("DELETE FROM Vehiculo")
    fun deleteAll()

    @Query("SELECT * FROM Vehiculo")
    fun getVehiculosTask(): List<Vehiculo>

    @Query("SELECT * FROM Vehiculo WHERE estado =:e")
    fun getAllVehiculoTask(e: Int): List<Vehiculo>

    @Query("UPDATE Vehiculo SET estado =:e")
    fun updateEnabledVehiculo(e: Int)

    @Query("UPDATE Vehiculo SET estado =1 WHERE placa =:p")
    fun updateEnabledVehiculoByPlaca(p: String)
}