package com.tecsup.proyecto.ui.perfil

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tecsup.proyecto.LoginActivity
import com.tecsup.proyecto.R
import com.tecsup.proyecto.api.RetrofitClient
import com.tecsup.proyecto.databinding.FragmentPerfilBinding
import com.tecsup.proyecto.models.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("MiAppPrefs", AppCompatActivity.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("USER_ID", -1)

        // Verifica si el usuario tiene una sesión activa
        if (userId != -1) {
            obtenerDatosUsuario(userId, sharedPreferences)
        } else {
            // Si no hay sesión activa, redirige al login
            navegarALogin()
        }

        // Configura el botón para mostrar más información
        binding.infoMas.setOnClickListener {
            val intent = Intent(requireContext(), PerfilUsuarioActivity::class.java)
            startActivity(intent)
        }

        binding.btnInfoHistorial.setOnClickListener {
            val intent = Intent(requireContext(), HistorialCitasActivity::class.java)
            startActivity(intent)
        }

        binding.infoCompras.setOnClickListener {
            val intent = Intent(requireContext(), VentasActivity::class.java)
            startActivity(intent)
        }

        // Configura el botón de logout
        binding.infoLogout.setOnClickListener {
            showLogoutConfirmationDialog(sharedPreferences)
        }

        return root
    }

    private fun obtenerDatosUsuario(userId: Int, sharedPreferences: SharedPreferences) {
        val retrofit = RetrofitClient.apiService
        retrofit.getUsuario(userId).enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if (isAdded && _binding != null) {
                    if (response.isSuccessful) {
                        val usuario = response.body()
                        usuario?.let {
                            binding.textViewUsername.text = it.username
                            binding.textViewCorreo.text = it.correo
                            binding.textViewTelefono.text = it.telefono

                            // Carga de imagen
                            val imageUrl = it.foto
                            Glide.with(this@PerfilFragment)
                                .load(imageUrl)
                                .placeholder(R.drawable.no_perfil)
                                .error(R.drawable.no_perfil)
                                .into(binding.perfilUsuario)

                            // Guarda datos actualizados en SharedPreferences
                            with(sharedPreferences.edit()) {
                                putString("NOMBRES", it.nombres)
                                putString("APELLIDOS", it.apellidos)
                                putString("CORREO", it.correo)
                                putString("TELEFONO", it.telefono)
                                putString("DIRECCION", it.direccion)
                                putString("FOTO_URL", it.foto)
                                apply()
                            }
                        }
                    } else {
                        Log.e("PerfilFragment", "Error al obtener el usuario")
                    }
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                Log.e("PerfilFragment", "Error en la solicitud: ${t.message}")
            }
        })
    }

    private fun showLogoutConfirmationDialog(sharedPreferences: SharedPreferences) {
        // Verificar que el fragmento esté añadido y no esté separado
        if (!isAdded || isDetached) {
            return
        }

        // Construir el diálogo
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Cerrar Sesión")
            .setMessage("¿Seguro que quieres cerrar sesión?")
            .setPositiveButton("Sí") { _, _ ->
                cerrarSesion(sharedPreferences)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

        // Verificar que la actividad no esté destruida antes de mostrar el diálogo
        if (activity?.isFinishing == false && activity?.isDestroyed == false) {
            builder.show()
        }
    }

    private fun cerrarSesion(sharedPreferences: SharedPreferences) {
        // Limpia los datos de sesión
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }

        // Redirige al LoginActivity y elimina actividades previas
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()

        // Redirige al LoginActivity
        navegarALogin()
    }

    private fun navegarALogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
