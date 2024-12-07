package com.tecsup.proyecto

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private val DURACION_CARGA: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carga)

        Handler(Looper.getMainLooper()).postDelayed({
            // Iniciar LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            finish()
        }, DURACION_CARGA)
    }
}