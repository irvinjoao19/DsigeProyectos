package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.logistica.ComboEstado

@Dao
interface ComboEstadoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComboEstadoTask(c: ComboEstado)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComboEstadoListTask(c: List<ComboEstado>)

    @Update
    fun updateComboEstadoTask(vararg c: ComboEstado)

    @Delete
    fun deleteComboEstadoTask(c: ComboEstado)

    @Query("SELECT * FROM ComboEstado")
    fun getComboEstados(): LiveData<List<ComboEstado>>

    @Query("SELECT * FROM ComboEstado")
    fun getComboEstadoTask(): List<ComboEstado>

    @Query("SELECT * FROM ComboEstado")
    fun getComboEstado(): ComboEstado

    @Query("DELETE FROM ComboEstado")
    fun deleteAll()
}