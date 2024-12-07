package com.tecsup.proyecto.ui.tienda

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tecsup.proyecto.R

class DetalleProductoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_producto)

        val btnAtrasDetailProducto = findViewById<ImageButton>(R.id.btnAtrasDetalleProducto)
        btnAtrasDetailProducto.setOnClickListener {
            finish()
        }

        // Recuperar el Bundle con los datos
        val bundle = intent.extras

        // Asegúrate de que el Bundle no sea nulo y recuperar los valores
        if (bundle != null) {
            val productoId = bundle.getInt("producto_id")
            val nombre = bundle.getString("nombre")
            val precio = bundle.getDouble("precio")
            val marca = bundle.getString("marca")
            val descripcion = bundle.getString("descripcion")
            val stock = bundle.getInt("stock")
            val estado = bundle.getBoolean("estado")
            val imagen = bundle.getString("imagen")

            // Mostrar los datos en los elementos de la interfaz
            val nombreTextView: TextView = findViewById(R.id.nombreProductoDetalle)
            val descripcionTextView: TextView = findViewById(R.id.descripcionProductoDetalle)
            val marcaTextView: TextView = findViewById(R.id.marcaProductoDetalle)
            val precioTextView: TextView = findViewById(R.id.precioProductoDetalle)
            val imagenImageView: ImageView = findViewById(R.id.imagenProductoDetalle)

            nombreTextView.text = nombre
            marcaTextView.text = marca
            descripcionTextView.text = descripcion
            precioTextView.text = "S/. $precio"
            Glide.with(this)
                .load(imagen)
                .into(imagenImageView)
        }
    }
}
