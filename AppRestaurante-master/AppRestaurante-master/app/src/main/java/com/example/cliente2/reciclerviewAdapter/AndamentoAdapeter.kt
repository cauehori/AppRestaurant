package com.example.cliente2.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.cliente2.R
import com.example.cliente2.data.AppDatabase
import com.example.cliente2.data.Pedido
import com.example.cliente2.data.PedidoDao
import com.example.cliente2.data.PedidoPratoDao
import com.example.cliente2.data.PratoDao
import com.example.cliente2.data.mesaDao
import kotlinx.coroutines.launch

class AndamentoAdapeter(
    private val mesaList: List<Int>,
    private val pedidoDao: PedidoDao,
    private val pedidoPratoDao: PedidoPratoDao,
    private val pratoDao: PratoDao,
    private val mesaDao: mesaDao,
    private val lifecycleScope: LifecycleCoroutineScope // Recebe o lifecycleScope
) : RecyclerView.Adapter<AndamentoAdapeter.PedidoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lista_pedidos, parent, false)
        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {


        val mesa = mesaList[position]
        // Realizando a consulta assíncrona para obter as quantidades de pratos
        lifecycleScope.launch {
            val quantidade1 = pedidoPratoDao.somaPrato1(mesa) ?: 0
            val quantidade2 = pedidoPratoDao.somaPrato2(mesa) ?: 0
            val quantidade3 = pedidoPratoDao.somaPrato3(mesa) ?: 0
            val quantidade4 = pedidoPratoDao.somaPrato4(mesa) ?: 0

            //val pedidoList = pedidoDao.obterPedidosPorMesa(1)

            // Buscando os pratos pelo ID (de forma assíncrona também)
            //val prato1 = pratoDao.getPratoById(1)
            // val prato2 = pratoDao.getPratoById(2)
            // val prato3 = pratoDao.getPratoById(3)
            // val prato4 = pratoDao.getPratoById(4)

            // Atualizando a UI com as informações obtidas
            holder.tvMesa.text = "${mesaDao.obeterNumeroMesa(mesa)}"
            holder.tvMesaId.text = "$mesa"

            holder.Quantidade1.text = "$quantidade1"
            holder.Quantidade2.text = "$quantidade2"
            holder.Quantidade3.text = "$quantidade3"
            holder.Quantidade4.text = "$quantidade4"

            holder.Valor1.text = "R$ ${(quantidade1 * pratoDao.precoPrato(1))}"
            holder.Valor2.text = "R$ ${(quantidade2 * pratoDao.precoPrato(2))}"
            holder.Valor3.text = "R$ ${(quantidade3 * pratoDao.precoPrato(3))}"
            holder.Valor4.text = "R$ ${(quantidade4 * pratoDao.precoPrato(4))}"

            // Calculando o valor total
            val valorTotal = (quantidade1 * pratoDao.precoPrato(1)) +
                    (quantidade2 * pratoDao.precoPrato(2)) +
                    (quantidade3 * pratoDao.precoPrato(3)) +
                    (quantidade4 * pratoDao.precoPrato(4))
            holder.ValorTotal.text = "R$ $valorTotal"
        }
    }

    override fun getItemCount(): Int = mesaList.size

    class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMesa: TextView = itemView.findViewById(R.id.tvMesa)
        val tvMesaId: TextView = itemView.findViewById(R.id.tvMesaId)
        val Quantidade1: TextView = itemView.findViewById(R.id.Quantidade1)
        val Quantidade2: TextView = itemView.findViewById(R.id.Quantidade2)
        val Quantidade3: TextView = itemView.findViewById(R.id.Quantidade3)
        val Quantidade4: TextView = itemView.findViewById(R.id.Quantidade4)
        val Valor1: TextView = itemView.findViewById(R.id.Valor1)
        val Valor2: TextView = itemView.findViewById(R.id.Valor2)
        val Valor3: TextView = itemView.findViewById(R.id.Valor3)
        val Valor4: TextView = itemView.findViewById(R.id.Valor4)
        val ValorTotal: TextView = itemView.findViewById(R.id.ValorTotal)
    }
}
