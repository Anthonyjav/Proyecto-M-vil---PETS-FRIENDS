package com.tecsup.proyecto.ui.tienda


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tecsup.proyecto.R
import com.tecsup.proyecto.databinding.ItemCategoriaBinding

class CategoriaAdapter(private val categorias: List<CategoriaProducto>,
                       private val onCategoriaClick: (CategoriaProducto) -> Unit
) : RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {


    private val iconosPorCategoria = mapOf(
        "Medicina" to R.drawable.medicina1,
        "Comida" to R.drawable.comidas1,
        "Accesorios" to R.drawable.accesorio,
    )

    private var categoriaSeleccionada: CategoriaProducto? = null

    class CategoriaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagenCategoria: ImageView = view.findViewById(R.id.imagencategoria)
        val textCategoria: TextView = view.findViewById(R.id.textViewCategoria)
        val layoutCategoria: LinearLayout = view.findViewById(R.id.layoutCategoria) // Agregar un contenedor para cambiar el fondo

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categoria, parent, false)
        return CategoriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val categoria = categorias[position]
        holder.textCategoria.text = categoria.nombre

        // Asignar el icono correspondiente
        val iconoCategoria = iconosPorCategoria[categoria.nombre] ?: R.drawable.ic_default_servicio
        holder.imagenCategoria.setImageResource(iconoCategoria)

        // Resaltar la categoría seleccionada cambiando el backgroundTint, no el background completo
        val contexto = holder.itemView.context
        val colorRes = if (categoria == categoriaSeleccionada) R.color.categoria_seleccionada else R.color.fondo_categoria
        holder.layoutCategoria.backgroundTintList = contexto.getColorStateList(colorRes)

        // Manejar clics en la categoría
        holder.itemView.setOnClickListener {
            categoriaSeleccionada = if (categoriaSeleccionada == categoria) null else categoria
            onCategoriaClick(categoria)

        }
    }

    override fun getItemCount(): Int = categorias.size

}
