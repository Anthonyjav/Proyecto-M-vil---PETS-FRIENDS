package com.tecsup.proyecto.ui.home

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tecsup.proyecto.R
import com.tecsup.proyecto.models.Servicio
import com.tecsup.proyecto.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaServiciosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_servicios)

        // Botón para regresar
        val btnAtrasDetService = findViewById<ImageButton>(R.id.btnAtras_detservicio)
        btnAtrasDetService.setOnClickListener {
            finish()
        }

        // Llamar a la API para obtener los servicios
        obtenerServicios()
    }

    private fun obtenerServicios() {
        RetrofitClient.apiService.getServicios().enqueue(object : Callback<List<Servicio>> {
            override fun onResponse(call: Call<List<Servicio>>, response: Response<List<Servicio>>) {
                if (response.isSuccessful) {
                    val servicios = response.body() ?: emptyList()
                    configurarRecyclerView(servicios)
                } else {
                    Toast.makeText(
                        this@ListaServiciosActivity,
                        "Error al cargar los servicios. Código: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Servicio>>, t: Throwable) {
                Toast.makeText(
                    this@ListaServiciosActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun configurarRecyclerView(servicios: List<Servicio>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewServicios)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ServicioDetalleAdapter(servicios)
    }
}

