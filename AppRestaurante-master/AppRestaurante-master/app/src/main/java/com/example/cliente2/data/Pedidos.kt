package com.example.cliente2.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "pedidos",
    foreignKeys = [ForeignKey(entity = Mesa::class,
        parentColumns = ["mesaId"],
        childColumns = ["mesaId"],
        onDelete = ForeignKey.CASCADE)])

data class Pedido(
    @PrimaryKey(autoGenerate = true) val pedidoId: Int = 0,
    @ColumnInfo(name = "mesaId") val mesaId: Int,
    @ColumnInfo(name = "status") val status: String = "Em andamento" // 'Em andamento', 'Pago', etc.
)