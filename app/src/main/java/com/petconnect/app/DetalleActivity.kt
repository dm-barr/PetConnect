package com.petconnect.app

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.petconnect.app.model.Mascota

class DetalleActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var mascotaId: String = ""
    private var esFavorito = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        // Recibir los datos de la mascota desde el Intent
        val nombre = intent.getStringExtra("nombre") ?: ""
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

        // Cargar los datos en las vistas
        tvNombre.text = nombre
        tvEspecie.text = especie
        tvRaza.text = raza
        tvEdad.text = edad
        tvDescripcion.text = descripcion

        // Cargar la imagen usando Coil
        if (imageUrl.isNotEmpty()) {
            ivMascota.load(imageUrl) {
                crossfade(true)
                placeholder(android.R.drawable.ic_menu_gallery)
            }
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
        val userId = auth.currentUser?.uid ?: return

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
}