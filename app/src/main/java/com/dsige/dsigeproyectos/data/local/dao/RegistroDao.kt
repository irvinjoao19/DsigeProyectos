package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.trinidad.Registro

@Dao
interface RegistroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRegistroTask(c: Registro)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRegistroListTask(c: List<Registro>)

    @Update
    fun updateRegistroTask(vararg c: Registro)

    @Delete
    fun deleteRegistroTask(c: Registro)

    @Query("SELECT * FROM Registro")
    fun getRegistro(): LiveData<Registro>

    @Query("SELECT COUNT(estado) FROM Registro WHERE estado = 1")
    fun getSizeRegistro(): LiveData<Int>

    @Query("SELECT * FROM Registro WHERE active =:valor")
    fun getAllRegistroTask(valor: Int): List<Registro>

    @Query("SELECT COUNT(estado) FROM Registro WHERE estado =:valor")
    fun getAllRegistroVerificate(valor: Int): LiveData<Int>

    @Query("SELECT * FROM Registro WHERE registroId =:id")
    fun getRegistroById(id: Int): LiveData<Registro>

    @Query("SELECT * FROM Registro WHERE registroId =:id")
    fun getRegistroTaskById(id: Int): Registro

    @Query("DELETE FROM Registro")
    fun deleteAll()

    @Query("UPDATE Registro SET active = 2 , estado = :e , identity=:retorno WHERE registroId =:id")
    fun updateRegistroEstado(id: Int, retorno: Int,e:String)

    @Query("UPDATE Registro SET estado =:e")
    fun updateRegistroMasivoEstado(e: Int)

    @Query("SELECT registroId FROM Registro ORDER BY registroId DESC")
    fun getIdentity(): LiveData<Int>

    @Query("SELECT registroId FROM Registro ORDER BY registroId DESC")
    fun getTaskIdentity(): Int

    @Query("SELECT * FROM Registro WHERE tipo =:t")
    fun getRegistroByTipo(t: Int): LiveData<List<Registro>>

    @Query("SELECT * FROM Registro WHERE tipo =:t GROUP BY nroObra")
    fun getRegistroPagingByTipo(t: Int): DataSource.Factory<Int, Registro>

//    @Query("SELECT * FROM Registro WHERE tipo =:t AND estado LIKE :s GROUP BY nroObra")
    @Query("SELECT * FROM Registro WHERE tipo =:t AND estado =:s GROUP BY nroObra")
    fun getRegistroPagingByTipo(t: Int,s:String): DataSource.Factory<Int, Registro>

    @Query("SELECT * FROM Registro WHERE nroObra =:o")
    fun getRegistroByObra(o: String): LiveData<List<Registro>>

    @Query("UPDATE Registro SET foto =:name ,active = 1 WHERE registroId =:id")
    fun updatePhoto(id: Int, name: String)

    @Query("UPDATE Registro SET active = 1 , estado = :e WHERE registroId =:id")
    fun closeRegistro(id: Int,e:String)
}