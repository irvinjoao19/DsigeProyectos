package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.trinidad.RegistroDetalle

@Dao
interface RegistroDetalleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRegistroPhotoTask(c: RegistroDetalle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRegistroPhotoListTask(c: List<RegistroDetalle>)

    @Update
    fun updateRegistroPhotoTask(vararg c: RegistroDetalle)

    @Delete
    fun deleteRegistroPhotoTask(c: RegistroDetalle)

    @Query("SELECT * FROM RegistroDetalle")
    fun getRegistroPhotoTask(): LiveData<RegistroDetalle>

    @Query("SELECT * FROM RegistroDetalle")
    fun getRegistroPhoto(): RegistroDetalle

    @Query("SELECT * FROM RegistroDetalle WHERE registroId=:id ")
    fun getAllRegistroDetalleTask(id: Int): List<RegistroDetalle>

    @Query("SELECT * FROM RegistroDetalle WHERE detalleId =:id")
    fun getRegistroPhotoById(id: Int): LiveData<RegistroDetalle>

    @Query("DELETE FROM RegistroDetalle")
    fun deleteAll()

    @Query("UPDATE RegistroDetalle SET estado = 0 WHERE registroId =:id")
    fun updateRegistroPhotoEstado(id: Int)

    @Query("SELECT * FROM RegistroDetalle WHERE registroId =:id")
    fun getExistRegistroPhoto(id: Int): RegistroDetalle

    @Query("UPDATE RegistroDetalle SET estado =:e ")
    fun updateRegistroPhotoMasivoEstado(e: Int)

    @Query("SELECT * FROM RegistroDetalle WHERE registroId =:id")
    fun getRegistroPhotoPaging(id: Int): DataSource.Factory<Int, RegistroDetalle>

    @Query("SELECT detalleId FROM RegistroDetalle ORDER BY detalleId DESC")
    fun getIdentity(): LiveData<Int>

    @Query("SELECT * FROM RegistroDetalle WHERE detalleId =:id")
    fun getRegistroDetalleById(id: Int): RegistroDetalle

    @Query("SELECT * FROM RegistroDetalle WHERE detalleId =:id")
    fun getRegistroDetalle(id: Int): LiveData<RegistroDetalle>

    @Query("SELECT * FROM RegistroDetalle WHERE registroId =:id")
    fun getRegistroDetalleByRegistroId(id: Int): LiveData<List<RegistroDetalle>>

    @Query("UPDATE RegistroDetalle SET observacion =:obs WHERE detalleId =:id")
    fun updateObservacion(id: Int, obs: String)

    @Query("SELECT * FROM RegistroDetalle WHERE registroId =:id")
    fun getRegistroDetalleFk(id: Int): LiveData<RegistroDetalle>

    @Query("UPDATE RegistroDetalle SET active1 = 1 WHERE detalleId=:id")
    fun closeRegistroDetalle1(id: Int)

    @Query("UPDATE RegistroDetalle SET active2 = 1 WHERE detalleId=:id")
    fun closeRegistroDetalle2(id: Int)
}