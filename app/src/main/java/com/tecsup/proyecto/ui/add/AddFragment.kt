package com.tecsup.proyecto.ui.add

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tecsup.proyecto.R
import com.tecsup.proyecto.api.RetrofitClient
import com.tecsup.proyecto.databinding.FragmentAddBinding
import com.tecsup.proyecto.models.Mascota

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val addViewModel =
            ViewModelProvider(this).get(AddViewModel::class.java)

        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Referencia al botón y configuración del clic
        val btnAddMascota: Button = root.findViewById(R.id.btnAddMascota)
        btnAddMascota.setOnClickListener {
            val intent = Intent(requireContext(), AgregarMascotaActivity::class.java)
            startActivity(intent)
        }

        val sharedPreferences = requireContext().getSharedPreferences("MiAppPrefs", AppCompatActivity.MODE_PRIVATE)
        val usuarioId = sharedPreferences.getInt("USER_ID", -1) // ID del usuario logueado

        if (usuarioId != -1) {
            // Llamada a Retrofit
            RetrofitClient.apiService.getMascotasPorUsuario(usuarioId)
                .enqueue(object : retrofit2.Callback<List<Mascota>> {
                    override fun onResponse(
                        call: retrofit2.Call<List<Mascota>>,
                        response: retrofit2.Response<List<Mascota>>
                    ) {
                        if (isAdded && _binding != null) {  // Verificación de seguridad
                            if (response.isSuccessful) {
                                val mascotas = response.body()
                                if (mascotas != null && mascotas.isNotEmpty()) {
                                    configurarListaMascotas(mascotas)
                                } else {
                                    binding.listMascotas.visibility = View.GONE
                                    binding.vetmascotas.visibility = View.VISIBLE
                                    /*Toast.makeText(requireContext(), "No hay mascotas registradas.", Toast.LENGTH_SHORT).show()*/
                                }
                            } else {
                                // Manejo de error de API
                                Toast.makeText(
                                    requireContext(),
                                    "Error al cargar las mascotas",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<List<Mascota>>, t: Throwable) {
                        Toast.makeText(requireContext(), "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        } else {
            Toast.makeText(requireContext(), "ID de usuario no encontrado", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    private fun configurarListaMascotas(mascotas: List<Mascota>) {
        val recyclerView = binding.recyclerViewMascotas // Asegúrate de tener un RecyclerView en tu layout
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = MascotaAdapter(mascotas)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}