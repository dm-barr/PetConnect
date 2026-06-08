package com.petconnect.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.petconnect.app.model.Mascota

class MascotaViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _mascotas = MutableLiveData<List<Mascota>>()
    val mascotas: LiveData<List<Mascota>> = _mascotas

    private var allMascotas = listOf<Mascota>()

    init {
        cargarMascotas()
    }

    private fun cargarMascotas() {
        println("🔍 DEBUG: Iniciando carga de mascotas desde Firestore...")

        db.collection("mascotas")
            .get()
            .addOnSuccessListener { result ->
                println("✅ DEBUG: Consulta exitosa. Documentos encontrados: ${result.size()}")

                val lista = result.map { document ->
                    println("📄 DEBUG: Documento ID: ${document.id}")
                    println("   - Nombre: ${document.getString("nombre")}")

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

                println("📋 DEBUG: Total de mascotas cargadas: ${lista.size}")
                allMascotas = lista
                _mascotas.value = lista
            }
            .addOnFailureListener { exception ->
                println(" DEBUG: Error al cargar mascotas: ${exception.message}")
                exception.printStackTrace()
            }
    }

    fun buscar(query: String) {
        if (query.isEmpty()) {
            _mascotas.value = allMascotas
        } else {
            val filtered = allMascotas.filter { mascota ->
                mascota.nombre.contains(query, ignoreCase = true) ||
                        mascota.raza.contains(query, ignoreCase = true) ||
                        mascota.especie.contains(query, ignoreCase = true)
            }
            _mascotas.value = filtered
        }
    }
}