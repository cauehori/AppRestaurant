package com.example.cliente2

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.cliente2.data.AppDatabase
import com.example.cliente2.data.Cliente
import com.example.cliente2.data.ClienteDao
import com.example.cliente2.data.Mesa
import com.example.cliente2.data.Prato
import com.example.cliente2.data.PratoDao
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.launch
import com.example.cliente2.data.mesaDao as mesaDao

class MainActivity : AppCompatActivity() {


    private val CAMERA_PERMISSION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val editText = findViewById<EditText>(R.id.editTextText) // Referência para o EditText
        val button_cont = findViewById<Button>(R.id.button2)

        val pratoDao: PratoDao by lazy {
            val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_database").build()
            db.pratoDao()
        }

        val mesaDao: mesaDao by lazy {
            val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_database").build()
            db.mesaDao()
        }

        val clienteDao: ClienteDao by lazy {
            val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_database").build()
            db.clienteDao()
        }

        val clienteId = 0

        lifecycleScope.launch {
            val pratosCount = pratoDao.contarPratos() // Chamada dentro da coroutine
            if (pratosCount == 0) {
                insertPratos(pratoDao)
            }
        }

        lifecycleScope.launch {
            val clienteCount = clienteDao.contarClientes() // Chamada dentro da coroutine
            if (clienteCount == 0) {
                insertCliente(clienteDao)
            }
        }

        lifecycleScope.launch {
            val statusCliente = clienteDao.getStatusById(clienteId)
        }

        /*
        lifecycleScope.launch {
            val mesasCount = mesaDao.contarMesas() // Chamada dentro da coroutine
            if (mesasCount == 0) {
                insertMesa(mesaDao)
            }
        }
        */

        button.setOnClickListener {
            if (hasCameraPermission()) {
                startBarcodeScanner(editText) // Passando o EditText como parâmetro
            } else {
                requestCameraPermission()
            }
        }

        button_cont.setOnClickListener {
            val textoMesa = editText.text.toString()

            lifecycleScope.launch {
                val statusCliente = clienteDao.getStatusById(clienteId)

                if (textoMesa == "Mesa 1" && statusCliente == "Livre") {
                    lifecycleScope.launch {
                        val mesasContNumero =
                            mesaDao.contarMesasNumero(1) // Chamada dentro da coroutine
                        if (mesasContNumero == 0) {
                            val num = 1
                            insertCliente1(clienteDao)
                            val intent = Intent(this@MainActivity, SegundaActivity::class.java)
                            intent.putExtra("numero", num)
                            startActivity(intent)
                        }
                        else {
                            // Exibir o AlertDialog informando que a Mesa 1 está ocupada
                            AlertDialog.Builder(this@MainActivity) // Substitua MainActivity pelo nome da sua Activity
                                .setTitle("Aviso")
                                .setMessage("A Mesa 1 já está ocupada. Caso você pertença a esssa mesa, clique CONTINUAR para proseeguior com os pedidos, caso contrário sente-se em outra mesa clicanco em ESPERAR")
                                .setPositiveButton("Continuar") { dialog, _ ->
                                    val num = 1
                                    insertCliente1(clienteDao)
                                    val intent =
                                        Intent(this@MainActivity, SegundaActivity::class.java)
                                    intent.putExtra("numero", num)
                                    startActivity(intent)
                                    dialog.dismiss() // Fecha o diálogo ao clicar no botão OK
                                }
                                .setNegativeButton("Esperar") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .create()
                                .show()
                        }
                    }

                } else if (textoMesa == "Mesa 2" && statusCliente == "Livre") {
                    lifecycleScope.launch {
                        val mesasContNumero =
                            mesaDao.contarMesasNumero(2) // Chamada dentro da coroutine
                        if (mesasContNumero == 0) {
                            val num = 2
                            insertCliente2(clienteDao)
                            val intent = Intent(this@MainActivity, SegundaActivity::class.java)
                            intent.putExtra("numero", num)
                            startActivity(intent)
                        } else {
                            // Exibir o AlertDialog informando que a Mesa 1 está ocupada
                            AlertDialog.Builder(this@MainActivity) // Substitua MainActivity pelo nome da sua Activity
                                .setTitle("Aviso")
                                .setMessage("A Mesa 2 já está ocupada. Caso você pertença a esssa mesa, clique CONTINUAR para proseeguior com os pedidos, caso contrário sente-se em outra mesa clicanco em ESPERAR")
                                .setPositiveButton("Continuar") { dialog, _ ->
                                    val num = 2
                                    insertCliente2(clienteDao)
                                    val intent =
                                        Intent(this@MainActivity, SegundaActivity::class.java)
                                    intent.putExtra("numero", num)
                                    startActivity(intent)
                                    dialog.dismiss() // Fecha o diálogo ao clicar no botão OK
                                }
                                .setNegativeButton("Esperar") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .create()
                                .show()
                        }
                    }

                } else if (textoMesa == "09723") {
                    val intent = Intent(this@MainActivity, FuncActivity::class.java)
                    startActivity(intent)
                }
                else if(statusCliente == "Ocupado") {
                    AlertDialog.Builder(this@MainActivity) // Substitua MainActivity pelo nome da sua Activity
                        .setTitle("Aviso")
                        .setMessage("Você já está em uma mesa, deseja retornar ao cardápio?")
                        .setPositiveButton("Continuar") { dialog, _ ->
                            // Aqui, ao invés de chamar diretamente o número da mesa, buscamos no banco
                            lifecycleScope.launch {
                                val numMesa = clienteDao.getNumeroMesaById(clienteId) // Aqui, você pega o número da mesa
                                val intent = Intent(this@MainActivity, SegundaActivity::class.java)
                                intent.putExtra("numero", numMesa) // Passando o número da mesa para a próxima activity
                                startActivity(intent)
                            }
                            dialog.dismiss() // Fecha o diálogo ao clicar no botão OK
                        }
                        .setNegativeButton("Esperar") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
                else {

                    // Caso contrário, exibe uma mensagem ou não faz nada
                    editText.error = "Coloque uma mesa válida"
                }
            }
        }
    }

    private fun insertPratos(pratoDao: PratoDao) {
        lifecycleScope.launch {
            // Inserindo pratos no banco de dados
            pratoDao.inserirPrato(Prato(nome = "Hosomaki", preco = 35.0))
            pratoDao.inserirPrato(Prato(nome = "Urumaki", preco = 35.0))
            pratoDao.inserirPrato(Prato(nome = "Temaki", preco = 25.0))
            pratoDao.inserirPrato(Prato(nome = "Refrigerante Cola", preco = 8.0))
        }
    }

    /*private fun insertMesa(mesaDao: mesaDao) {
        lifecycleScope.launch {
            // Inserindo pratos no banco de dados
            mesaDao.inserirMesa(Mesa(numero = 1, status = "Livre"))
            mesaDao.inserirMesa(Mesa(numero = 2, status = "Livre"))
        }
    }*/

    private fun insertCliente(clienteDao: ClienteDao)
    {
        lifecycleScope.launch {
            // Inserindo pratos no banco de dados
            clienteDao.inserirCliente(Cliente(clienteId = 0, mesaNumero = -1, status = "Livre"))
        }
    }

    private fun insertCliente2(clienteDao: ClienteDao)
    {
        lifecycleScope.launch {
            // Inserindo pratos no banco de dados
            clienteDao.atualizarCliente(Cliente(clienteId = 0, mesaNumero = 2, status = "Ocupado"))
        }
    }

    private fun insertCliente1(clienteDao: ClienteDao)
    {
        lifecycleScope.launch {
            // Inserindo pratos no banco de dados
            clienteDao.atualizarCliente(Cliente(clienteId = 0, mesaNumero = 1, status = "Ocupado"))
        }
    }



    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    private fun startBarcodeScanner(editText: EditText) {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .enableAutoZoom()
            .build()
        val scanner = GmsBarcodeScanning.getClient(this)

        scanner.startScan()
            .addOnSuccessListener { barcode ->
                val rawValue: String? = barcode.rawValue
                // Preenchendo o EditText com o valor do QR Code
                editText.setText(rawValue)
            }
            .addOnCanceledListener {
                // Scanner cancelado
            }
            .addOnFailureListener { e ->
                // Falha ao iniciar o scanner
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startBarcodeScanner(findViewById(R.id.editTextText)) // Iniciando o scanner após permissão
            } else {
                // Permissão negada pelo usuário
            }
        }
    }
}