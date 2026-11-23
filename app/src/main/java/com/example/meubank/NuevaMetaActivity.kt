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

class NuevaMetaActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nueva_meta)

        val etNombre = findViewById<EditText>(R.id.etNombreMeta)
        val etCantidad = findViewById<EditText>(R.id.etCantidadMeta)
        val etFecha = findViewById<EditText>(R.id.etFechaMeta)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarMeta)

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
                val idMeta = db.child("usuarios").child(uid).child("metas").push().key ?: "meta"

                val datosMeta = mapOf(
                    "tipo" to "meta",
                    "nombre" to nombre,
                    "cantidad" to cantidad,
                    "fecha" to fecha
                )

                db.child("usuarios").child(uid).child("metas").child(idMeta).setValue(datosMeta)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Ahorro guardado", Toast.LENGTH_SHORT).show()
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