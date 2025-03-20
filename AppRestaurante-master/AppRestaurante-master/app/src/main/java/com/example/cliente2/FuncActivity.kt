package com.example.cliente2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cliente2.connect.MqttHandler
import com.example.cliente2.ui.HistoricoPedidosActivity
import com.example.cliente2.ui.PedidosAndamento


class FuncActivity : AppCompatActivity() {

    private val BROKER_URL: String = "tcp://broker.emqx.io:1883"
    private val CLIENT_ID: String = "1"
    private var mqttHandler: MqttHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_func)

        //mqttHandler = MqttHandler()
        //mqttHandler!!.connect(BROKER_URL, CLIENT_ID)

        val btnToggle = findViewById<Button>(R.id.btnToggle)
        val anda = findViewById<Button>(R.id.button5)
        val hist = findViewById<Button>(R.id.button6)

        btnToggle.setOnClickListener {
            mqttHandler = MqttHandler()
            mqttHandler!!.connect(BROKER_URL, CLIENT_ID)
            publishMessage("/esteiraapi", "Mensagem app")
            mqttHandler!!.disconnect()
        }

        anda.setOnClickListener {
            val intent = Intent(this@FuncActivity, PedidosAndamento::class.java)
            startActivity(intent)
        }

        hist.setOnClickListener {
            val intent = Intent(this@FuncActivity, HistoricoPedidosActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onDestroy() {
     //   mqttHandler!!.disconnect()
        super.onDestroy()
    }

    private fun publishMessage(topic: String, message: String) {
        Toast.makeText(this, "Publishing message: $message", Toast.LENGTH_SHORT).show()
        mqttHandler!!.publish(topic, message)
    }

    private fun subscribeToTopic(topic: String) {
        Toast.makeText(this, "Subscribing to topic $topic", Toast.LENGTH_SHORT).show()
        mqttHandler!!.subscribe(topic)
    }

}
