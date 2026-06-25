package com.petconnect.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DetalleActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var mascotaId: String = ""
    private var esFavorito = false
    private var nombreMascota: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        // Recibir los datos de la mascota desde el Intent
        nombreMascota = intent.getStringExtra("nombre") ?: ""
        val especie = intent.getStringExtra("especie") ?: ""
        val raza = intent.getStringExtra("raza") ?: ""
        val edad = intent.getStringExtra("edad") ?: ""
        val descripcion = intent.getStringExtra("descripcion") ?: ""
        val imageUrl = intent.getStringExtra("imageUrl") ?: ""
        mascotaId = intent.getStringExtra("id") ?: ""

        // Encontrar las vistas
        val ivMascota = findViewById<ImageView>(R.id.ivMascotaGrande)
        val tvNombre = findViewById<TextView>(R.id.tvNombreDetalle)
        val tvEspecie = findViewById<TextView>(R.id.tvEspecieDetalle)
        val tvRaza = findViewById<TextView>(R.id.tvRazaDetalle)
        val tvEdad = findViewById<TextView>(R.id.tvEdadDetalle)
        val tvDescripcion = findViewById<TextView>(R.id.tvDescripcionDetalle)
        val btnFavorito = findViewById<ImageButton>(R.id.btnFavorito)
        val btnAdoptar = findViewById<MaterialButton>(R.id.buttonAdoptar)

        // Cargar los datos en las vistas
        tvNombre.text = nombreMascota
        tvEspecie.text = especie
        tvRaza.text = raza
        tvEdad.text = edad
        tvDescripcion.text = descripcion

        // Cargar la imagen usando Coil
        if (imageUrl.isNotEmpty()) {
            ivMascota.load(imageUrl) {
                crossfade(true)
                placeholder(android.R.drawable.ic_menu_gallery)
                error(android.R.drawable.ic_menu_gallery)
            }
        } else {
            ivMascota.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        // Verificar si ya es favorito
        verificarFavorito(btnFavorito)

        // Manejar el clic en el botón de favoritos
        btnFavorito.setOnClickListener {
            if (esFavorito) {
                eliminarFavorito(btnFavorito)
            } else {
                agregarFavorito(btnFavorito)
            }
        }

        // Botón solicitar adopción → Abre WhatsApp Web en Chrome
        btnAdoptar.setOnClickListener {
            abrirWhatsAppAdopcion()
        }
    }

    private fun verificarFavorito(btnFavorito: ImageButton) {
        val userId = auth.currentUser?.uid ?: return

        db.collection("usuarios")
            .document(userId)
            .collection("favoritos")
            .document(mascotaId)
            .get()
            .addOnSuccessListener { document ->
                esFavorito = document.exists()
                actualizarIconoFavorito(btnFavorito)
            }
    }

    private fun agregarFavorito(btnFavorito: ImageButton) {
        val userId = auth.currentUser?.uid ?: run {
            Toast.makeText(this, "Debes iniciar sesión para agregar favoritos", Toast.LENGTH_SHORT).show()
            return
        }

        val favoritoData = hashMapOf(
            "mascotaId" to mascotaId,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("usuarios")
            .document(userId)
            .collection("favoritos")
            .document(mascotaId)
            .set(favoritoData)
            .addOnSuccessListener {
                esFavorito = true
                actualizarIconoFavorito(btnFavorito)
                Toast.makeText(this, "Agregado a favoritos", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al agregar a favoritos", Toast.LENGTH_SHORT).show()
            }
    }

    private fun eliminarFavorito(btnFavorito: ImageButton) {
        val userId = auth.currentUser?.uid ?: return

        db.collection("usuarios")
            .document(userId)
            .collection("favoritos")
            .document(mascotaId)
            .delete()
            .addOnSuccessListener {
                esFavorito = false
                actualizarIconoFavorito(btnFavorito)
                Toast.makeText(this, "Eliminado de favoritos", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al eliminar de favoritos", Toast.LENGTH_SHORT).show()
            }
    }

    private fun actualizarIconoFavorito(btnFavorito: ImageButton) {
        if (esFavorito) {
            btnFavorito.setImageResource(android.R.drawable.btn_star_big_on)
        } else {
            btnFavorito.setImageResource(android.R.drawable.btn_star_big_off)
        }
    }

    /**
     * Abre WhatsApp Web en el navegador (Chrome) con un mensaje predefinido.
     * El usuario puede elegir abrirlo con Chrome o con la app de WhatsApp si la tiene.
     */
    private fun abrirWhatsAppAdopcion() {
        val numeroTelefono = "51952704345" // Número sin el +
        val mensaje = "¡Hola! 👋\n\nEstoy interesado en adoptar a *$nombreMascota* que vi en la app PetConnect. " +
                "¿Podrían darme más información sobre el proceso de adopción? 🐾\n\n" +
                "ID de mascota: $mascotaId"

        val urlWhatsApp = "https://wa.me/$numeroTelefono?text=${Uri.encode(mensaje)}"

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlWhatsApp))

        // Forzar que abra con el navegador (Chrome) en lugar de WhatsApp app
        intent.setPackage("com.android.chrome")

        try {
            startActivity(intent)
        } catch (e: Exception) {
            // Si Chrome no está instalado, abrir con cualquier navegador disponible
            intent.setPackage(null)
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "No se encontró un navegador instalado", Toast.LENGTH_LONG).show()
            }
        }
    }
}