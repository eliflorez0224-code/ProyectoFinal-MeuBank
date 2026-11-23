package com.example.meubank

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database

class IniciarsesionActivity : AppCompatActivity() {

        private val auth = FirebaseAuth.getInstance()
        private val db = Firebase.database.reference

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(R.layout.iniciousuarios)

            val etCorreo = findViewById<EditText>(R.id.etCorreoinicio)
            val etClave = findViewById<EditText>(R.id.etClaveinicio)
            val btnIngresar = findViewById<Button>(R.id.btnIngresarinicio)
            val tvEstado = findViewById<TextView>(R.id.tvEstadoinicio)

            btnIngresar.setOnClickListener {
                val correo = etCorreo.text.toString().trim()
                val clave = etClave.text.toString()

                if (correo.isEmpty() || clave.isEmpty()) {
                    tvEstado.text = "Escribe correo y contraseña para ingresar"
                    return@setOnClickListener
                }

                auth.signInWithEmailAndPassword(correo, clave)
                    .addOnSuccessListener { resultado ->
                        val uid = resultado.user!!.uid

                        db.child("usuarios").child(uid).child("nombre").get()
                            .addOnSuccessListener { dato ->
                                val nombre = dato.getValue(String::class.java)
                                tvEstado.text = "¡Bienvenido, $nombre!"

                                db.child("usuarios").child(uid).child("registros").get()
                                    .addOnSuccessListener { snapshot ->
                                        if (!snapshot.exists()) {
                                            val intent = Intent(
                                                this,
                                                InicioActivity::class.java
                                            )
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            val intent = android.content.Intent(
                                            this, InicioDatosActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                    }
                                    .addOnFailureListener {
                                        val intent =
                                            Intent(this, InicioActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                            }
                    }
                    .addOnFailureListener {
                        tvEstado.text = "Correo o contraseña incorrectos"
                    }
            }
            val btnVolver = findViewById<Button>(R.id.btnVolver)
            btnVolver.setOnClickListener {
                finish()
            }
        }
    }