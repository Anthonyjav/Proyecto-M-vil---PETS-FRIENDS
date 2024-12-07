package com.tecsup.proyecto.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tecsup.proyecto.MapsActivity
import com.tecsup.proyecto.api.RetrofitClient
import com.tecsup.proyecto.databinding.FragmentHomeBinding
import com.tecsup.proyecto.models.Servicio
import com.tecsup.proyecto.models.Veterinario
import com.tecsup.proyecto.ui.tienda.ProductoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root



        // Recupera el nombre desde los argumentos
        val username = arguments?.getString("username") ?: "username"
        // Asigna el nombre de usuario al TextView
        binding.username.text = "Hola $username!"

        binding.btnVerMasServicios.setOnClickListener {
            val intent = Intent(requireContext(), ListaServiciosActivity::class.java)
            startActivity(intent)
        }


        // Referencia al ícono
        binding.iconoUbi.setOnClickListener {
            // Crear un Intent para navegar a la nueva actividad
            val intent = Intent(requireContext(), MapsActivity::class.java)
            startActivity(intent)
        }


        //---------------------------------------------------------------------------------------------------------------
        // Llamada a la API para obtener los servicios
        RetrofitClient.apiService.getServicios().enqueue(object : Callback<List<Servicio>> {
            override fun onResponse(call: Call<List<Servicio>>, response: Response<List<Servicio>>) {
                if (isAdded && _binding != null) {  // Verificación de seguridad
                    if (response.isSuccessful) {
                        val servicios = response.body() ?: emptyList()
                        configurarListaServicios(servicios)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error al cargar los veterinarios",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Servicio>>, t: Throwable) {
                if (isAdded && _binding != null) {  // Verificación de seguridad
                    Toast.makeText(
                        requireContext(),
                        "Error de conexión: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
        //---------------------------------------------------------------------------------------------------------------
        //---------------------------------------------------------------------------------------------------------------

        // Llamada a la API para obtener la lista de veterinarios
        RetrofitClient.apiService.getVeterinarios().enqueue(object : Callback<List<Veterinario>> {
            override fun onResponse(
                call: Call<List<Veterinario>>,
                response: Response<List<Veterinario>>
            ) {
                if (isAdded && _binding != null) {  // Verificación de seguridad
                    if (response.isSuccessful) {
                        val veterinarios = response.body()
                        if (veterinarios != null) {
                            configurarListaVeterinarios(veterinarios)
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error al cargar los veterinarios",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Veterinario>>, t: Throwable) {
                if (isAdded && _binding != null) {  // Verificación de seguridad
                    Toast.makeText(
                        requireContext(),
                        "Error de conexión: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        RetrofitClient.apiService.getProductos().enqueue(object : Callback<List<ProductoResponse>> {
            override fun onResponse(call: Call<List<ProductoResponse>>, response: Response<List<ProductoResponse>>) {
                if (isAdded && _binding != null) {  // Verificación de seguridad
                    if (response.isSuccessful) {
                        val productos = response.body()?.filter { it.estado } ?: emptyList()

                        // Filtrar y ordenar los productos, tomar los últimos 4
                        val productosFiltrados =
                            productos.sortedByDescending { it.getFechaRegistro() }
                                .take(4)

                        configurarListaProductos(productosFiltrados)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error al cargar los productos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<ProductoResponse>>, t: Throwable) {
                if (isAdded && _binding != null) {  // Verificación de seguridad
                    Toast.makeText(
                        requireContext(),
                        "Error de conexión: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
        return root
    }

    //---------------------------------------------------------------------------------------------------------------

    //---------------------------------------------------------------------------------------------------------------
    private fun configurarListaServicios(servicios: List<Servicio>) {
        _binding?.recyclerViewServicio?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        _binding?.recyclerViewServicio?.adapter = ServicioAdapter(servicios)
    }
    //---------------------------------------------------------------------------------------------------------------
    private fun configurarListaVeterinarios(veterinarios: List<Veterinario>) {
        // Asegúrate de que el binding no sea null antes de usarlo
        _binding?.recyclerViewVeterinario?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false) // Orientación horizontal
        _binding?.recyclerViewVeterinario?.adapter = VeterinarioAdapter(veterinarios)
    }
    //---------------------------------------------------------------------------------------------------------------
    private fun configurarListaProductos(productos: List<ProductoResponse>) {
        _binding?.recyclerViewProductos?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        _binding?.recyclerViewProductos?.adapter = TiendaAdapter(productos)
    }
    //---------------------------------------------------------------------------------------------------------------
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}