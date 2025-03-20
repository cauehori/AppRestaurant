package com.example.cliente2.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Cliente")

data class Cliente(
    @PrimaryKey val clienteId: Int,
    @ColumnInfo(name = "mesaNumero") val mesaNumero: Int,
    @ColumnInfo(name = "status") val status: String = "Livre"
)
