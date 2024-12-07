package com.tecsup.proyecto.ui.tienda

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ProductoResponse(
    val producto_id: Int,
    val nombre: String,
    val precio: Double,
    val categoria_producto_id: CategoriaProducto,
    val stock: Int,
    val marca: String,
    val descripcion: String,
    val estado: Boolean,
    val imagen: String,
    val fecha_registro: String
) {
    // Convertir la fecha de registro a LocalDateTime
    fun getFechaRegistro(): LocalDateTime {
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        return LocalDateTime.parse(fecha_registro, formatter)
    }
}

data class CategoriaProducto(
    val categoria_producto_id: Int,
    val nombre: String,
    val descripcion: String
)
