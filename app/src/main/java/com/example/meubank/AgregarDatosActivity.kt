package com.example.meubank

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AgregarDatosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.agregar_datos)

        val tvTituloPantalla = findViewById<TextView>(R.id.tvTituloPantalla)
        tvTituloPantalla.text = "Agregar Datos"

        val btnNuevoIngreso = findViewById<Button>(R.id.btnNuevoIngreso)
        val btnNuevoGasto = findViewById<Button>(R.id.btnNuevoGasto)
        val btnNuevaMeta = findViewById<Button>(R.id.btnNuevaMeta)

        btnNuevoIngreso.setOnClickListener {
            Toast.makeText(this, "Nuevo Ingreso", Toast.LENGTH_SHORT).show()
            val btnNuevoIngreso = findViewById<Button>(R.id.btnNuevoIngreso)
            btnNuevoIngreso.setOnClickListener {
                val intent = Intent(this, NuevoIngresoActivity::class.java)
                startActivity(intent)
            }
        }

        btnNuevoGasto.setOnClickListener {
            Toast.makeText(this, "Nuevo Gasto", Toast.LENGTH_SHORT).show()
            val btnNuevoGasto = findViewById<Button>(R.id.btnNuevoGasto)
            btnNuevoGasto.setOnClickListener {
                val intent = Intent(this, NuevoGastoActivity::class.java)
                startActivity(intent)
            }
        }

        btnNuevaMeta.setOnClickListener {
            Toast.makeText(this, "Nuevo ahorro", Toast.LENGTH_SHORT).show()
            val btnNuevaMeta = findViewById<Button>(R.id.btnNuevaMeta)
            btnNuevaMeta.setOnClickListener {
                val intent = Intent(this, NuevaMetaActivity::class.java)
                startActivity(intent)
            }
        }

        val navInicio = findViewById<ImageButton>(R.id.navInicio)
        val navAgregar = findViewById<ImageButton>(R.id.navAgregar)
        val navPerfil = findViewById<ImageButton>(R.id.navPerfil)

        resaltarIconoActivo(navAgregar, listOf(navInicio, navPerfil))

        navInicio.setOnClickListener {
            val intent = Intent(this, InicioDatosActivity::class.java)
            startActivity(intent)
            finish()
        }

        navAgregar.setOnClickListener {
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