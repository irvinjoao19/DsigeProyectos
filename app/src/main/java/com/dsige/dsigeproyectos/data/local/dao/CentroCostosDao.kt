package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.CentroCostos

@Dao
interface CentroCostosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCentroCostosTask(c: CentroCostos)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCentroCostosListTask(c: List<CentroCostos>)

    @Update
    fun updateCentroCostosTask(vararg c: CentroCostos)

    @Delete
    fun deleteCentroCostosTask(c: CentroCostos)

    @Query("SELECT * FROM CentroCostos")
    fun getCentroCostosTask(): LiveData<List<CentroCostos>>

    @Query("SELECT * FROM CentroCostos")
    fun getCentroCostos(): CentroCostos

    @Query("SELECT * FROM CentroCostos WHERE centroId =:id")
    fun getCentroCostosById(id: Int): LiveData<CentroCostos>

    @Query("DELETE FROM CentroCostos")
    fun deleteAll()
}