package com.example.meubank

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class NuevoIngresoActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nuevo_ingreso)

        val etNombre = findViewById<EditText>(R.id.etNombreIngreso)
        val etCantidad = findViewById<EditText>(R.id.etCantidadIngreso)
        val etFecha = findViewById<EditText>(R.id.etFechaIngreso)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarIngreso)

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

                val idRegistro = db.child("usuarios").child(uid).child("registros").push().key ?: "ingreso"

                val datosIngreso = mapOf(
                    "tipo" to "ingreso",
                    "nombre" to nombre,
                    "cantidad" to cantidad,
                    "fecha" to fecha
                )

                db.child("usuarios").child(uid).child("registros").child(idRegistro).setValue(datosIngreso)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Ingreso guardado", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, InicioDatosActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { e ->
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