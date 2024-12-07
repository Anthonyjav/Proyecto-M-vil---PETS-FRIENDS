package com.tecsup.proyecto.models

data class Usuario (
    val usuario: Int,
    val nombres: String,
    val apellidos: String,
    val correo: String,
    val username: String,
    val contraseña: String, // Encriptada
    val fecha_registro: String,
    val fecha_nacimiento: String,
    val foto: String,
    val telefono: String,
    val direccion: String
)