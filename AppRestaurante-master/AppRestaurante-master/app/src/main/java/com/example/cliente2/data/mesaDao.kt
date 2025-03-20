package com.example.cliente2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface mesaDao {
    @Insert
    suspend fun inserirMesa(mesa: Mesa): Long

    @Update
    suspend fun atualizarMesa(mesa: Mesa)

    @Query("SELECT * FROM mesa WHERE mesaId = :mesaId LIMIT 1")
    suspend fun obterMesa(mesaId: Int): Mesa?

    @Query("SELECT COUNT(*) FROM mesa")
    suspend fun contarMesas(): Int

    @Query("UPDATE mesa SET status = :novoStatus WHERE mesaId = :mesaId")
    suspend fun atualizarStatusMesa(mesaId: Int, novoStatus: String)

    @Query("UPDATE mesa SET numero = 0 WHERE mesaId = :mesaId")
    suspend fun liberarMesa(mesaId: Int)

    @Query("SELECT count(*) FROM mesa WHERE numero = :numero")
    suspend fun contarMesasNumero(numero: Int): Int

    @Query("SELECT mesaId FROM mesa WHERE numero = :numero")
    suspend fun pegarIdMesa(numero: Int): Int

    @Query("SELECT mesaId FROM mesa WHERE status = 'Fechada'")
    suspend fun obterMesaFechada(): List<Int>

    @Query("SELECT mesaId FROM mesa WHERE status = 'Ocupado'")
    suspend fun obterMesaOcupada(): List<Int>

    @Query("SELECT mesaNumero FROM mesa WHERE mesaId = :mesaId")
    suspend fun obeterNumeroMesa(mesaId: Int): Int
}