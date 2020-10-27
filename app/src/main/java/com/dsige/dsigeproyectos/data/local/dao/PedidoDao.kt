package com.dsige.dsigeproyectos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dsigeproyectos.data.local.model.logistica.Pedido

@Dao
interface PedidoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPedidoTask(c: Pedido)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPedidoListTask(c: List<Pedido>)

    @Update
    fun updatePedidoTask(vararg c: Pedido)

    @Delete
    fun deletePedidoTask(c: Pedido)

    @Query("SELECT * FROM Pedido")
    fun getPedidoes(): LiveData<List<Pedido>>

    @Query("SELECT * FROM Pedido")
    fun getPedidoTask(): List<Pedido>

    @Query("SELECT * FROM Pedido")
    fun getPedido(): Pedido

    @Query("DELETE FROM Pedido")
    fun deleteAll()

    @Query("SELECT * FROM Pedido WHERE estadoId = 1 GROUP BY nroPedido")
    fun getPedidoGroup(): LiveData<List<Pedido>>

    @Query("SELECT * FROM Pedido WHERE estadoId = 1 AND delegacion=:e GROUP BY nroPedido")
    fun getPedidoGroup(e: String): LiveData<List<Pedido>>

    @Query("SELECT * FROM Pedido WHERE nroPedido =:c GROUP BY nroPedido")
    fun getPedidoGroupOne(c: String): LiveData<Pedido>

    @Query("SELECT * FROM Pedido WHERE nroPedido =:c ")
    fun getPedidoByCodigo(c: String): LiveData<List<Pedido>>

    @Query("UPDATE Pedido SET cantidadAprobada =:c WHERE id=:i")
    fun updateCantidad(i: Int, c: Double)

    @Query("UPDATE Pedido SET estadoId = 0 WHERE id=:i")
    fun updateAprobacion(i: Int)
}