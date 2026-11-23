package com.example.meubank

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistroActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registrousuarios)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etClave = findViewById<EditText>(R.id.etClave)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val tvEstado = findViewById<TextView>(R.id.tvEstado)

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val clave = etClave.text.toString()

            if (nombre.isEmpty() || correo.isEmpty() || clave.length < 6) {
                tvEstado.text = "Llena todos los campos (clave mínimo 6)"
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(correo, clave)
                .addOnSuccessListener { resultado ->
                    val uid = resultado.user!!.uid

                    val usuario = mapOf(
                        "nombre" to nombre,
                        "correo" to correo
                    )

                    db.child("usuarios").child(uid).setValue(usuario)
                        .addOnSuccessListener {
                            tvEstado.text = "Usuario registrado: $nombre"
                            db.child("usuarios").child(uid).setValue(usuario)
                                .addOnSuccessListener {
                                    db.child("usuarios").child(uid).child("registros").get()
                                        .addOnSuccessListener { snapshot ->
                                            if (!snapshot.exists()) {
                                                val intent = android.content.Intent(this, InicioActivity::class.java)
                                                startActivity(intent)
                                                finish()
                                            }
                                        }
                                }
                        }
                        .addOnFailureListener { e ->
                            tvEstado.text = "Error al guardar: ${e.message}"
                        }
                }
                .addOnFailureListener { e ->
                    tvEstado.text = "Error al registrar: ${e.message}"
                }
        }
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish()
        }
    }
}