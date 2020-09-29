package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.Obra

@Dao
interface ObraDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertObraTask(c: Obra)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertObraListTask(c: List<Obra>)

    @Update
    fun updateObraTask(vararg c: Obra)

    @Delete
    fun deleteObraTask(c: Obra)

    @Query("SELECT * FROM Obra")
    fun getObraTask(): LiveData<List<Obra>>

    @Query("SELECT * FROM Obra")
    fun getObra(): Obra

    @Query("SELECT * FROM Obra WHERE obraId =:id")
    fun getObraById(id: String): LiveData<Obra>

    @Query("DELETE FROM Obra")
    fun deleteAll()

}