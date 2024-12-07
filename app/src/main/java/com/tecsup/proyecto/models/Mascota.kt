package com.tecsup.proyecto.models

data class Mascota(
    val mascota_id: Int,
    val usuario: Int,
    val nombre: String,
    val especie: String,
    val raza: String,
    val genero: String,
    val fecha_nacimiento: String,
    val peso: String,
    val altura: String,
    val edad: Int,
    val color: String,
    val fotom: String,
    val observaciones: String,
    val fecha_inscripcion: String,
    val codigo_identificacion: String
)