package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.RegistroMaterial

@Dao
interface RegistroMaterialDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRegistroMaterialTask(c: RegistroMaterial)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRegistroMaterialListTask(c: List<RegistroMaterial>)

    @Update
    fun updateRegistroMaterialTask(vararg c: RegistroMaterial)

    @Delete
    fun deleteRegistroMaterialTask(c: RegistroMaterial)

    @Query("SELECT * FROM RegistroMaterial WHERE parteDiarioId=:id")
    fun getRegistroMaterialByFK(id: Int): LiveData<List<RegistroMaterial>>

    @Query("SELECT * FROM RegistroMaterial WHERE parteDiarioId=:id")
    fun getRegistroMaterialTaskByFK(id: Int): List<RegistroMaterial>

    @Query("SELECT * FROM RegistroMaterial")
    fun getRegistroMaterial(): RegistroMaterial

    @Query("SELECT * FROM RegistroMaterial WHERE parteDiarioId =:id  AND codigoMaterial=:codigo AND guiaSalida=:g")
    fun getValidateRegistroMaterial(id: Int, codigo: String, g: String): RegistroMaterial

    @Query("DELETE FROM RegistroMaterial")
    fun deleteAll()

    @Query("SELECT * FROM RegistroMaterial ORDER BY registroMaterialId DESC LIMIT 1")
    fun getMaxIdRegistroMaterial(): LiveData<RegistroMaterial>

    @Query("UPDATE RegistroMaterial SET identity=:retorno , tipo = 1 , estado=:e  WHERE parteDiarioId=:base")
    fun updateRegistroMaterial(base: Int, retorno: Int, e: String)

}