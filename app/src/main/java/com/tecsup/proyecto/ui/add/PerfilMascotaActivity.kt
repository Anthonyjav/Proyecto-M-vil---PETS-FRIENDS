package com.tecsup.proyecto.ui.add

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tecsup.proyecto.R
import com.tecsup.proyecto.api.RetrofitClient
import com.tecsup.proyecto.models.Mascota
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PerfilMascotaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_mascota)

        val mascotaId = intent.getIntExtra("MASCOTA_ID", -1)
        if (mascotaId != -1) {
            val sharedPreferences = getSharedPreferences("MiAppPrefs", MODE_PRIVATE)
            val usuarioId = sharedPreferences.getInt("USER_ID", -1) // Obtén el ID del usuario logueado
            if (usuarioId != -1) {
                obtenerDetallesMascota(usuarioId, mascotaId)
            } else {
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "ID de mascota no válido", Toast.LENGTH_SHORT).show()
        }

        val btnAtrasMascota = findViewById<ImageButton>(R.id.btnAtras_mascota)
        btnAtrasMascota.setOnClickListener {
            finish()
        }

        // Configurar el ImageButton con ID 'qr' para mostrar el QRDialogFragment
        val qrButton = findViewById<ImageButton>(R.id.qr)
        qrButton.setOnClickListener {
            val dialog = QRDialogFragment()
            dialog.show(supportFragmentManager, "QRDialog")
        }
    }

    private fun obtenerDetallesMascota(usuarioId: Int, mascotaId: Int) {
        RetrofitClient.apiService.getMascotasPorUsuario(usuarioId).enqueue(object : Callback<List<Mascota>> {
            override fun onResponse(call: Call<List<Mascota>>, response: Response<List<Mascota>>) {
                if (response.isSuccessful) {
                    val mascotas = response.body()
                    if (mascotas != null) {
                        val mascota = mascotas.find { it.mascota_id == mascotaId }
                        if (mascota != null) {
                            mostrarDetallesMascota(mascota)
                        } else {
                            Toast.makeText(this@PerfilMascotaActivity, "Mascota no encontrada", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@PerfilMascotaActivity, "No se encontraron mascotas", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@PerfilMascotaActivity, "Error al obtener detalles", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Mascota>>, t: Throwable) {
                Toast.makeText(this@PerfilMascotaActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun mostrarDetallesMascota(mascota: Mascota) {
        val imagenMascota = findViewById<ImageView>(R.id.imagenMascota)
        val nombreMascota = findViewById<TextView>(R.id.nombreMascota)
        val razaMascota = findViewById<TextView>(R.id.razaMascota)
        val generoMascota = findViewById<TextView>(R.id.generoMascota)
        val edadMascota = findViewById<TextView>(R.id.edadMascota)
        val alturaMascota = findViewById<TextView>(R.id.alturaMascota)
        val pesoMascota = findViewById<TextView>(R.id.pesoMascota)
        val codigoMascota = findViewById<TextView>(R.id.codigoIdentificacion)
        val fechanacimiento = findViewById<TextView>(R.id.fechanacimiento)

        Glide.with(this).load(mascota.fotom).into(imagenMascota)
        nombreMascota.text = mascota.nombre
        razaMascota.text = mascota.raza
        generoMascota.text = mascota.genero
        edadMascota.text = "${mascota.edad} meses"
        alturaMascota.text = "${mascota.altura} m"
        pesoMascota.text = "${mascota.peso} kg"
        codigoMascota.text = mascota.codigo_identificacion
        fechanacimiento.text = mascota.fecha_nacimiento
    }

}