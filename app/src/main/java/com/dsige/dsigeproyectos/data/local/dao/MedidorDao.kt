package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.Medidor

@Dao
interface MedidorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMedidorTask(c: Medidor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMedidorListTask(c: List<Medidor>)

    @Update
    fun updateMedidorTask(vararg c: Medidor)

    @Delete
    fun deleteMedidorTask(c: Medidor)

    @Query("SELECT * FROM Medidor")
    fun getMedidor(): LiveData<List<Medidor>>

    @Query("SELECT * FROM Medidor")
    fun getMedidorTask(): Medidor

    @Query("SELECT * FROM Medidor WHERE medidorId=:id")
    fun getMedidorById(id: String): Medidor

    @Query("DELETE FROM Medidor")
    fun deleteAll()
}