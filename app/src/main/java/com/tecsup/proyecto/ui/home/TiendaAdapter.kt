package com.tecsup.proyecto.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tecsup.proyecto.R
import com.tecsup.proyecto.ui.tienda.ProductoResponse

class TiendaAdapter(private val listaTienda: List<ProductoResponse>) :
    RecyclerView.Adapter<TiendaAdapter.TiendaViewHolder>() {

    // ViewHolder para cada ítem
    class TiendaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreTextView: TextView = view.findViewById(R.id.nombreproTienda)
        val imagenImageView: ImageView = view.findViewById(R.id.imagenproTienda) // La imagen de producto
    }

    // Inflar el layout de cada ítem
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TiendaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tienda, parent, false)
        return TiendaViewHolder(view)
    }

    // Enlazar datos con las vistas
    override fun onBindViewHolder(holder: TiendaViewHolder, position: Int) {
        val producto = listaTienda[position]

        // Mostrar el nombre del producto
        holder.nombreTextView.text = producto.nombre

        // Cargar la imagen usando Glide (asegurate de agregar Glide a las dependencias)
        Glide.with(holder.itemView.context)
            .load(producto.imagen)
            .into(holder.imagenImageView) // Aquí estamos cargando la imagen desde la URL
    }

    // Retorna el tamaño de la lista
    override fun getItemCount() = listaTienda.size
}
