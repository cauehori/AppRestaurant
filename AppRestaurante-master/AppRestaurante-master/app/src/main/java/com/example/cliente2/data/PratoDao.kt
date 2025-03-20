package com.example.cliente2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface PratoDao {
    @Insert
    suspend fun inserirPrato(pratos: Prato)

    @Query("SELECT * FROM pratos")
    suspend fun getAllPratos(): List<Prato>

    @Query("SELECT COUNT(*) FROM pratos")
    suspend fun contarPratos(): Int

    @Query("SELECT preco FROM pratos WHERE pratoId = :pratoId")
    suspend fun precoPrato(pratoId: Int): Double
}