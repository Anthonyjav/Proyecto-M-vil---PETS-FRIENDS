package com.tecsup.proyecto.ui.cita

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tecsup.proyecto.MainActivity
import com.tecsup.proyecto.R

class CitaCalendarioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_calendario)

        val btnAtrascalendar = findViewById<ImageButton>(R.id.btnAtrasFormCalendar)
        btnAtrascalendar.setOnClickListener {
            finish()
        }

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val progressTexts = findViewById<TextView>(R.id.progressTextCalendar) // TextView del porcentaje

        // Configura la animación para llenar el progreso desde 50% hasta 100%
        val animator = ValueAnimator.ofInt(50, 100) // Desde 50% hasta 100%
        animator.duration = 2000 // Duración de 2 segundos
        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            progressBar.progress = progress // Actualiza el progreso de la barra
            progressTexts.text = "$progress%" // Actualiza el texto del porcentaje
        }
        animator.start() // Inicia la animación

        val buttonConfirmar = findViewById<Button>(R.id.buttonConfirmar)
        buttonConfirmar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("open_fragment_cita", true)
            startActivity(intent)
            finish()
        }
    }
}