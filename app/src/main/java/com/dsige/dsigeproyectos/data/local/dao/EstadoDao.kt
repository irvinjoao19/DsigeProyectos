package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.Estado

@Dao
interface EstadoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEstadoTask(c: Estado)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEstadoListTask(c: List<Estado>)

    @Update
    fun updateEstadoTask(vararg c: Estado)

    @Delete
    fun deleteEstadoTask(c: Estado)

    @Query("SELECT * FROM Estado")
    fun getEstado(): LiveData<List<Estado>>

    @Query("DELETE FROM Estado")
    fun deleteAll()
}