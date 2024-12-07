package com.tecsup.proyecto.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tecsup.proyecto.R
import com.tecsup.proyecto.models.Servicio

class ServicioDetalleAdapter(private val listaServicios: List<Servicio>) :
    RecyclerView.Adapter<ServicioDetalleAdapter.ServicioViewHolder>() {

    class ServicioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreTextView: TextView = view.findViewById(R.id.nombreServicio)
        val descripcionTextView: TextView = view.findViewById(R.id.descripcionServicio)
        val imagenImageView: ImageView = view.findViewById(R.id.imagenServicio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_servicio_detalle, parent, false)
        return ServicioViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServicioViewHolder, position: Int) {
        val servicio = listaServicios[position]
        holder.nombreTextView.text = servicio.nombre
        holder.descripcionTextView.text = servicio.descripcion

        // Cargar el ícono basado en el nombre del servicio
        val iconosPorServicio = mapOf(
            "Dermatologia" to R.drawable.dematologia,
            "Endocrinología" to R.drawable.endocrino,
            "Bañado y limpieza" to R.drawable.limpieza,
            "Corte de pelo" to R.drawable.corte1,
            "Vacunas" to R.drawable.ic_vacunas,
            "Atencion General" to R.drawable.ic_pet,
        )
        val iconoResId = iconosPorServicio[servicio.nombre] ?: R.drawable.ic_default_servicio
        holder.imagenImageView.setImageResource(iconoResId)
    }

    override fun getItemCount() = listaServicios.size
}

