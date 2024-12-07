package com.tecsup.proyecto.models

data class Venta(
    val id: Int,
    val metodo_pago: Int,
    val numero_tarjeta: String,
    val correo: String,
    val fecha_expiracion: String,
    val cvv: Int,
    val carrito: Int,
    val usuario: Int,
    val subtotal: String,
    val total: String,
    val fecha_venta: String
)