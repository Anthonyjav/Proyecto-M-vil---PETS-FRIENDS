// TiendaFragment.kt
package com.tecsup.proyecto.ui.tienda

import android.R
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tecsup.proyecto.api.RetrofitClient
import com.tecsup.proyecto.databinding.FragmentTiendaBinding
import com.tecsup.proyecto.ui.tienda.ProductoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TiendaFragment : Fragment() {

    private var _binding: FragmentTiendaBinding? = null
    private val binding get() = _binding!!

    //MODIFICACION 21/11/2024
    private var listaProductos = listOf<ProductoResponse>()
    private var listaCategorias = listOf<CategoriaProducto>()
    private var categoriaSeleccionada: CategoriaProducto? = null // Guardar la categoría seleccionada

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTiendaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Configuración del RecyclerView
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // Configuración del RecyclerView para las categorías
        binding.recyclerViewCategorias.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Llamada a la API
        fetchProductos()
        fetchCategorias()

        // Configuración del buscador
        configurarBuscador()

        return root
    }

    private fun fetchProductos() {
        RetrofitClient.apiService.getProductos().enqueue(object : Callback<List<ProductoResponse>> {
            override fun onResponse(call: Call<List<ProductoResponse>>, response: Response<List<ProductoResponse>>) {
                if (isAdded && _binding != null) {
                    if (response.isSuccessful) {
                        // Filtra solo productos con estado true
                        listaProductos = response.body()?.filter { it.estado && it.stock > 0 } ?: emptyList()
                        // Mostrar todos los productos inicialmente
                        mostrarProductosFiltrados()
                    } else {
                        Toast.makeText(context, "Error: ${response.code()}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<List<ProductoResponse>>, t: Throwable) {
                if (isAdded && _binding != null) {  // Verificación de seguridad
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    // Obtener categorías desde la API
    private fun fetchCategorias() {
        RetrofitClient.apiService.getCategorias().enqueue(object : Callback<List<CategoriaProducto>> {
            override fun onResponse(call: Call<List<CategoriaProducto>>, response: Response<List<CategoriaProducto>>) {
                if (isAdded && _binding != null) {  // Verificación de seguridad
                    if (response.isSuccessful) {
                        // Actualizar la lista de categorías
                        listaCategorias = response.body() ?: emptyList()
                        binding.recyclerViewCategorias.adapter =
                            CategoriaAdapter(listaCategorias) { categoria ->
                                manejarSeleccionCategoria(categoria)
                            }
                    } else {
                        Toast.makeText(context, "Error: ${response.code()}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<List<CategoriaProducto>>, t: Throwable) {
                if (isAdded && _binding != null) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun configurarBuscador() {
        binding.buscadorProducto.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mostrarProductosFiltrados()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun manejarSeleccionCategoria(categoria: CategoriaProducto) {
        categoriaSeleccionada = if (categoriaSeleccionada == categoria) null else categoria
        mostrarProductosFiltrados()
        binding.recyclerViewCategorias.adapter?.notifyDataSetChanged() // Actualizar visualmente las categorías
    }

    private fun mostrarProductosFiltrados() {
        if (isAdded && _binding != null) {  // Verificación de seguridad
            val textoBusqueda = binding.buscadorProducto.text.toString().trim()
            val productosFiltrados = listaProductos.filter { producto ->
                val perteneceCategoria = categoriaSeleccionada == null ||
                        producto.categoria_producto_id.categoria_producto_id == categoriaSeleccionada?.categoria_producto_id
                val coincideBusqueda = textoBusqueda.isEmpty() ||
                        producto.nombre.contains(textoBusqueda, ignoreCase = true)
                perteneceCategoria && coincideBusqueda
            }
            binding.recyclerView.adapter = ProductoAdapter(productosFiltrados)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
