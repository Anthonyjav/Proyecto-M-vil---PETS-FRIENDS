package com.tecsup.proyecto.ui.cita

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tecsup.proyecto.api.RetrofitClient
import com.tecsup.proyecto.databinding.FragmentCitaBinding
import com.tecsup.proyecto.models.Cita
import com.tecsup.proyecto.models.CitaDetallada
import com.tecsup.proyecto.models.Horario
import com.tecsup.proyecto.models.Mascota
import com.tecsup.proyecto.models.Servicio

class CitaFragment : Fragment() {

    private var _binding: FragmentCitaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCitaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val buttonAgregarCita = binding.btnAgregarCita
        buttonAgregarCita.setOnClickListener {
            val intent = Intent(requireContext(), AgregarCitaActivity::class.java)
            startActivity(intent)
        }

        val sharedPreferences = requireContext().getSharedPreferences("MiAppPrefs", AppCompatActivity.MODE_PRIVATE)
        val usuarioId = sharedPreferences.getInt("USER_ID", -1) // ID del usuario logueado

        if (usuarioId != -1) {
            // Llamada a Retrofit para obtener las citas
            RetrofitClient.apiService.getCitasPorUsuario(usuarioId)
                .enqueue(object : retrofit2.Callback<List<Cita>> {
                    override fun onResponse(
                        call: retrofit2.Call<List<Cita>>,
                        response: retrofit2.Response<List<Cita>>
                    ) {
                        if (response.isSuccessful) {
                            val citas = response.body()
                            if (citas != null && citas.isNotEmpty()) {
                                configurarListaCitas(citas, usuarioId)
                            } else {
                                binding.listCitas.visibility = View.GONE
                                binding.emptyView.visibility = View.VISIBLE
                                Toast.makeText(requireContext(), "No hay citas registradas.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(requireContext(), "Error al cargar las citas", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<List<Cita>>, t: Throwable) {
                        Toast.makeText(requireContext(), "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        } else {
            Toast.makeText(requireContext(), "ID de usuario no encontrado", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    private fun configurarListaCitas(citas: List<Cita>, usuarioId: Int) {
        val citasDetalladas = mutableListOf<CitaDetallada>()
        val serviciosMap = mutableMapOf<Int, String>()
        val mascotasMap = mutableMapOf<Int, String>()
        val horariosMap = mutableMapOf<Int, String>()

        val retrofit = RetrofitClient.apiService

        // Llamadas simultáneas a la API para cargar nombres de servicios, mascotas y horarios
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

                                // Verificar si el fragmento aún está agregado y si el binding no es null
                                if (isAdded && _binding != null) {

                                    // Construir la lista detallada
                                    citas.filter { !it.estado }.sortedBy { it.fecha_cita }. forEach { cita ->
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

                                    // Configurar RecyclerView
                                    val recyclerView = binding.recyclerViewCitas
                                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                                    recyclerView.adapter = CitaAdapter(citasDetalladas)
                                }
                            }

                            override fun onFailure(call: retrofit2.Call<List<Horario>>, t: Throwable) {
                                Toast.makeText(requireContext(), "Error al cargar horarios", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }

                    override fun onFailure(call: retrofit2.Call<List<Mascota>>, t: Throwable) {
                        Toast.makeText(requireContext(), "Error al cargar mascotas", Toast.LENGTH_SHORT).show()
                    }
                })
            }

            override fun onFailure(call: retrofit2.Call<List<Servicio>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error al cargar servicios", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
