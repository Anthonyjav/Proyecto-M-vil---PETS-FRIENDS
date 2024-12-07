package com.tecsup.proyecto.ui.perfil

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tecsup.proyecto.R
import com.tecsup.proyecto.api.RetrofitClient
import com.tecsup.proyecto.databinding.ActivityVentasBinding
import com.tecsup.proyecto.models.MetodoPago
import com.tecsup.proyecto.models.Venta
import com.tecsup.proyecto.models.VentaDetallada
import retrofit2.Call
import retrofit2.Response

class VentasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVentasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVentasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnAtrasVentas = findViewById<ImageButton>(R.id.btnAtras_ventas)
        btnAtrasVentas.setOnClickListener {
            finish()
        }

        val sharedPreferences = getSharedPreferences("MiAppPrefs", MODE_PRIVATE)
        val usuarioId = sharedPreferences.getInt("USER_ID", -1)

        if (usuarioId != -1) {
            RetrofitClient.apiService.getVentasPorUsuario(usuarioId)
                .enqueue(object : retrofit2.Callback<List<Venta>> {
                    override fun onResponse(
                        call: retrofit2.Call<List<Venta>>,
                        response: retrofit2.Response<List<Venta>>
                    ) {
                        if (response.isSuccessful) {
                            val ventas = response.body()
                            if (ventas != null && ventas.isNotEmpty()) {
                                configurarListaVentas(ventas)
                            } else {
                                Toast.makeText(
                                    this@VentasActivity,
                                    "No se encontraron ventas.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@VentasActivity,
                                "Error al cargar ventas.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<List<Venta>>, t: Throwable) {
                        Toast.makeText(
                            this@VentasActivity,
                            "Error de conexión: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        } else {
            Toast.makeText(this, "ID de usuario no encontrado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun configurarListaVentas(ventas: List<Venta>) {
        val VentasDetalladas = mutableListOf<VentaDetallada>()
        val metodosPagoMap = mutableMapOf<Int, String>()

        RetrofitClient.apiService.getMetodosPago().enqueue(object : retrofit2.Callback<List<MetodoPago>> {
            override fun onResponse(call: retrofit2.Call<List<MetodoPago>>, response: retrofit2.Response<List<MetodoPago>>) {
                response.body()?.forEach { metodoPago ->
                    metodosPagoMap[metodoPago.metodo_id] = metodoPago.nombre
                }

                ventas.forEach { venta ->
                    val nombreMetodoPago = metodosPagoMap[venta.metodo_pago] ?: "Desconocido"
                    VentasDetalladas.add(
                        VentaDetallada(
                            venta.id,
                            nombreMetodoPago,
                            venta.correo,
                            venta.subtotal,
                            venta.total,
                            venta.fecha_venta
                        )
                    )
                }
                val recyclerView = binding.recyclerViewVentas
                recyclerView.layoutManager = LinearLayoutManager(this@VentasActivity)
                recyclerView.adapter = VentaAdapter(VentasDetalladas)
            }

            override fun onFailure(call: retrofit2.Call<List<MetodoPago>>, t: Throwable) {
                Toast.makeText(this@VentasActivity, "Error al cargar métodos de pago.", Toast.LENGTH_SHORT).show()
            }

        })

    }
}
