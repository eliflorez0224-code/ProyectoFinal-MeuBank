package com.example.meubank

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PerfilActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase

    private lateinit var tvNombreUsuario: TextView
    private lateinit var tvCorreo: TextView
    private lateinit var tvEdad: TextView
    private lateinit var tvTelefono: TextView
    private lateinit var imgFotoPerfil: ImageView

    private val seleccionarGaleria = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imgFotoPerfil.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.perfil)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()

        tvNombreUsuario = findViewById(R.id.tvNombreUsuario)
        tvCorreo = findViewById(R.id.tvCorreo)
        tvEdad = findViewById(R.id.tvEdad)
        tvTelefono = findViewById(R.id.tvTelefono)
        imgFotoPerfil = findViewById(R.id.imgFotoPerfil)

        cargarDatosUsuario()

        imgFotoPerfil.setOnClickListener {
            seleccionarGaleria.launch("image/*")
        }

        tvEdad.setOnClickListener {
            mostrarDialogoEdicion("Edad", "edad")
        }

        tvTelefono.setOnClickListener {
            mostrarDialogoEdicion("Número", "telefono")
        }

        val navInicio = findViewById<ImageButton>(R.id.navInicio)
        val navAgregar = findViewById<ImageButton>(R.id.navAgregar)
        val navPerfil = findViewById<ImageButton>(R.id.navPerfil)

        resaltarIconoActivo(navPerfil, listOf(navInicio, navAgregar))

        navInicio.setOnClickListener {
            startActivity(Intent(this, InicioDatosActivity::class.java))
            finish()
        }

        navAgregar.setOnClickListener {
            startActivity(Intent(this, AgregarDatosActivity::class.java))
            finish()
        }

        navPerfil.setOnClickListener {
        }
    }

    private fun cargarDatosUsuario() {
        val user = auth.currentUser
        user?.let {
            val uid = it.uid
            val correoAuth = it.email ?: "Sin correo"
            tvCorreo.text = "Correo: $correoAuth"

            db.reference.child("usuarios").child(uid).get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val nombre = snapshot.child("nombre").value.toString()
                        tvNombreUsuario.text = nombre

                        val edad = snapshot.child("edad").value?.toString()
                        if (!edad.isNullOrEmpty()) {
                            tvEdad.text = "Edad: $edad años"
                        }

                        val telefono = snapshot.child("telefono").value?.toString()
                        if (!telefono.isNullOrEmpty()) {
                            tvTelefono.text = "Número: $telefono"
                        }
                    }
                }
        }
    }

    private fun mostrarDialogoEdicion(titulo: String, campo: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Actualizar $titulo")

        val input = EditText(this)
        input.hint = "Ingresa tu $titulo"
        builder.setView(input)

        builder.setPositiveButton("Guardar") { _, _ ->
            val nuevoValor = input.text.toString().trim()
            if (nuevoValor.isNotEmpty()) {
                val uid = auth.currentUser?.uid
                if (uid != null) {
                    db.reference.child("usuarios").child(uid).child(campo).setValue(nuevoValor)
                        .addOnSuccessListener {
                            Toast.makeText(this, "$titulo actualizado", Toast.LENGTH_SHORT).show()
                            cargarDatosUsuario()
                        }
                }
            }
        }
        builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }
        builder.show()
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