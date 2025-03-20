package com.example.cliente2.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "pedido_pratos",
    foreignKeys = [
        ForeignKey(entity = Pedido::class, parentColumns = ["pedidoId"], childColumns = ["pedidoId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Prato::class, parentColumns = ["pratoId"], childColumns = ["pratoId"], onDelete = ForeignKey.CASCADE)
    ],
    primaryKeys = ["pedidoId", "pratoId"]
)
data class PedidoPrato(
    @ColumnInfo(name = "pedidoId") val pedidoId: Long,
    @ColumnInfo(name = "pratoId") val pratoId: Int,
    @ColumnInfo(name = "quantidade") val quantidade: Int
)