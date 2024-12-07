package com.tecsup.proyecto.models

data class Servicio(
    val servicio_id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: String,
    val categoria: CategoriaServicio,
    val veterinario: Veterinario,
    val foto: String // Foto del servicio
)


data class CategoriaServicio(
    val categoria_servicio_id: Int,
    val nombre: String,
    val descripcion: String
)