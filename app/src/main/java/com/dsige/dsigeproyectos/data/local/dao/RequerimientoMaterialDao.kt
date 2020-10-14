package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.Solicitud
import com.dsige.dsigeproyectos.data.local.model.logistica.RequerimientoMaterial

@Dao
interface RequerimientoMaterialDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRequerimientoMaterialTask(c: RequerimientoMaterial)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRequerimientoMaterialListTask(c: List<RequerimientoMaterial>)

    @Update
    fun updateRequerimientoMaterialTask(vararg c: RequerimientoMaterial)

    @Delete
    fun deleteRequerimientoMaterialTask(c: RequerimientoMaterial)

    @Query("SELECT * FROM RequerimientoMaterial")
    fun getRequerimientoMaterials(): LiveData<List<RequerimientoMaterial>>

    @Query("SELECT * FROM RequerimientoMaterial WHERE codigo=:c")
    fun getRequerimientoMaterial(c: String): LiveData<RequerimientoMaterial>

    @Query("DELETE FROM RequerimientoMaterial")
    fun deleteAll()

    @Query("SELECT * FROM RequerimientoMaterial")
    fun getRequerimientoMaterialsTask(): List<RequerimientoMaterial>

    @Query("SELECT * FROM RequerimientoMaterial")
    fun getRequerimientoMateriales(): LiveData<List<RequerimientoMaterial>>
}