package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.engie.RegistroBaremo

@Dao
interface RegistroBaremoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRegistroBaremoTask(c: RegistroBaremo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRegistroBaremoListTask(c: List<RegistroBaremo>)

    @Update
    fun updateRegistroBaremoTask(vararg c: RegistroBaremo)

    @Delete
    fun deleteRegistroBaremoTask(c: RegistroBaremo)

    @Query("SELECT * FROM RegistroBaremo WHERE parteDiarioId =:id")
    fun getRegistroBaremoByFK(id: Int): LiveData<List<RegistroBaremo>>

    @Query("SELECT * FROM RegistroBaremo WHERE parteDiarioId =:id")
    fun getRegistroBaremoTaskByFK(id: Int): List<RegistroBaremo>

    @Query("SELECT * FROM RegistroBaremo")
    fun getRegistroBaremo(): RegistroBaremo

    @Query("SELECT * FROM RegistroBaremo WHERE parteDiarioId =:id AND codigoBaremo =:codigo ")
    fun getValidateRegistroBaremo(id: Int, codigo: String): RegistroBaremo

    @Query("DELETE FROM RegistroBaremo")
    fun deleteAll()

    @Query("SELECT * FROM RegistroBaremo ORDER BY registroBaremoId DESC LIMIT 1")
    fun getMaxIdRegistroBaremo(): LiveData<RegistroBaremo>

    @Query("UPDATE RegistroBaremo SET identity=:retorno , tipo = 1 , estado =:e WHERE parteDiarioId=:base")
    fun updateRegistroBaremo(base: Int, retorno: Int, e: String)
}