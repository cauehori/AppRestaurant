package com.example.cliente2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.cliente2.data.AppDatabase
import com.example.cliente2.data.ClienteDao
import com.example.cliente2.data.Mesa
import com.example.cliente2.data.Pedido
import com.example.cliente2.data.PedidoDao
import com.example.cliente2.data.PedidoPrato
import com.example.cliente2.data.PedidoPratoDao
import com.example.cliente2.data.Prato
import com.example.cliente2.data.PratoDao
import com.example.cliente2.data.mesaDao
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SegundaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_cardapio)

        val num = intent.getIntExtra("numero", -1) // -1 é o valor padrão caso não tenha sido enviado

        // Verifica se o mesaId foi passado corretamente
        if (num != -1) {
            // Use o mesaId para carregar as informações da mesa ou associar pedidos a ela
            println("Numero Mesa: $num")
        } else {
            // Caso o mesaId não tenha sido passado corretamente
            println("Mesa não encontrada")
        }

        val aumentar1 = findViewById<Button>(R.id.btnIncreaseItem1)
        val aumentar2 = findViewById<Button>(R.id.btnIncreaseItem2)
        val aumentar3 = findViewById<Button>(R.id.btnIncreaseItem3)
        val aumentar4 = findViewById<Button>(R.id.btnIncreaseItem4)

        val diminuir1 = findViewById<Button>(R.id.btnDecreaseItem1)
        val diminuir2 = findViewById<Button>(R.id.btnDecreaseItem2)
        val diminuir3 = findViewById<Button>(R.id.btnDecreaseItem3)
        val diminuir4 = findViewById<Button>(R.id.btnDecreaseItem4)

        val text1 = findViewById<TextView>(R.id.tvQuantityItem1)
        val text2 = findViewById<TextView>(R.id.tvQuantityItem2)
        val text3 = findViewById<TextView>(R.id.tvQuantityItem3)
        val text4 = findViewById<TextView>(R.id.tvQuantityItem4)

        val confirmar = findViewById<Button>(R.id.btnConfirmOrder)

        var auxiliar = 0;

        var cont1 = 0
        var cont2 = 0
        var cont3 = 0
        var cont4 = 0

        var cont1_cp = 0
        var cont2_cp = 0
        var cont3_cp = 0
        var cont4_cp = 0

        val buttonVerconta = findViewById<Button>(R.id.btnVerConta)

        val clienteId = 0

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

        val clienteDao: ClienteDao by lazy {
            val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_database").build()
            db.clienteDao()
        }


        fun insertPedidoPrato(pedidoId: Long) {
            lifecycleScope.launch {
                // Inserindo pratos no banco de dados
                pedidoPratoDao.inserirPedidoPrato(PedidoPrato(pedidoId = pedidoId, pratoId = 1, quantidade = cont1_cp))
                pedidoPratoDao.inserirPedidoPrato(PedidoPrato(pedidoId = pedidoId, pratoId = 2, quantidade = cont2_cp))
                pedidoPratoDao.inserirPedidoPrato(PedidoPrato(pedidoId = pedidoId, pratoId = 3, quantidade = cont3_cp))
                pedidoPratoDao.inserirPedidoPrato(PedidoPrato(pedidoId = pedidoId, pratoId = 4, quantidade = cont4_cp))
            }
        }


        fun insertPedido(pedidoDao: PedidoDao): Job {
            return lifecycleScope.launch {
                val mesasContNumero = mesaDao.contarMesasNumero(num)
                if(mesasContNumero == 0){
                    val mesaId = mesaDao.inserirMesa(Mesa(numero = num, mesaNumero = num, status = "Ocupado"))
                }
                auxiliar = mesaDao.pegarIdMesa(num)
                val pedidoId = pedidoDao.inserirPedido(Pedido(mesaId = auxiliar, status = "Ocupado"))
                // Inserindo pratos no banco de dados
                insertPedidoPrato(pedidoId)

            }
        }

        suspend fun insertMesa(mesaDao: mesaDao){
            mesaDao.inserirMesa(Mesa(numero = num, mesaNumero = num, status = "Ocupado"))
        }

        // Incrementar quantidade do item 1
        aumentar1.setOnClickListener {
            cont1++
            text1.text = cont1.toString()

        }

        // Incrementar quantidade do item 2
        aumentar2.setOnClickListener {
            cont2++
            text2.text = cont2.toString()

        }

        // Incrementar quantidade do item 3
        aumentar3.setOnClickListener {
            cont3++
            text3.text = cont3.toString()

        }

        // Incrementar quantidade do item 4
        aumentar4.setOnClickListener {
            cont4++
            text4.text = cont4.toString()

        }

        // Decrementar quantidade do item 1
        diminuir1.setOnClickListener {
            if (cont1 > 0) {
                cont1--
                text1.text = cont1.toString()

            }
        }

        // Decrementar quantidade do item 2
        diminuir2.setOnClickListener {
            if (cont2 > 0) {
                cont2--
                text2.text = cont2.toString()

            }
        }

        // Decrementar quantidade do item 3
        diminuir3.setOnClickListener {
            if (cont3 > 0) {
                cont3--
                text3.text = cont3.toString()

            }
        }

        // Decrementar quantidade do item 4
        diminuir4.setOnClickListener {
            if (cont4 > 0) {
                cont4--
                text4.text = cont4.toString()

            }
        }



        // Ao clicar no botão "Confirmar Pedido"
        confirmar.setOnClickListener {
            // Criando uma mensagem para o AlertDialog
            val pedido = StringBuilder()
            if (cont1 > 0) pedido.append("Hosomaki: $cont1 unidades\n")
            if (cont2 > 0) pedido.append("Urumaki: $cont2 unidades\n")
            if (cont3 > 0) pedido.append("Temaki: $cont3 unidades\n")
            if (cont4 > 0) pedido.append("Refrigerante Cola: $cont4 unidades\n")

            //insertPedido(pedidoDao)
            // insertPedidoPrato()


            // Verificar se há itens no pedido
            if (pedido.isNotEmpty()) {
                // Criar um AlertDialog
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("Confirmar Pedido")
                    .setMessage("Você deseja confirmar o seguinte pedido?\n\n$pedido")
                    .setPositiveButton("Confirmar") { dialog, which ->
                        // Lógica para confirmar o pedido
                        cont1_cp = cont1
                        cont2_cp = cont2
                        cont3_cp = cont3
                        cont4_cp = cont4
                        insertPedido(pedidoDao)



                        cont1 = 0
                        text1.text = cont1.toString()
                        cont2 = 0
                        text2.text = cont2.toString()
                        cont3 = 0
                        text3.text = cont3.toString()
                        cont4 = 0
                        text4.text = cont4.toString()
                        // Aqui você pode adicionar o que acontece após o usuário confirmar
                    }
                    .setNegativeButton("Cancelar") { dialog, which ->
                        // Lógica para cancelar o pedido
                        // O pedido não é confirmado
                    }
                    .create()

                // Exibir o AlertDialog
                alertDialog.show()
            } else {
                // Se o pedido estiver vazio, mostrar uma mensagem de erro
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("Erro")
                    .setMessage("Por favor, adicione itens ao pedido antes de confirmar.")
                    .setPositiveButton("OK", null)
                    .create()

                alertDialog.show()
            }
        }

        buttonVerconta.setOnClickListener{
                lifecycleScope.launch {
                auxiliar = mesaDao.pegarIdMesa(num)
                    val intent = Intent(this@SegundaActivity, PagamentoActivity::class.java)
                    intent.putExtra("mesaId", auxiliar)
                    startActivity(intent)
            }
        }

    }

}
