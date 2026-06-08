package com.petconnect.app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        val etSearch = findViewById<EditText>(R.id.etSearch)
        val btnProfile = findViewById<ImageButton>(R.id.btnProfile)
        val btnIrAPublicar = findViewById<Button>(R.id.btnIrAPublicar)

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