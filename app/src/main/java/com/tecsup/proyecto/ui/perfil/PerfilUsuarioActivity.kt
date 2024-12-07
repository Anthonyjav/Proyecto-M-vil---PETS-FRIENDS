package com.tecsup.proyecto.ui.perfil

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tecsup.proyecto.R

class PerfilUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_usuario)

        val btnAtrasUser = findViewById<ImageButton>(R.id.btnAtras_user)
        btnAtrasUser.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        cargarDatosUsuario()
    }

    private fun cargarDatosUsuario() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MiAppPrefs", MODE_PRIVATE)

        val user = sharedPreferences.getString("USERNAME", "N/A")
        val nombres = sharedPreferences.getString("NOMBRES", "N/A")
        val apellidos = sharedPreferences.getString("APELLIDOS", "N/A")
        val correo = sharedPreferences.getString("CORREO", "N/A")
        val telefono = sharedPreferences.getString("TELEFONO", "N/A")
        val direccion = sharedPreferences.getString("DIRECCION", "N/A")
        val fotoUrl = sharedPreferences.getString("FOTO_URL", null) // Obtener la URL de la imagen
        Log.d("PerfilUsuarioActivity", "URL de imagen cargada: $fotoUrl")

        findViewById<TextView>(R.id.textUser).text = user
        findViewById<TextView>(R.id.textNombres).text = nombres
        findViewById<TextView>(R.id.textApellidos).text = apellidos
        findViewById<TextView>(R.id.textCorreo).text = correo
        findViewById<TextView>(R.id.textTelefono).text = telefono
        findViewById<TextView>(R.id.textDireccion).text = direccion

        val imagenUsuario = findViewById<ImageView>(R.id.imagenUsuario)
        if (!fotoUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(fotoUrl)
                .error(R.drawable.no_perfil)      // Imagen en caso de error
                .into(imagenUsuario)              // ImageView
        } else {
            imagenUsuario.setImageResource(R.drawable.no_perfil) // Imagen por defecto
            Log.e("PerfilUsuarioActivity", "La URL de la imagen es nula")
        }
    }
}
