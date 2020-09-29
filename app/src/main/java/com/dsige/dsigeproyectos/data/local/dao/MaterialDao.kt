package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.Material

@Dao
interface MaterialDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMaterialTask(c: Material)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMaterialListTask(c: List<Material>)

    @Update
    fun updateMaterialTask(vararg c: Material)

    @Delete
    fun deleteMaterialTask(c: Material)

    @Query("SELECT * FROM Material")
    fun getMaterialTask(): LiveData<List<Material>>

    @Query("SELECT * FROM Material")
    fun getMaterial(): Material

    @Query("SELECT * FROM Material WHERE id=:i")
    fun getMaterialById(i: Int): Material

    @Query("SELECT * FROM Material WHERE MaterialId =:id")
    fun getMaterialById(id: String): LiveData<Material>

    @Query("SELECT * FROM Material WHERE tipo =:t")
    fun getMaterialesByTipoObra(t: Int): LiveData<List<Material>>

    @Query("SELECT * FROM Material WHERE tipo =:t AND obra =:o")
    fun getMaterialesByObra(t: Int, o: String): LiveData<List<Material>>

    @Query("SELECT * FROM Material WHERE obra =:o AND almacenId =:a AND stock > 0")
    fun getMaterialesByObraAlmacen(o: String, a: String): LiveData<List<Material>>

    @Query("DELETE FROM Material")
    fun deleteAll()
}