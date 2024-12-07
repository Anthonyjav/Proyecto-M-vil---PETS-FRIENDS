package com.tecsup.proyecto.ui.add

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
/*import com.cloudinary.android.MediaManager*/
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.tecsup.proyecto.MainActivity
import com.tecsup.proyecto.R
import com.tecsup.proyecto.api.RetrofitClient
import com.tecsup.proyecto.models.Mascota
import com.tecsup.proyecto.utils.CloudinaryConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AgregarMascotaActivity : AppCompatActivity() {

    private lateinit var fotoUri: Uri

    private lateinit var fotomUrl: String  // Variable para almacenar la URL de Cloudinary

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_mascota)

        CloudinaryConfig.initCloudinary()


        val nombreEditText: EditText = findViewById(R.id.etNombre)
        val especieEditText: EditText = findViewById(R.id.etEspecie)
        val razaEditText: EditText = findViewById(R.id.etRaza)
        val generoEditText: EditText = findViewById(R.id.etGenero)
        val fechaNacimientoEditText: EditText = findViewById(R.id.etFechaNacimiento)
        val pesoEditText: EditText = findViewById(R.id.etPeso)
        val alturaEditText: EditText = findViewById(R.id.etAltura)
        val edadEditText: EditText = findViewById(R.id.etEdad)
        val colorEditText: EditText = findViewById(R.id.etColor)
        val fotomEditText: EditText = findViewById(R.id.etFotom)
        val observacionesEditText: EditText = findViewById(R.id.etObservaciones)
        val btnGuardar: Button = findViewById(R.id.btnGuardar)
        val fotoImageView: ImageView = findViewById(R.id.ivFoto) // ImageView para mostrar la imagen seleccionada
        val btnSeleccionarFoto: Button = findViewById(R.id.btnSeleccionarFoto) // Botón para seleccionar foto

        val btnAtrasFormPet = findViewById<ImageButton>(R.id.btnAtrasMascotaForm)
        btnAtrasFormPet.setOnClickListener {
            finish()
        }

        // Configurar el campo de fecha con un DatePickerDialog
        fechaNacimientoEditText.setOnClickListener {
            mostrarDatePickerDialog(fechaNacimientoEditText)
        }

        // Configurar el botón para seleccionar la foto
        btnSeleccionarFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 100) // Código de solicitud para la imagen
        }

        btnGuardar.setOnClickListener {
            val sharedPreferences: SharedPreferences = getSharedPreferences("MiAppPrefs", MODE_PRIVATE)
            val usuarioId = sharedPreferences.getInt("USER_ID", -1)

            if (usuarioId == -1) {
                Toast.makeText(this, "Debe iniciar sesión para agregar una mascota.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Obtener valores de los campos
            val nombre = nombreEditText.text.toString().trim()
            val especie = especieEditText.text.toString().trim()
            val raza = razaEditText.text.toString().trim()
            val genero = generoEditText.text.toString().trim()
            val fechaNacimiento = fechaNacimientoEditText.text.toString().trim()
            val peso = pesoEditText.text.toString().trim()
            val altura = alturaEditText.text.toString().trim()
            val edad = edadEditText.text.toString().trim()
            val color = colorEditText.text.toString().trim()
            val observaciones = observacionesEditText.text.toString().trim()

            // Validaciones
            if (nombre.isEmpty() || especie.isEmpty() || raza.isEmpty() || genero.isEmpty() ||
                fechaNacimiento.isEmpty() || peso.isEmpty() || altura.isEmpty() || edad.isEmpty() ||
                color.isEmpty() || fotomUrl.isEmpty()
            ) {
                Toast.makeText(this, "Todos los campos son obligatorios.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (especie !in listOf("Perro", "Gato", "Hamster")) {
                Toast.makeText(this, "La especie debe ser 'Perro', 'Gato' o 'Hamster'.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (genero !in listOf("Macho", "Hembra")) {
                Toast.makeText(this, "El género debe ser 'Macho' o 'Hembra'.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val fechaActual = Calendar.getInstance().time
            val fechaNacimientoDate = try {
                sdf.parse(fechaNacimiento)
            } catch (e: Exception) {
                null
            }

            if (fechaNacimientoDate == null || fechaNacimientoDate.after(fechaActual)) {
                Toast.makeText(this, "La fecha de nacimiento no puede ser en el futuro.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nuevaMascota = Mascota(
                mascota_id = 0, // Generado automáticamente en el backend
                usuario = usuarioId,
                nombre = nombreEditText.text.toString(),
                especie = especieEditText.text.toString(),
                raza = razaEditText.text.toString(),
                genero = generoEditText.text.toString(),
                fecha_nacimiento = fechaNacimientoEditText.text.toString(),
                peso = pesoEditText.text.toString(),
                altura = alturaEditText.text.toString(),
                edad = edadEditText.text.toString().toIntOrNull() ?: 0,
                color = colorEditText.text.toString(),
                fotom = fotomUrl, // Usamos la URI de la imagen seleccionada
                observaciones = observacionesEditText.text.toString(),
                fecha_inscripcion = "", // Automáticamente generado
                codigo_identificacion = "" // Automáticamente generado
            )

            agregarMascota(nuevaMascota)
        }
    }

    private fun mostrarDatePickerDialog(fechaEditText: EditText) {
        val calendario = Calendar.getInstance()
        val year = calendario.get(Calendar.YEAR)
        val month = calendario.get(Calendar.MONTH)
        val day = calendario.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, anio, mes, dia ->
                // Formatear la fecha en "YYYY-MM-DD"
                val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                calendario.set(anio, mes, dia)
                val fechaFormateada = formatoFecha.format(calendario.time)
                fechaEditText.setText(fechaFormateada)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            data?.data?.let {
                fotoUri = it
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fotoUri)
                val fotoImageView: ImageView = findViewById(R.id.ivFoto)
                fotoImageView.setImageBitmap(bitmap)
                Log.d("AgregarMascota", "URI de la foto: ${fotoUri}")

                // Sube la imagen a Cloudinary
                subirImagenACloudinary(fotoUri)
            }
        }
    }
    // Se agrego dependencia de Media Manager por incompatibilidad
    private fun subirImagenACloudinary(fotoUri: Uri) {
        com.cloudinary.android.MediaManager.get().upload(fotoUri)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {
                    Toast.makeText(this@AgregarMascotaActivity, "Subiendo imagen...", Toast.LENGTH_SHORT).show()
                }

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    // Mostrar progreso si es necesario
                }

                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val url = resultData["secure_url"] as String
                    Log.d("Cloudinary", "URL de la imagen: $url")

                    // Actualizar la variable fotomUrl con la URL obtenida
                    fotomUrl = url

                    // Asigna la URL al campo fotom
                    val fotomEditText: EditText = findViewById(R.id.etFotom)
                    fotomEditText.setText(url)

                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    Log.e("Cloudinary", "Error al subir imagen: ${error.description}")
                    Toast.makeText(this@AgregarMascotaActivity, "Error al subir imagen", Toast.LENGTH_SHORT).show()
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    // Si se reprograma la subida
                }
            }).dispatch()
    }

    private fun agregarMascota(mascota: Mascota) {
        Log.d("AgregarMascota", "Datos enviados: $mascota")
        val call: Call<Void> = RetrofitClient.apiService.agregarMascota(
            usuario = mascota.usuario,
            nombre = mascota.nombre,
            especie = mascota.especie,
            raza = mascota.raza,
            genero = mascota.genero,
            fechaNacimiento = mascota.fecha_nacimiento,
            peso = mascota.peso,
            altura = mascota.altura,
            edad = mascota.edad,
            color = mascota.color,
            fotom = mascota.fotom,
            observaciones = mascota.observaciones
        )

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@AgregarMascotaActivity,
                        "Mascota agregada exitosamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@AgregarMascotaActivity, MainActivity::class.java))
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    Log.e("AgregarMascota", "Error: ${response.code()} - $errorBody")
                    Toast.makeText(
                        this@AgregarMascotaActivity,
                        "Error al agregar mascota: ${response.code()} - $errorBody",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("AgregarMascota", "Fallo de conexión: ${t.message}")
                Toast.makeText(
                    this@AgregarMascotaActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
