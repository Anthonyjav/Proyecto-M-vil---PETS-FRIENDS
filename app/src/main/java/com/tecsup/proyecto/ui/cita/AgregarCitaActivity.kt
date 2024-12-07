package com.tecsup.proyecto.ui.cita

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.tecsup.proyecto.MainActivity
import com.tecsup.proyecto.R
import com.tecsup.proyecto.api.RetrofitClient
import com.tecsup.proyecto.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AgregarCitaActivity : AppCompatActivity() {

    private lateinit var spinnerMascota: Spinner
    private lateinit var spinnerServicio: Spinner
    private lateinit var spinnerHorario: Spinner
    private lateinit var etRazon: EditText
    private lateinit var etObservaciones: EditText
    private lateinit var etFechaCita: EditText
    private lateinit var btnGuardarCita: Button
    private lateinit var tvCostoCita: TextView  // Referencia al TextView del costo

    private var mascotas: List<Mascota> = emptyList()
    private var servicios: List<Servicio> = emptyList()
    private var horarios: List<Horario> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_cita)

        // Inicializar vistas
        spinnerMascota = findViewById(R.id.spinnerMascota)
        spinnerServicio = findViewById(R.id.spinnerServicio)
        spinnerHorario = findViewById(R.id.spinnerHorario)
        etRazon = findViewById(R.id.etRazon)
        etObservaciones = findViewById(R.id.etObservaciones)
        etFechaCita = findViewById(R.id.etFechaCita)
        btnGuardarCita = findViewById(R.id.btnGuardarCita)
        tvCostoCita = findViewById(R.id.tvCostoCita) // Inicializamos el TextView del costo

        // Obtener datos para los spinners
        cargarMascotas()
        cargarServicios()
        cargarHorarios()

        // Guardar cita al hacer clic
        btnGuardarCita.setOnClickListener { guardarCita() }

        // Configurar el DatePicker para el campo de fecha
        etFechaCita.setOnClickListener { mostrarDatePicker() }

        val btnAtrascalendar = findViewById<ImageButton>(R.id.btnAtrasFormCalendar)
        btnAtrascalendar.setOnClickListener {
            finish()
        }
    }

    private fun mostrarDatePicker() {
        val calendario = Calendar.getInstance()
        val year = calendario.get(Calendar.YEAR)
        val month = calendario.get(Calendar.MONTH)
        val day = calendario.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
            val fechaSeleccionada = "$year-${monthOfYear + 1}-${dayOfMonth}"
            etFechaCita.setText(fechaSeleccionada)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun cargarMascotas() {
        val sharedPreferences = getSharedPreferences("MiAppPrefs", MODE_PRIVATE)
        val usuarioId = sharedPreferences.getInt("USER_ID", -1)

        RetrofitClient.apiService.getMascotasPorUsuario(usuarioId).enqueue(object : Callback<List<Mascota>> {
            override fun onResponse(call: Call<List<Mascota>>, response: Response<List<Mascota>>) {
                if (response.isSuccessful) {
                    mascotas = response.body() ?: emptyList()
                    val nombresMascotas = mascotas.map { it.nombre }
                    spinnerMascota.adapter = ArrayAdapter(this@AgregarCitaActivity, android.R.layout.simple_spinner_item, nombresMascotas)
                } else {
                    Log.e("AgregarCitaActivity", "Error al cargar mascotas: ${response.message()}")
                    Toast.makeText(this@AgregarCitaActivity, "Error al cargar mascotas.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Mascota>>, t: Throwable) {
                Log.e("AgregarCitaActivity", "Error al cargar mascotas: ${t.message}", t)
                Toast.makeText(this@AgregarCitaActivity, "Error al cargar mascotas.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cargarServicios() {
        RetrofitClient.apiService.getServicios().enqueue(object : Callback<List<Servicio>> {
            override fun onResponse(call: Call<List<Servicio>>, response: Response<List<Servicio>>) {
                if (response.isSuccessful) {
                    servicios = response.body() ?: emptyList()
                    val nombresServicios = servicios.map { it.nombre }
                    spinnerServicio.adapter = ArrayAdapter(this@AgregarCitaActivity, android.R.layout.simple_spinner_item, nombresServicios)

                    // Añadir el listener para cuando se seleccione un servicio
                    spinnerServicio.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                            // Obtener el servicio seleccionado
                            val servicioSeleccionado = servicios.getOrNull(position)
                            servicioSeleccionado?.let {
                                // Actualizar el costo de la cita en el TextView
                                tvCostoCita.text = "Costo: $${it.precio}"
                            }
                        }

                        override fun onNothingSelected(parentView: AdapterView<*>) {
                            // No hacer nada si no se selecciona ningún servicio
                        }
                    }

                } else {
                    Log.e("AgregarCitaActivity", "Error al cargar servicios: ${response.message()}")
                    Toast.makeText(this@AgregarCitaActivity, "Error al cargar servicios.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Servicio>>, t: Throwable) {
                Log.e("AgregarCitaActivity", "Error al cargar servicios: ${t.message}", t)
                Toast.makeText(this@AgregarCitaActivity, "Error al cargar servicios.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cargarHorarios() {
        RetrofitClient.apiService.getHorarios().enqueue(object : Callback<List<Horario>> {
            override fun onResponse(call: Call<List<Horario>>, response: Response<List<Horario>>) {
                if (response.isSuccessful) {
                    horarios = response.body() ?: emptyList()
                    val horas = horarios.map { it.hora }
                    spinnerHorario.adapter = ArrayAdapter(this@AgregarCitaActivity, android.R.layout.simple_spinner_item, horas)
                } else {
                    Log.e("AgregarCitaActivity", "Error al cargar horarios: ${response.message()}")
                    Toast.makeText(this@AgregarCitaActivity, "Error al cargar horarios.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Horario>>, t: Throwable) {
                Log.e("AgregarCitaActivity", "Error al cargar horarios: ${t.message}", t)
                Toast.makeText(this@AgregarCitaActivity, "Error al cargar horarios.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun guardarCita() {
        val mascotaSeleccionada = mascotas.getOrNull(spinnerMascota.selectedItemPosition)
        val servicioSeleccionado = servicios.getOrNull(spinnerServicio.selectedItemPosition)
        val horarioSeleccionado = horarios.getOrNull(spinnerHorario.selectedItemPosition)

        // Validar campos
        if (mascotaSeleccionada == null || servicioSeleccionado == null || horarioSeleccionado == null) {
            Toast.makeText(this, "Por favor, selecciona todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        val razon = etRazon.text.toString().trim()
        val observaciones = etObservaciones.text.toString().trim()
        val fechaCita = etFechaCita.text.toString().trim()

        if (razon.isEmpty() || observaciones.isEmpty() || fechaCita.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios.", Toast.LENGTH_SHORT).show()
            return
        }

        // Validar fecha no pasada
        val fechaActual = Calendar.getInstance()
        val fechaCitaCalendar = Calendar.getInstance()
        val fechaCitaParts = fechaCita.split("-")
        fechaCitaCalendar.set(fechaCitaParts[0].toInt(), fechaCitaParts[1].toInt() - 1, fechaCitaParts[2].toInt()) // Configura la fecha de la cita

        if (fechaCitaCalendar.before(fechaActual)) {
            Toast.makeText(this, "La fecha de la cita no puede ser antes de la fecha actual.", Toast.LENGTH_SHORT).show()
            return
        }

        val costoCita = servicioSeleccionado.precio.toDoubleOrNull()
        if (costoCita == null) {
            Toast.makeText(this, "Error al calcular el costo de la cita.", Toast.LENGTH_SHORT).show()
            return
        }

        val sharedPreferences = getSharedPreferences("MiAppPrefs", MODE_PRIVATE)
        val usuarioId = sharedPreferences.getInt("USER_ID", -1)

        if (usuarioId == -1) {
            Toast.makeText(this, "Usuario no encontrado.", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener las citas del usuario y validar duplicados
        RetrofitClient.apiService.getCitasPorUsuario(usuarioId).enqueue(object : Callback<List<Cita>> {
            override fun onResponse(call: Call<List<Cita>>, response: Response<List<Cita>>) {
                if (response.isSuccessful) {
                    val citasExistentes = response.body() ?: emptyList()

                    // Verificar si ya existe una cita con la misma mascota y fecha
                    val citaDuplicada = citasExistentes.any {
                        it.mascota_id == mascotaSeleccionada.mascota_id && it.fecha_cita == fechaCita
                    }

                    if (citaDuplicada) {
                        Toast.makeText(this@AgregarCitaActivity, "Ya existe una cita para esta mascota en esta fecha.", Toast.LENGTH_SHORT).show()
                        return
                    }

                    // Si no hay cita duplicada, procedemos a crear la nueva cita
                    val nuevaCita = Cita(
                        usuario_id = usuarioId,
                        mascota_id = mascotaSeleccionada.mascota_id,
                        servicio_id = servicioSeleccionado.servicio_id,
                        razon = razon,
                        observaciones = observaciones,
                        fecha_cita = fechaCita,
                        horario_id = horarioSeleccionado.horario_id,
                        costo_cita = costoCita.toString(),
                        estado = false
                    )

                    RetrofitClient.apiService.crearCita(nuevaCita).enqueue(object : Callback<Cita> {
                        override fun onResponse(call: Call<Cita>, response: Response<Cita>) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    this@AgregarCitaActivity,
                                    "Cita registrada con éxito.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(this@AgregarCitaActivity, MainActivity::class.java))
                            } else {
                                Log.e("AgregarCitaActivity", "Error al registrar cita: ${response.message()}")
                                Toast.makeText(this@AgregarCitaActivity, "Error al registrar cita.", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Cita>, t: Throwable) {
                            Log.e("AgregarCitaActivity", "Error al registrar cita: ${t.message}", t)
                            Toast.makeText(this@AgregarCitaActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    Log.e("AgregarCitaActivity", "Error al obtener citas: ${response.message()}")
                    Toast.makeText(this@AgregarCitaActivity, "Error al obtener citas.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Cita>>, t: Throwable) {
                Log.e("AgregarCitaActivity", "Error al obtener citas: ${t.message}", t)
                Toast.makeText(this@AgregarCitaActivity, "Error al obtener citas.", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
