package com.petconnect.app

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.petconnect.app.model.Mascota

class PublicarActivity : AppCompatActivity() {

    private var imagenUri: Uri? = null
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imagenUri = it
            ivPreview.setImageURI(it)
            ivPreview.visibility = View.VISIBLE
            layoutPlaceholder.visibility = View.GONE
        }
    }

    private lateinit var ivPreview: ImageView
    private lateinit var layoutPlaceholder: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publicar)

        val btnVolver = findViewById<ImageButton>(R.id.btnVolver)
        ivPreview = findViewById(R.id.ivPreview)
        layoutPlaceholder = findViewById(R.id.layoutPlaceholder)
        val etNombre = findViewById<TextInputEditText>(R.id.etNombre)
        val etEspecie = findViewById<TextInputEditText>(R.id.etEspecie)
        val etRaza = findViewById<TextInputEditText>(R.id.etRaza)
        val etEdad = findViewById<TextInputEditText>(R.id.etEdad)
        val etDescripcion = findViewById<TextInputEditText>(R.id.etDescripcion)
        val btnPublicar = findViewById<MaterialButton>(R.id.btnPublicar)

        // Clic en el área de preview/placeholder abre la galería
        val abrirGaleria = View.OnClickListener {
            pickImageLauncher.launch("image/*")
        }
        ivPreview.setOnClickListener(abrirGaleria)
        layoutPlaceholder.setOnClickListener(abrirGaleria)

        // Botón volver
        btnVolver.setOnClickListener {
            finish()
        }

        btnPublicar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val especie = etEspecie.text.toString().trim()
            val raza = etRaza.text.toString().trim()
            val edad = etEdad.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()

            // Validar campos
            when {
                nombre.isEmpty() -> {
                    etNombre.error = "Ingresa el nombre"
                    etNombre.requestFocus()
                    return@setOnClickListener
                }
                especie.isEmpty() -> {
                    etEspecie.error = "Ingresa la especie"
                    etEspecie.requestFocus()
                    return@setOnClickListener
                }
                raza.isEmpty() -> {
                    etRaza.error = "Ingresa la raza"
                    etRaza.requestFocus()
                    return@setOnClickListener
                }
                edad.isEmpty() -> {
                    etEdad.error = "Ingresa la edad"
                    etEdad.requestFocus()
                    return@setOnClickListener
                }
                descripcion.isEmpty() -> {
                    etDescripcion.error = "Ingresa una descripción"
                    etDescripcion.requestFocus()
                    return@setOnClickListener
                }
                imagenUri == null -> {
                    Toast.makeText(this, "Por favor selecciona una fotografía", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // Deshabilitar botón para evitar doble clic
            btnPublicar.isEnabled = false
            btnPublicar.text = "Subiendo..."

            subirImagenYGuardar(nombre, especie, raza, edad, descripcion, btnPublicar)
        }
    }

    private fun subirImagenYGuardar(
        nombre: String,
        especie: String,
        raza: String,
        edad: String,
        descripcion: String,
        btnPublicar: MaterialButton
    ) {
        val ref = storage.reference.child("mascotas/${System.currentTimeMillis()}.jpg")
        ref.putFile(imagenUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    guardarEnFirestore(nombre, especie, raza, edad, descripcion, imageUrl, btnPublicar)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                btnPublicar.isEnabled = true
                btnPublicar.text = "Publicar mascota"
            }
    }

    private fun guardarEnFirestore(
        nombre: String,
        especie: String,
        raza: String,
        edad: String,
        descripcion: String,
        imageUrl: String,
        btnPublicar: MaterialButton
    ) {
        val refugioId = auth.currentUser?.uid ?: "desconocido"

        val nuevaMascota = Mascota(
            nombre = nombre,
            especie = especie,
            raza = raza,
            edad = edad,
            descripcion = descripcion,
            imageUrl = imageUrl,
            refugioId = refugioId
        )

        db.collection("mascotas")
            .add(nuevaMascota)
            .addOnSuccessListener {
                Toast.makeText(this, "¡Mascota publicada con éxito!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_SHORT).show()
                btnPublicar.isEnabled = true
                btnPublicar.text = "Publicar mascota"
            }
    }
}