package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.Cuadrilla

@Dao
interface CuadrillaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCuadrillaTask(c: Cuadrilla)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCuadrillaListTask(c: List<Cuadrilla>)

    @Update
    fun updateCuadrillaTask(vararg c: Cuadrilla)

    @Delete
    fun deleteCuadrillaTask(c: Cuadrilla)

    @Query("SELECT * FROM Cuadrilla")
    fun getCuadrillaTask(): LiveData<List<Cuadrilla>>

    @Query("SELECT * FROM Cuadrilla")
    fun getCuadrilla(): Cuadrilla

    @Query("SELECT * FROM Cuadrilla WHERE orden =:id")
    fun getCuadrillaByFK(id: String): LiveData<List<Cuadrilla>>

    @Query("SELECT * FROM Cuadrilla WHERE cuadrillaId =:id")
    fun getCuadrillaById(id: Int): LiveData<Cuadrilla>

    @Query("DELETE FROM Cuadrilla")
    fun deleteAll()
}