package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.Almacen

@Dao
interface AlmacenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlmacenTask(c: Almacen)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlmacenListTask(c: List<Almacen>)

    @Update
    fun updateAlmacenTask(vararg c: Almacen)

    @Delete
    fun deleteAlmacenTask(c: Almacen)

    @Query("SELECT * FROM Almacen")
    fun getAlmacenTask(): LiveData<List<Almacen>>

    @Query("SELECT * FROM Almacen WHERE equipamiento ='SI' ")
    fun getAlmacenByEquipamiento(): LiveData<List<Almacen>>

    @Query("SELECT * FROM Almacen WHERE seraEdelnor ='SI' ")
    fun getAlmacenBySeraEdelnor(): LiveData<List<Almacen>>

    @Query("SELECT * FROM Almacen")
    fun getAlmacen(): Almacen

    @Query("SELECT * FROM Almacen WHERE codigo=:id")
    fun getAlmacenById(id: Int): LiveData<Almacen>

    @Query("DELETE FROM Almacen")
    fun deleteAll()
}