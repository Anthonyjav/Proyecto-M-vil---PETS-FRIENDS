package com.tecsup.proyecto.ui.add

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tecsup.proyecto.R
import com.tecsup.proyecto.models.Mascota
import com.tecsup.proyecto.ui.add.PerfilMascotaActivity

class MascotaAdapter(private val mascotas: List<Mascota>) : RecyclerView.Adapter<MascotaAdapter.MascotaViewHolder>() {

    class MascotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagen: ImageView = itemView.findViewById(R.id.mascotaImagen)
        val nombre: TextView = itemView.findViewById(R.id.mascotaNombre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MascotaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mascotas, parent, false)
        return MascotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MascotaViewHolder, position: Int) {
        val mascota = mascotas[position]
        holder.nombre.text = mascota.nombre
        Glide.with(holder.imagen.context).load(mascota.fotom).into(holder.imagen)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, PerfilMascotaActivity::class.java).apply {
                putExtra("MASCOTA_ID", mascota.mascota_id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = mascotas.size
}
