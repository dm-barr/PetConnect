package com.petconnect.app

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.petconnect.app.model.Mascota

class PublicarActivity : AppCompatActivity() {

    private var imagenUri: Uri? = null
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Esto abre la galería del celular para elegir una foto
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imagenUri = it
            findViewById<ImageView>(R.id.ivPreview).setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publicar)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etEspecie = findViewById<EditText>(R.id.etEspecie)
        val etRaza = findViewById<EditText>(R.id.etRaza)
        val etEdad = findViewById<EditText>(R.id.etEdad)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcion)
        val btnSeleccionar = findViewById<Button>(R.id.btnSeleccionarImagen)
        val btnPublicar = findViewById<Button>(R.id.btnPublicar)

        btnSeleccionar.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        btnPublicar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val especie = etEspecie.text.toString()
            val raza = etRaza.text.toString()
            val edad = etEdad.text.toString()
            val descripcion = etDescripcion.text.toString()

            // Validar que todo esté lleno
            if (nombre.isEmpty() || especie.isEmpty() || raza.isEmpty() || edad.isEmpty() || descripcion.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (imagenUri == null) {
                Toast.makeText(this, "Por favor selecciona una fotografía", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Deshabilitar botón para evitar doble clic
            btnPublicar.isEnabled = false
            btnPublicar.text = "Subiendo..."

            subirImagenYGuardar(nombre, especie, raza, edad, descripcion, btnPublicar)
        }
    }

    private fun subirImagenYGuardar(nombre: String, especie: String, raza: String, edad: String, descripcion: String, btnPublicar: Button) {
        // 1. Subir la imagen a Firebase Storage
        val ref = storage.reference.child("mascotas/${System.currentTimeMillis()}.jpg")
        ref.putFile(imagenUri!!)
            .addOnSuccessListener {
                // 2. Si la imagen se subió, obtener su enlace URL
                ref.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()

                    // 3. Guardar los datos en Firestore
                    guardarEnFirestore(nombre, especie, raza, edad, descripcion, imageUrl, btnPublicar)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                btnPublicar.isEnabled = true
                btnPublicar.text = "Publicar Mascota"
            }
    }

    private fun guardarEnFirestore(nombre: String, especie: String, raza: String, edad: String, descripcion: String, imageUrl: String, btnPublicar: Button) {
        val refugioId = auth.currentUser?.uid ?: "desconocido"

        // Creamos el objeto Mascota
        val nuevaMascota = Mascota(
            nombre = nombre,
            especie = especie,
            raza = raza,
            edad = edad,
            descripcion = descripcion,
            imageUrl = imageUrl,
            refugioId = refugioId
        )

        // Guardamos en la colección "mascotas"
        db.collection("mascotas")
            .add(nuevaMascota) // Firebase le asignará un ID automático
            .addOnSuccessListener {
                Toast.makeText(this, "¡Mascota publicada con éxito!", Toast.LENGTH_SHORT).show()
                finish() // Cierra esta pantalla y vuelve a la principal
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_SHORT).show()
                btnPublicar.isEnabled = true
                btnPublicar.text = "Publicar Mascota"
            }
    }
}