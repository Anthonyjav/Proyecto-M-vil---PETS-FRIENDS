// ProductoAdapter.kt
package com.tecsup.proyecto.ui.tienda

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tecsup.proyecto.R
import com.tecsup.proyecto.ui.tienda.ProductoResponse

class ProductoAdapter(private val listaProductos: List<ProductoResponse>) :
    RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val precioTextView: TextView = view.findViewById(R.id.precioProducto)
        val nombreTextView: TextView = view.findViewById(R.id.nombreProducto)
        val imagenImageView: ImageView = view.findViewById(R.id.imagenProducto)
        val divProducto: LinearLayout = view.findViewById(R.id.div1)  // Contenedor de producto
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_productos, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = listaProductos[position]
        holder.precioTextView.text = "S/. ${producto.precio}"
        holder.nombreTextView.text = producto.nombre
        Glide.with(holder.itemView.context)
            .load(producto.imagen)
            .placeholder(R.drawable.placeholder_image) // Imagen mientras carga
            .error(R.drawable.error_image) // Imagen si falla
            .into(holder.imagenImageView)

        // Agregar listener al contenedor del producto
        holder.divProducto.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetalleProductoActivity::class.java).apply {
                // Crear un Bundle con los datos del producto
                val bundle = Bundle().apply {
                    putInt("producto_id", producto.producto_id)
                    putString("nombre", producto.nombre)
                    putDouble("precio", producto.precio)
                    putString("marca", producto.marca)
                    putString("descripcion", producto.descripcion)
                    putInt("stock", producto.stock)
                    putBoolean("estado", producto.estado)
                    putString("imagen", producto.imagen)
                }
                // Pasar el Bundle a la actividad de detalle
                putExtras(bundle)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = listaProductos.size
}
