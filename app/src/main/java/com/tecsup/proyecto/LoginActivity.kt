package com.tecsup.proyecto

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.mindrot.jbcrypt.BCrypt
import android.content.SharedPreferences
import android.widget.ImageView
import com.tecsup.proyecto.api.RetrofitClient
import com.tecsup.proyecto.models.Usuario
import retrofit2.Call

import retrofit2.Response

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verificar si la sesión está activa
        val sharedPreferences = getSharedPreferences("MiAppPrefs", MODE_PRIVATE)
        val isUserLoggedIn = sharedPreferences.contains("USER_ID")

        if (isUserLoggedIn) {
            // Si hay un usuario guardado, redirigir al MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.btnIngresar)
        val eyeIcon = findViewById<ImageView>(R.id.eyeIcon)

        // Cambiar visibilidad de la contraseña
        eyeIcon.setOnClickListener {
            if (passwordEditText.inputType == android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                passwordEditText.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_NORMAL
                eyeIcon.setImageResource(R.drawable.ic_eye_on) // Cambiar a icono de ojo abierto
            } else {
                passwordEditText.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                eyeIcon.setImageResource(R.drawable.ic_eye_off) // Cambiar a icono de ojo cerrado
            }
            passwordEditText.setSelection(passwordEditText.text.length) // Mantener el cursor al final
        }

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                iniciarSesion(username, password)
            } else {
                Toast.makeText(this, "Por favor ingresa usuario y contraseña", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun iniciarSesion(username: String, password: String) {
        val call = RetrofitClient.apiService.getUsuarios()

        call.enqueue(object : retrofit2.Callback<List<Usuario>> {
            override fun onResponse(call: Call<List<Usuario>>, response: retrofit2.Response<List<Usuario>>) {
                if (response.isSuccessful) {
                    val usuarios = response.body()
                    val usuarioEncontrado = usuarios?.find { it.username == username }

                    if (usuarioEncontrado != null && BCrypt.checkpw(password, usuarioEncontrado.contraseña)) {
                        // Guardar el nombre de usuario en SharedPreferences
                        val sharedPreferences = getSharedPreferences("MiAppPrefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putInt("USER_ID", usuarioEncontrado.usuario) // Guarda el ID del usuario
                        editor.putString("NOMBRES", usuarioEncontrado.nombres)
                        editor.putString("APELLIDOS", usuarioEncontrado.apellidos)
                        editor.putString("CORREO", usuarioEncontrado.correo)
                        editor.putString("TELEFONO", usuarioEncontrado.telefono)
                        editor.putString("DIRECCION", usuarioEncontrado.direccion)
                        editor.putString("USERNAME", usuarioEncontrado.username)
                        editor.apply()

                        // Iniciar la actividad principal
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Error al obtener los usuarios", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
