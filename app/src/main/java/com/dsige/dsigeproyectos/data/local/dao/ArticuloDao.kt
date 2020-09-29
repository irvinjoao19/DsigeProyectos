package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.Articulo

@Dao
interface ArticuloDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticuloTask(c: Articulo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticuloListTask(c: List<Articulo>)

    @Update
    fun updateArticuloTask(vararg c: Articulo)

    @Delete
    fun deleteArticuloTask(c: Articulo)

    @Query("SELECT * FROM Articulo")
    fun getArticuloTask(): LiveData<List<Articulo>>

    @Query("SELECT * FROM Articulo")
    fun getArticulo(): Articulo

    @Query("SELECT * FROM Articulo WHERE articuloId =:id")
    fun getArticuloById(id: Int): LiveData<Articulo>

    @Query("SELECT * FROM Articulo WHERE tipo =:t")
    fun getArticuloByTipo(t: Int): LiveData<List<Articulo>>

    @Query("DELETE FROM Articulo")
    fun deleteAll()
}