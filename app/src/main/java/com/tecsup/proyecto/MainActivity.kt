package com.tecsup.proyecto

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.tecsup.proyecto.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Recupera el ID del usuario desde SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("MiAppPrefs", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("USER_ID", -1) // El valor predeterminado es -1 si no se encuentra el ID
        val username = sharedPreferences.getString("USERNAME", "username") // Valor por defecto "Usuario" si no se encuentra el nombre

        // Verifica si el usuario no ha iniciado sesión (id es -1), y redirige al LoginActivity
        if (userId == -1) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Finaliza la MainActivity
            return
        }

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val bundle = Bundle().apply {
            putString("username", username)
            putInt("userId", userId)
        }
        navController.navigate(R.id.navigation_home, bundle) // Navega al HomeFragment con el Bundle

        val navView: BottomNavigationView = binding.navView

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_store, R.id.navigation_add, R.id.navigation_cita, R.id.navigation_perfil
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Verifica si se debe abrir fragment_cita
        if (intent.getBooleanExtra("open_fragment_cita", false)) {
            navController.navigate(R.id.navigation_cita)
        }
    }
}
