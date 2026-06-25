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
        val ivMascota: ImageView = itemView.findViewById(R.id.ivMascotaItem)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombreItem)
        val tvEspecie: TextView = itemView.findViewById(R.id.tvEspecieItem)
        val tvRazaEdad: TextView = itemView.findViewById(R.id.tvRazaEdadItem)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MascotaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mascota, parent, false)

        return MascotaViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MascotaViewHolder,
        position: Int
    ) {
        val mascota = mascotas[position]

        holder.tvNombre.text = mascota.nombre
        holder.tvEspecie.text = mascota.especie
        holder.tvRazaEdad.text = "${mascota.raza} · ${mascota.edad}"

        if (mascota.imageUrl.isNotEmpty()) {
            holder.ivMascota.load(mascota.imageUrl) {
                crossfade(true)
                placeholder(android.R.drawable.ic_menu_gallery)
                error(android.R.drawable.ic_menu_gallery)
            }
        } else {
            holder.ivMascota.setImageResource(
                android.R.drawable.ic_menu_myplaces
            )
        }

        holder.itemView.setOnClickListener {
            onItemClick(mascota)
        }
    }

    override fun getItemCount(): Int {
        return mascotas.size
    }

    fun updateList(newList: List<Mascota>) {
        mascotas = newList
        notifyDataSetChanged()
    }
}