package com.tecsup.proyecto.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tecsup.proyecto.R
import com.tecsup.proyecto.models.Veterinario

class VeterinarioAdapter(private val veterinarios: List<Veterinario>) : RecyclerView.Adapter<VeterinarioAdapter.VeterinarioViewHolder>() {

    class VeterinarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.nombreVeterinario)
        val foto: ImageView = itemView.findViewById(R.id.fotoVeterinario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VeterinarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_veterinario, parent, false)
        return VeterinarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: VeterinarioViewHolder, position: Int) {
        val veterinario = veterinarios[position]
        holder.nombre.text = "${veterinario.nombres} ${veterinario.apellidos}"
        Glide.with(holder.foto.context).load(veterinario.foto).into(holder.foto)
    }

    override fun getItemCount(): Int = veterinarios.size
}
