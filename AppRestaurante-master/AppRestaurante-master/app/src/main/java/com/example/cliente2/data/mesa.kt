package com.example.cliente2.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mesa")
data class Mesa(
    @PrimaryKey(autoGenerate = true) val mesaId: Int = 0,
    @ColumnInfo(name = "numero") val numero: Int,
    @ColumnInfo(name = "mesaNumero") val mesaNumero: Int,
    @ColumnInfo(name = "status") val status: String = "Livre" // 'Livre' ou 'Ocupada'
)