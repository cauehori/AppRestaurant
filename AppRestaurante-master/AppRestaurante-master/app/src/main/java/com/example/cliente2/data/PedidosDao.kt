package com.example.cliente2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface PedidoDao {
    @Insert
    suspend fun inserirPedido(pedido: Pedido): Long

    @Update
    suspend fun atualizarPedido(pedido: Pedido)

    @Query("SELECT * FROM pedidos WHERE mesaId = :mesaId AND status = 'Em andamento'")
    suspend fun obterPedidosEmAndamentoPorMesa(mesaId: Int): List<Pedido>

    @Query("SELECT * FROM pedidos WHERE mesaId = :mesaId AND status = 'Fechado'")
    suspend fun obterPedidosFechadosPorMesa(mesaId: Int): List<Pedido>

    @Query("SELECT * FROM pedidos WHERE mesaId = :mesaId")
    suspend fun obterPedidosPorMesa(mesaId: Int): List<Pedido>


}