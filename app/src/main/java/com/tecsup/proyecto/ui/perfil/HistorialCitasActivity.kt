package com.tecsup.proyecto.ui.perfil

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tecsup.proyecto.R
import com.tecsup.proyecto.api.RetrofitClient
import com.tecsup.proyecto.databinding.ActivityHistorialCitasBinding
import com.tecsup.proyecto.models.Cita
import com.tecsup.proyecto.models.CitaDetallada
import com.tecsup.proyecto.models.Horario
import com.tecsup.proyecto.models.Mascota
import com.tecsup.proyecto.models.Servicio
import com.tecsup.proyecto.ui.cita.CitaAdapter

class HistorialCitasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistorialCitasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistorialCitasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnAtrasHistorial = findViewById<ImageButton>(R.id.btnAtras_historial)
        btnAtrasHistorial.setOnClickListener {
            finish()
        }


        val sharedPreferences = getSharedPreferences("MiAppPrefs", MODE_PRIVATE)
        val usuarioId = sharedPreferences.getInt("USER_ID", -1) // ID del usuario logueado

        if (usuarioId != -1) {
            RetrofitClient.apiService.getCitasPorUsuario(usuarioId)
                .enqueue(object : retrofit2.Callback<List<Cita>> {
                    override fun onResponse(
                        call: retrofit2.Call<List<Cita>>,
                        response: retrofit2.Response<List<Cita>>
                    ) {
                        if (response.isSuccessful) {
                            val citas = response.body()
                            if (citas != null) {
                                val citasHechas = citas.filter { it.estado } // Filtrar las citas hechas
                                configurarListaCitas(citasHechas, usuarioId)
                            } else {
                                Toast.makeText(
                                    this@HistorialCitasActivity,
                                    "No hay citas completadas.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@HistorialCitasActivity,
                                "Error al cargar el historial de citas.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<List<Cita>>, t: Throwable) {
                        Toast.makeText(
                            this@HistorialCitasActivity,
                            "Error de conexión: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        } else {
            Toast.makeText(this, "ID de usuario no encontrado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun configurarListaCitas(citas: List<Cita>, usuarioId: Int) {
        val citasDetalladas = mutableListOf<CitaDetallada>()
        val serviciosMap = mutableMapOf<Int, String>()
        val mascotasMap = mutableMapOf<Int, String>()
        val horariosMap = mutableMapOf<Int, String>()

        val retrofit = RetrofitClient.apiService

        retrofit.getServicios().enqueue(object : retrofit2.Callback<List<Servicio>> {
            override fun onResponse(call: retrofit2.Call<List<Servicio>>, response: retrofit2.Response<List<Servicio>>) {
                response.body()?.forEach { servicio ->
                    serviciosMap[servicio.servicio_id] = servicio.nombre
                }

                retrofit.getMascotasPorUsuario(usuarioId).enqueue(object : retrofit2.Callback<List<Mascota>> {
                    override fun onResponse(call: retrofit2.Call<List<Mascota>>, response: retrofit2.Response<List<Mascota>>) {
                        response.body()?.forEach { mascota ->
                            mascotasMap[mascota.mascota_id] = mascota.nombre
                        }

                        retrofit.getHorarios().enqueue(object : retrofit2.Callback<List<Horario>> {
                            override fun onResponse(call: retrofit2.Call<List<Horario>>, response: retrofit2.Response<List<Horario>>) {
                                response.body()?.forEach { horario ->
                                    horariosMap[horario.horario_id] = horario.hora
                                }

                                citas.forEach { cita ->
                                    val nombreServicio = serviciosMap[cita.servicio_id] ?: "Desconocido"
                                    val nombreMascota = mascotasMap[cita.mascota_id] ?: "Desconocido"
                                    val horario = horariosMap[cita.horario_id] ?: "Desconocido"

                                    citasDetalladas.add(
                                        CitaDetallada(
                                            cita.cita_id,
                                            nombreServicio,
                                            nombreMascota,
                                            horario,
                                            cita.fecha_cita,
                                            cita.estado
                                        )
                                    )
                                }

                                val recyclerView = binding.recyclerViewHistorialCitas
                                recyclerView.layoutManager = LinearLayoutManager(this@HistorialCitasActivity)
                                recyclerView.adapter = HistorialCitaAdapter(citasDetalladas)
                            }

                            override fun onFailure(call: retrofit2.Call<List<Horario>>, t: Throwable) {
                                Toast.makeText(this@HistorialCitasActivity, "Error al cargar horarios.", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }

                    override fun onFailure(call: retrofit2.Call<List<Mascota>>, t: Throwable) {
                        Toast.makeText(this@HistorialCitasActivity, "Error al cargar mascotas.", Toast.LENGTH_SHORT).show()
                    }
                })
            }

            override fun onFailure(call: retrofit2.Call<List<Servicio>>, t: Throwable) {
                Toast.makeText(this@HistorialCitasActivity, "Error al cargar servicios.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}