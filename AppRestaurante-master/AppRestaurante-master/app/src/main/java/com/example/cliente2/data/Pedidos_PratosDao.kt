package com.example.cliente2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface PedidoPratoDao {
    @Insert
    suspend fun inserirPedidoPrato(pedidoPrato: PedidoPrato): Long

    @Query("SELECT * FROM pedido_pratos WHERE pedidoId = :pedidoId")
    suspend fun obterPratosPorPedido(pedidoId: Int): List<PedidoPrato>

    @Query ("SELECT SUM(pedido_pratos.quantidade) FROM pedido_pratos INNER JOIN pedidos ON pedido_pratos.pedidoId = pedidos.pedidoId WHERE pedido_pratos.pratoId = 1 AND pedidos.mesaId = :mesaId")
    suspend fun somaPrato1(mesaId: Int) : Int?

    @Query ("SELECT SUM(pedido_pratos.quantidade) FROM pedido_pratos INNER JOIN pedidos ON pedido_pratos.pedidoId = pedidos.pedidoId WHERE pedido_pratos.pratoId = 2 AND pedidos.mesaId = :mesaId")
    suspend fun somaPrato2(mesaId: Int) : Int?

    @Query ("SELECT SUM(pedido_pratos.quantidade) FROM pedido_pratos INNER JOIN pedidos ON pedido_pratos.pedidoId = pedidos.pedidoId WHERE pedido_pratos.pratoId = 3 AND pedidos.mesaId = :mesaId")
    suspend fun somaPrato3(mesaId: Int) : Int?

    @Query ("SELECT SUM(pedido_pratos.quantidade) FROM pedido_pratos INNER JOIN pedidos ON pedido_pratos.pedidoId = pedidos.pedidoId WHERE pedido_pratos.pratoId = 4 AND pedidos.mesaId = :mesaId")
    suspend fun somaPrato4(mesaId: Int) : Int?
}