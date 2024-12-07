package com.tecsup.proyecto.models

data class CitaDetallada(
    val cita_id: Int,
    val nombreServicio: String,
    val nombreMascota: String,
    val horario: String,
    val fecha_cita: String,
    val estado: Boolean
)