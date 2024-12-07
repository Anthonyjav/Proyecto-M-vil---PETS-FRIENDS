package com.tecsup.proyecto.models

data class Cita(
    val cita_id: Int = 0,
    val usuario_id: Int,  // Cambiado de Usuario a Int
    val mascota_id: Int,  // Cambiado de Mascota a Int
    val servicio_id: Int, // Cambiado de Servicio a Int
    val razon: String,
    val observaciones: String,
    val fecha_cita: String,
    val horario_id: Int,  // Cambiado de Horario a Int
    val costo_cita: String,
    val estado: Boolean = false
)
