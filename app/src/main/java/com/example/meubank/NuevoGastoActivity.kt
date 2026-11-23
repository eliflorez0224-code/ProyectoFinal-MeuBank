package com.example.meubank

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class NuevoGastoActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nuevo_gasto)

        val etNombre = findViewById<EditText>(R.id.etNombreGasto)
        val etCantidad = findViewById<EditText>(R.id.etCantidadGasto)
        val etFecha = findViewById<EditText>(R.id.etFechaGasto)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarGasto)

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val cantidadStr = etCantidad.text.toString().trim()
            val fecha = etFecha.text.toString().trim()

            if (nombre.isEmpty() || cantidadStr.isEmpty() || fecha.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cantidad = cantidadStr.toDoubleOrNull()
            if (cantidad == null) {
                Toast.makeText(this, "Ingresa una cantidad valida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val uid = auth.currentUser?.uid
            if (uid != null) {
                val idRegistro = db.child("usuarios").child(uid).child("registros").push().key ?: "gasto"

                val datosGasto = mapOf(
                    "tipo" to "gasto",
                    "nombre" to nombre,
                    "cantidad" to cantidad,
                    "fecha" to fecha
                )

                db.child("usuarios").child(uid).child("registros").child(idRegistro).setValue(datosGasto)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Gasto guardado", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, InicioDatosActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { e: Exception ->
                        Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        findViewById<ImageButton>(R.id.navInicio).setOnClickListener {
            startActivity(Intent(this, InicioDatosActivity::class.java))
            finish()
        }
        findViewById<ImageButton>(R.id.navAgregar).setOnClickListener {
            startActivity(Intent(this, AgregarDatosActivity::class.java))
            finish()
        }
        findViewById<ImageButton>(R.id.navPerfil).setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
            finish()
        }
    }
}