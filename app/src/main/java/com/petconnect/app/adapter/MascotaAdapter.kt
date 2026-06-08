package com.petconnect.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.petconnect.app.R
import com.petconnect.app.model.Mascota

class MascotaAdapter(
    private var mascotas: List<Mascota>,
    private val onItemClick: (Mascota) -> Unit
) : RecyclerView.Adapter<MascotaAdapter.MascotaViewHolder>() {

    class MascotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivMascota: ImageView = itemView.findViewById(R.id.ivMascota)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvEspecie: TextView = itemView.findViewById(R.id.tvEspecie)
        val tvRaza: TextView = itemView.findViewById(R.id.tvRaza)
        val tvEdad: TextView = itemView.findViewById(R.id.tvEdad)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MascotaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mascota, parent, false)
        return MascotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MascotaViewHolder, position: Int) {
        val mascota = mascotas[position]

        holder.tvNombre.text = mascota.nombre
        holder.tvEspecie.text = mascota.especie
        holder.tvRaza.text = mascota.raza
        holder.tvEdad.text = mascota.edad

        // Cargar imagen usando Coil
        if (mascota.imageUrl.isNotEmpty()) {
            holder.ivMascota.load(mascota.imageUrl) {
                crossfade(true)
                placeholder(android.R.drawable.ic_menu_gallery)
            }
        }

        holder.itemView.setOnClickListener {
            onItemClick(mascota)
        }
    }

    override fun getItemCount() = mascotas.size

    // ESTE ES EL MÉTODO QUE FALTABA
    fun updateList(newList: List<Mascota>) {
        println("🔄 DEBUG Adapter: Actualizando lista. Nueva cantidad: ${newList.size}")
        mascotas = newList
        notifyDataSetChanged()
        println("✅ DEBUG Adapter: notifyDataSetChanged() llamado")
    }
}