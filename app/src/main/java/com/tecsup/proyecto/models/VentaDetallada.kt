package com.tecsup.proyecto.models

data class VentaDetallada (
    val id: Int,
    val nombreMetodoPago: String,
    val correo: String,
    val subtotal: String,
    val total: String,
    val fecha_venta: String
)