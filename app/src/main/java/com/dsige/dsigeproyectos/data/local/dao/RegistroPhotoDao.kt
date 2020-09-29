package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.RegistroPhoto

@Dao
interface RegistroPhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRegistroPhotoTask(c: RegistroPhoto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRegistroPhotoListTask(c: List<RegistroPhoto>)

    @Update
    fun updateRegistroPhotoTask(vararg c: RegistroPhoto)

    @Delete
    fun deleteRegistroPhotoTask(c: RegistroPhoto)

    @Query("SELECT * FROM RegistroPhoto WHERE parteDiarioId =:id")
    fun getRegistroPhotoByFK(id: Int): LiveData<List<RegistroPhoto>>

    @Query("SELECT * FROM RegistroPhoto WHERE parteDiarioId =:id")
    fun getRegistroPhotoTaskByFK(id: Int): List<RegistroPhoto>

    @Query("SELECT * FROM RegistroPhoto")
    fun getRegistroPhoto(): RegistroPhoto

    @Query("DELETE FROM RegistroPhoto")
    fun deleteAll()

    @Query("SELECT * FROM RegistroPhoto ORDER BY registroPhotoId DESC LIMIT 1")
    fun getMaxIdRegistroPhoto(): LiveData<RegistroPhoto>

    @Query("UPDATE RegistroPhoto SET identity=:retorno , tipo = 1 WHERE parteDiarioId=:base")
    fun updateRegistroPhoto(base: Int, retorno: Int)
}