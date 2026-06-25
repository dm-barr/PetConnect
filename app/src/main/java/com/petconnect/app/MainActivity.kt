package com.petconnect.app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.petconnect.app.adapter.MascotaAdapter
import com.petconnect.app.viewmodel.MascotaViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MascotaViewModel
    private lateinit var adapter: MascotaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MascotaViewModel::class.java]

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewMascotas)
        val etSearch = findViewById<TextInputEditText>(R.id.etSearch)
        val btnProfile = findViewById<ImageButton>(R.id.btnProfile)
        val btnIrAPublicar = findViewById<MaterialButton>(R.id.btnIrAPublicar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MascotaAdapter(emptyList()) { mascota ->
            val intent = Intent(this, DetalleActivity::class.java).apply {
                putExtra("id", mascota.id)
                putExtra("nombre", mascota.nombre)
                putExtra("especie", mascota.especie)
                putExtra("raza", mascota.raza)
                putExtra("edad", mascota.edad)
                putExtra("descripcion", mascota.descripcion)
                putExtra("imageUrl", mascota.imageUrl)
                putExtra("refugioId", mascota.refugioId)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        viewModel.mascotas.observe(this) { mascotas ->
            mascotas.forEach { mascota ->
                println("   - ${mascota.nombre} (${mascota.especie})")
            }
            adapter.updateList(mascotas)
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.buscar(s.toString())
            }
        })

        btnProfile.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        btnIrAPublicar.setOnClickListener {
            startActivity(Intent(this, PublicarActivity::class.java))
        }
    }
}