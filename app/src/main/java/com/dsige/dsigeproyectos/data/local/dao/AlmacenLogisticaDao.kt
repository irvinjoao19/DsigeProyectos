package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.logistica.AlmacenLogistica

@Dao
interface AlmacenLogisticaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlmacenLogisticaTask(c: AlmacenLogistica)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlmacenLogisticaListTask(c: List<AlmacenLogistica>)

    @Update
    fun updateAlmacenLogisticaTask(vararg c: AlmacenLogistica)

    @Delete
    fun deleteAlmacenLogisticaTask(c: AlmacenLogistica)

    @Query("SELECT * FROM AlmacenLogistica")
    fun getAlmacenLogisticas(): LiveData<List<AlmacenLogistica>>

    @Query("SELECT * FROM AlmacenLogistica")
    fun getAlmacenLogisticaTask(): List<AlmacenLogistica>

    @Query("SELECT * FROM AlmacenLogistica")
    fun getAlmacenLogistica(): AlmacenLogistica

    @Query("DELETE FROM AlmacenLogistica")
    fun deleteAll()
}