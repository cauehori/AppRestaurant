package com.example.cliente2.data

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "pratos")
data class Prato(
    @PrimaryKey(autoGenerate = true) val pratoId: Int = 0,
    @ColumnInfo(name = "nome") val nome: String,
    //@ColumnInfo(name = "descricao") val descricao: String?,
    @ColumnInfo(name = "preco") val preco: Double
)