package com.example.cliente2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Mesa::class, Prato::class, Pedido::class, PedidoPrato::class, Cliente::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mesaDao(): mesaDao
    abstract fun pedidoDao(): PedidoDao
    abstract fun pedidoPratoDao(): PedidoPratoDao
    abstract fun pratoDao(): PratoDao
    abstract fun clienteDao(): ClienteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cliente2_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}