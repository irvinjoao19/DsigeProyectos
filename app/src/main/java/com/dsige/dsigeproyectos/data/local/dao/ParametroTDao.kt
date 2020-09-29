package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.trinidad.ParametroT

@Dao
interface ParametroTDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertParametroTask(c: ParametroT)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertParametroListTask(c: List<ParametroT>)

    @Update
    fun updateParametroTask(vararg c: ParametroT)

    @Delete
    fun deleteParametroTask(c: ParametroT)

    @Query("SELECT * FROM ParametroT")
    fun getParametroTask(): LiveData<ParametroT>

    @Query("SELECT * FROM ParametroT")
    fun getParametro(): ParametroT

    @Query("SELECT * FROM ParametroT WHERE tipo =:t")
    fun getComboByTipo(t:Int) : LiveData<List<ParametroT>>

    @Query("DELETE FROM ParametroT")
    fun deleteAll()
}