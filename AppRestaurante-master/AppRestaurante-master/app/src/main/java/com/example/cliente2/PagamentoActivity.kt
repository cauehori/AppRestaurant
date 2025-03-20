package com.example.cliente2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.cliente2.data.AppDatabase
import com.example.cliente2.data.Cliente
import com.example.cliente2.data.ClienteDao
import com.example.cliente2.data.PedidoDao
import com.example.cliente2.data.PedidoPrato
import com.example.cliente2.data.PedidoPratoDao
import com.example.cliente2.data.PratoDao
import com.example.cliente2.data.mesaDao
import kotlinx.coroutines.launch

class PagamentoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_conta)

        val button_pagar = findViewById<Button>(R.id.btnPagar)

        val mesaId = intent.getIntExtra("mesaId", -1) // -1 é o valor padrão caso não tenha sido enviado

        // Verifica se o mesaId foi passado corretamente
        if (mesaId != -1) {
            // Use o mesaId para carregar as informações da mesa ou associar pedidos a ela
            println("Mesa ID: $mesaId")
        } else {
            // Caso o mesaId não tenha sido passado corretamente
            println("Mesa não encontrada")
        }

        val pedidoPratoDao: PedidoPratoDao by lazy {
            val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_database").build()
            db.pedidoPratoDao()
        }

        val mesaDao: mesaDao by lazy {
            val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_database").build()
            db.mesaDao()
        }

        val pedidoDao: PedidoDao by lazy {
            val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_database").build()
            db.pedidoDao()
        }

        val pratoDao: PratoDao by lazy {
            val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_database").build()
            db.pratoDao()
        }

        val clienteDao: ClienteDao by lazy {
            val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_database").build()
            db.clienteDao()
        }

        val quantidade1 = findViewById<TextView>(R.id.Quantidade1)
        val quantidade2 = findViewById<TextView>(R.id.Quantidade2)
        val quantidade3 = findViewById<TextView>(R.id.Quantidade3)
        val quantidade4 = findViewById<TextView>(R.id.Quantidade4)

        var v1: Double = 0.0
        var v2: Double = 0.0
        var v3: Double = 0.0
        var v4: Double = 0.0
        var vtotal: Double = 0.0


        val valor1 = findViewById<TextView>(R.id.Valor1)
        val valor2 = findViewById<TextView>(R.id.Valor2)
        val valor3 = findViewById<TextView>(R.id.Valor3)
        val valor4 = findViewById<TextView>(R.id.Valor4)
        val valorTotal = findViewById<TextView>(R.id.ValorTotal)




        lifecycleScope.launch {
            quantidade1.setText(pedidoPratoDao.somaPrato1(mesaId)?.toString() ?: "0")
            quantidade2.setText(pedidoPratoDao.somaPrato2(mesaId)?.toString() ?: "0")
            quantidade3.setText(pedidoPratoDao.somaPrato3(mesaId)?.toString() ?: "0")
            quantidade4.setText(pedidoPratoDao.somaPrato4(mesaId)?.toString() ?: "0")

            v1 = (pedidoPratoDao.somaPrato1(mesaId) ?: 0).toDouble() * pratoDao.precoPrato(1)
            valor1.setText(v1.toString())

            v2 = (pedidoPratoDao.somaPrato2(mesaId) ?: 0).toDouble() * pratoDao.precoPrato(2)
            valor2.setText(v2.toString())

            v3 = (pedidoPratoDao.somaPrato3(mesaId) ?: 0).toDouble() * pratoDao.precoPrato(3)
            valor3.setText(v3.toString())

            v4 = (pedidoPratoDao.somaPrato4(mesaId) ?: 0).toDouble() * pratoDao.precoPrato(4)
            valor4.setText(v4.toString())

            vtotal = v1 + v2 + v3 + v4
            valorTotal.setText(vtotal.toString())

        }


        button_pagar.setOnClickListener {

            // Criação do alerta de confirmação
            val alertaPagamento = AlertDialog.Builder(this)
                .setTitle("Confirmar Pagamento")
                .setMessage("Você deseja confirmar o pagamento?")
                .setPositiveButton("Confirmar") { dialog, which ->
                    lifecycleScope.launch {
                        mesaDao.atualizarStatusMesa(mesaId, "Fechada")
                        mesaDao.liberarMesa(mesaId)
                        insertCliente(clienteDao)
                    }
                    mostrarPagamentoConfirmado()
                }
                .setNegativeButton("Cancelar") { dialog, which ->
                    // Ação após cancelar o pagamento
                    dialog.dismiss() // Fecha o alerta
                }
                .create()

            alertaPagamento.show()
        }

    }


    // Método para exibir o popup de "Pagamento Confirmado"
    private fun mostrarPagamentoConfirmado() {
        val alertaConfirmacao = AlertDialog.Builder(this)
            .setTitle("Pagamento Confirmado")
            .setMessage("O pagamento foi confirmado com sucesso!")
            .setPositiveButton("OK") { dialog, which ->
                dialog.dismiss() // Fecha o alerta
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            .create()

        alertaConfirmacao.show()
    }

    private fun insertCliente(clienteDao: ClienteDao)
    {
        lifecycleScope.launch {
            // Inserindo pratos no banco de dados
            clienteDao.atualizarCliente(Cliente(clienteId = 0, mesaNumero = -1, status = "Livre"))
        }
    }




}
