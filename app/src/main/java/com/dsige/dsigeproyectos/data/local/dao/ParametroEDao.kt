package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.ParametroE

@Dao
interface ParametroEDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertParametroTask(c: ParametroE)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertParametroListTask(c: List<ParametroE>)

    @Update
    fun updateParametroTask(vararg c: ParametroE)

    @Delete
    fun deleteParametroTask(c: ParametroE)

    @Query("SELECT * FROM ParametroE")
    fun getParametroTask(): LiveData<List<ParametroE>>

    @Query("SELECT * FROM ParametroE")
    fun getParametro(): ParametroE

    @Query("SELECT * FROM ParametroE WHERE id_Configuracion =:id")
    fun getParametroById(id: Int): LiveData<ParametroE>

    @Query("DELETE FROM ParametroE")
    fun deleteAll()

}