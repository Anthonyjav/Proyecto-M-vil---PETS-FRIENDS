package com.tecsup.proyecto.models

import com.tecsup.proyecto.ui.tienda.ProductoResponse

data class ProductoCarrito(
    val id: Int,
    val carrito: Carrito,
    val producto: ProductoResponse,
    val cantidad: Int,
    val precio: Double
)