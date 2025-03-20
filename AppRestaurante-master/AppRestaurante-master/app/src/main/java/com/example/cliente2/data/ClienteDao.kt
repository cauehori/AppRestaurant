package com.example.cliente2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {
    @Insert
    suspend fun inserirCliente(cliente: Cliente)

    @Update
    suspend fun atualizarCliente(cliente: Cliente)

    @Query("SELECT COUNT(*) FROM Cliente")
    suspend fun contarClientes(): Int

    @Query("SELECT status FROM Cliente WHERE clienteId = :id")
    suspend fun getStatusById(id: Int): String

    @Query("SELECT mesaNumero FROM Cliente WHERE clienteId = :id")
    suspend fun getNumeroMesaById(id: Int): Int
}