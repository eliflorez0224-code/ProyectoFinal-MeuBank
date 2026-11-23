package com.example.meubank

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val textoAnimado = "MeuBank"
    private var index = 0
    private lateinit var tvMeuBank: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantallaprincipal)

        tvMeuBank = findViewById(R.id.tvMeuBank)

        iniciarAnimacionEscritura()

        val tvIniciarsesion = findViewById<TextView>(R.id.tvIniciarSesion)
        tvIniciarsesion.setOnClickListener {
            val intent = Intent(this, IniciarsesionActivity::class.java )
            startActivity(intent)
        }

        val tvRegistrate = findViewById<TextView>(R.id.tvRegistrate)
        tvRegistrate.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java )
            startActivity(intent)
        }
    }

    private fun iniciarAnimacionEscritura() {
        index = 0
        tvMeuBank.text = ""
        val handler = Handler(Looper.getMainLooper())

        val runnable = object : Runnable {
            override fun run() {
                if (index < textoAnimado.length) {
                    tvMeuBank.text = tvMeuBank.text.toString() + textoAnimado[index]
                    index++
                    handler.postDelayed(this, 90)
                }
            }
        }
        handler.post(runnable)
    }
}