package com.example.meubank

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class InicioDatosActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference

    private lateinit var tvDineroTotal: TextView
    private lateinit var tvTusAhorros: TextView
    private lateinit var tvIngresosRecientes: TextView
    private lateinit var tvGastosRecientes: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inicio_datos)

        tvDineroTotal = findViewById(R.id.tvDineroTotal)
        tvTusAhorros = findViewById(R.id.tvTusAhorros)
        tvIngresosRecientes = findViewById(R.id.tvIngresosRecientes)
        tvGastosRecientes = findViewById(R.id.tvGastosRecientes)

        calcularBalanceUsuario()

        findViewById<Button>(R.id.btnAgregarCentral).setOnClickListener {
            startActivity(Intent(this, AgregarDatosActivity::class.java))
        }

        val navInicio = findViewById<ImageButton>(R.id.navInicio)
        val navAgregar = findViewById<ImageButton>(R.id.navAgregar)
        val navPerfil = findViewById<ImageButton>(R.id.navPerfil)

        resaltarIconoActivo(navInicio, listOf(navAgregar, navPerfil))

        navAgregar.setOnClickListener {
            startActivity(Intent(this, AgregarDatosActivity::class.java))
        }

        navPerfil.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
            finish()
        }
    }

    private fun calcularBalanceUsuario() {
        val uid = auth.currentUser?.uid ?: return

        var sumaIngresos = 0.0
        var sumaGastos = 0.0
        var sumaAhorros = 0.0
        var ultimoIngreso = 0.0
        var ultimoGasto = 0.0

        db.child("usuarios").child(uid).child("registros").get()
            .addOnSuccessListener { snapshotRegistros ->
                if (snapshotRegistros.exists()) {
                    for (registro in snapshotRegistros.children) {
                        val tipo = registro.child("tipo").value?.toString() ?: ""
                        val cantidad = registro.child("cantidad").value?.toString()?.toDoubleOrNull() ?: 0.0

                        if (tipo == "ingreso") {
                            sumaIngresos += cantidad
                            ultimoIngreso = cantidad
                        } else if (tipo == "gasto") {
                            sumaGastos += cantidad
                            ultimoGasto = cantidad
                        }
                    }
                }

                db.child("usuarios").child(uid).child("metas").get()
                    .addOnSuccessListener { snapshotMetas ->
                        if (snapshotMetas.exists()) {
                            for (meta in snapshotMetas.children) {
                                val cantidadMeta = meta.child("cantidad").value?.toString()?.toDoubleOrNull() ?: 0.0
                                sumaAhorros += cantidadMeta
                            }
                        }

                        val dineroTotal = (sumaIngresos + sumaAhorros) - sumaGastos

                        tvDineroTotal.text = "Dinero total: ${formatearNumero(dineroTotal)}"
                        tvTusAhorros.text = "Tus ahorros: ${formatearNumero(sumaAhorros)}"
                        tvIngresosRecientes.text = "Ingresos recientes: ${formatearNumero(ultimoIngreso)}"
                        tvGastosRecientes.text = "Gastos recientes: ${formatearNumero(ultimoGasto)}"
                    }
            }
    }

    private fun formatearNumero(valor: Double): String {
        return if (valor % 1.0 == 0.0) {
            valor.toLong().toString()
        } else {
            valor.toString()
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