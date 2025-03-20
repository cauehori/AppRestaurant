package com.example.cliente2.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.cliente2.R
import com.example.cliente2.data.AppDatabase
import com.example.cliente2.data.Mesa
import com.example.cliente2.data.Pedido
import com.example.cliente2.data.PedidoDao
import com.example.cliente2.data.PedidoPratoDao
import com.example.cliente2.data.Prato
import com.example.cliente2.data.PratoDao
import com.example.cliente2.data.mesaDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PedidosAndamento : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var andamentoAdapeter: AndamentoAdapeter
    private lateinit var mesaList: List<Int> // Lista de pedidos


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_andamento)

        recyclerView = findViewById(R.id.recyclerviewAndamento)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializando o banco de dados e acessando os DAOs
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_database").build()
        val pedidoDao = db.pedidoDao()
        val pratoDao = db.pratoDao()
        val pedidoPratoDao = db.pedidoPratoDao()
        val mesaDao = db.mesaDao()

        lifecycleScope.launch {
            // Buscando os pedidos de maneira assíncrona
            mesaList = mesaDao.obterMesaOcupada() // Exemplo de busca de todos os pedidos

            // Inicializando o Adapter com a lista de pedidos e os DAOs
            andamentoAdapeter = AndamentoAdapeter(mesaList, pedidoDao, pedidoPratoDao, pratoDao, mesaDao, lifecycleScope)
            recyclerView.adapter = andamentoAdapeter
        }
    }
}



