package com.example.meubank

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class InicioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inicio)

        val tvTituloPantalla = findViewById<TextView>(R.id.tvTituloPantalla)
        tvTituloPantalla.text = "Inicio"

        val tvMensajeVacio = findViewById<TextView>(R.id.tvMensajeVacio)
        val textoCompleto = "Todavía no has registrado tus datos, presiona “agregar” para empezar a explorar la app"
        val spannable = SpannableString(textoCompleto)
        val inicioPalabra = textoCompleto.indexOf("agregar")
        val finPalabra = inicioPalabra + "agregar".length
        spannable.setSpan(
            ForegroundColorSpan(Color.parseColor("#D81B60")),
            inicioPalabra,
            finPalabra,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvMensajeVacio.text = spannable

        val btnAgregarCentral = findViewById<Button>(R.id.btnAgregar)
        btnAgregarCentral.setOnClickListener {
            Toast.makeText(this, "Agregar datos", Toast.LENGTH_SHORT).show()
            val btnAgregarCentral = findViewById<Button>(R.id.btnAgregar)
            btnAgregarCentral.setOnClickListener {
                val intent = Intent(this, AgregarDatosActivity::class.java)
                startActivity(intent)
            }
        }

        val navInicio = findViewById<ImageButton>(R.id.navInicio)
        val navAgregar = findViewById<ImageButton>(R.id.navAgregar)
        val navPerfil = findViewById<ImageButton>(R.id.navPerfil)

        resaltarIconoActivo(navInicio, listOf(navAgregar, navPerfil))

        navInicio.setOnClickListener {
        }

        navAgregar.setOnClickListener {
            Toast.makeText(this, "Agregar datos", Toast.LENGTH_SHORT).show()
            val navAgregar = findViewById<ImageButton>(R.id.navAgregar)
            navAgregar.setOnClickListener {
                val intent = Intent(this, AgregarDatosActivity::class.java)
                startActivity(intent)
            }
        }

        navPerfil.setOnClickListener {
            Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show()
            val navPerfil = findViewById<ImageButton>(R.id.navPerfil)
            navPerfil.setOnClickListener {
                val intent = Intent(this, PerfilActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun resaltarIconoActivo(activo: ImageButton, inactivos: List<ImageButton>) {
        activo.setBackgroundColor(Color.parseColor("#82D162"))
        activo.setColorFilter(Color.parseColor("#000000"))

        for (boton in inactivos) {
            boton.setBackgroundColor(Color.TRANSPARENT)
            boton.setColorFilter(Color.parseColor("#4A6B3C"))
        }
    }
}