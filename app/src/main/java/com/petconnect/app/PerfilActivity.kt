package com.petconnect.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.petconnect.app.adapter.MascotaAdapter
import com.petconnect.app.model.Mascota

class PerfilActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var adapter: MascotaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val tvNombre = findViewById<TextView>(R.id.tvNombreUsuario)
        val tvEmail = findViewById<TextView>(R.id.tvEmailUsuario)
        val btnVolver = findViewById<ImageButton>(R.id.btnVolver)
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewFavoritos)

        // Mostrar información del usuario
        val usuario = auth.currentUser
        if (usuario != null) {
            tvEmail.text = usuario.email ?: "No disponible"
            // Intentar obtener el nombre desde Firestore
            db.collection("usuarios").document(usuario.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val nombre = document.getString("nombre") ?: "Usuario"
                        tvNombre.text = nombre
                    } else {
                        tvNombre.text = "Usuario"
                    }
                }
                .addOnFailureListener {
                    tvNombre.text = "Usuario"
                }
        } else {
            tvNombre.text = "No disponible"
            tvEmail.text = "No disponible"
        }

        // Configurar RecyclerView de favoritos
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MascotaAdapter(emptyList()) { mascota ->
            val intent = Intent(this, DetalleActivity::class.java)
            intent.putExtra("id", mascota.id)
            intent.putExtra("nombre", mascota.nombre)
            intent.putExtra("especie", mascota.especie)
            intent.putExtra("raza", mascota.raza)
            intent.putExtra("edad", mascota.edad)
            intent.putExtra("descripcion", mascota.descripcion)
            intent.putExtra("imageUrl", mascota.imageUrl)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        // Cargar favoritos
        cargarFavoritos()

        // Botón volver
        btnVolver.setOnClickListener {
            finish()
        }

        // Botón cerrar sesión
        btnCerrarSesion.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun cargarFavoritos() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("usuarios")
            .document(userId)
            .collection("favoritos")
            .get()
            .addOnSuccessListener { favoritosSnapshot ->
                if (favoritosSnapshot.isEmpty) {
                    adapter.updateList(emptyList())
                    return@addOnSuccessListener
                }

                // Obtener los IDs de las mascotas favoritas
                val favoritosIds = favoritosSnapshot.mapNotNull { doc ->
                    doc.getString("mascotaId")
                }

                if (favoritosIds.isEmpty()) {
                    adapter.updateList(emptyList())
                    return@addOnSuccessListener
                }

                // Obtener los detalles de las mascotas favoritas
                db.collection("mascotas")
                    .whereIn("id", favoritosIds)
                    .get()
                    .addOnSuccessListener { mascotasSnapshot ->
                        val mascotasFavoritas = mascotasSnapshot.map { document ->
                            Mascota(
                                id = document.id,
                                nombre = document.getString("nombre") ?: "",
                                especie = document.getString("especie") ?: "",
                                raza = document.getString("raza") ?: "",
                                edad = document.getString("edad") ?: "",
                                descripcion = document.getString("descripcion") ?: "",
                                imageUrl = document.getString("imageUrl") ?: "",
                                refugioId = document.getString("refugioId") ?: ""
                            )
                        }
                        adapter.updateList(mascotasFavoritas)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al cargar favoritos: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al obtener favoritos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}