package com.tecsup.proyecto.models

data class Veterinario(
    val veterinario_id: Int,
    val nombres: String,
    val apellidos: String,
    val telefono: String,
    val correo: String,
    val fecha_nacimiento: String,
    val foto: String
)