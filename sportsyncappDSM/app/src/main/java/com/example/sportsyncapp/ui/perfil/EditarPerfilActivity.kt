package com.example.sportsyncapp.ui.perfil

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sportsyncapp.data.repository.UsuarioRepository
import com.example.sportsyncapp.databinding.ActivityEditarPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarPerfilBinding
    private val usuarioRepository = UsuarioRepository()

    private var fechaSeleccionada: Calendar = Calendar.getInstance()
    private var fechaInicial = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotones()
        cargarDatosPerfil()
    }


    private fun configurarBotones() {

        binding.btnCancelar.setOnClickListener {
            finish()
        }

        binding.btnSeleccionarFecha.setOnClickListener {
            mostrarSelectorFecha()
        }

        binding.etFechaNacimiento.setOnClickListener {
            mostrarSelectorFecha()
        }

        binding.btnGuardarCambios.setOnClickListener {
            guardarCambios()
        }
    }


    private fun cargarDatosPerfil() {
        val correoAutenticado =
            FirebaseAuth.getInstance().currentUser?.email.orEmpty()

        binding.etCorreo.setText(correoAutenticado)
        binding.etCorreo.isEnabled = false

        binding.btnGuardarCambios.isEnabled = false
        binding.tvMensaje.text = "Cargando información..."

        usuarioRepository.obtenerUsuarioActual { usuario ->
            if (isFinishing || isDestroyed) return@obtenerUsuarioActual

            if (usuario == null) {
                binding.tvMensaje.text =
                    "No se pudo cargar tu perfil. Intenta nuevamente."
                Toast.makeText(
                    this,
                    "No se pudieron cargar los datos del usuario",
                    Toast.LENGTH_LONG
                ).show()
                return@obtenerUsuarioActual
            }

            binding.etNombre.setText(usuario.nombre)
            binding.etApellido.setText(usuario.apellido)
            binding.etCorreo.setText(usuario.correo)

            fechaInicial = usuario.fechaNacimiento
            fechaSeleccionada = convertirAFecha(usuario.fechaNacimiento)
            binding.etFechaNacimiento.setText(
                formatearFecha(fechaSeleccionada)
            )

            binding.tvMensaje.text = ""
            binding.btnGuardarCambios.isEnabled = true
        }
    }

    private fun mostrarSelectorFecha() {
        val hoy = Calendar.getInstance()

        DatePickerDialog(
            this,
            { _, anio, mes, dia ->
                fechaSeleccionada.set(Calendar.YEAR, anio)
                fechaSeleccionada.set(Calendar.MONTH, mes)
                fechaSeleccionada.set(Calendar.DAY_OF_MONTH, dia)

                binding.etFechaNacimiento.setText(
                    formatearFecha(fechaSeleccionada)
                )
            },
            fechaSeleccionada.get(Calendar.YEAR),
            fechaSeleccionada.get(Calendar.MONTH),
            fechaSeleccionada.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.maxDate = hoy.timeInMillis
            show()
        }
    }

    private fun guardarCambios() {
        val nombre = binding.etNombre.text.toString().trim()
        val apellido = binding.etApellido.text.toString().trim()
        val correo = binding.etCorreo.text.toString().trim()
        val fecha = binding.etFechaNacimiento.text.toString().trim()

        binding.tvMensaje.text = ""

        when {
            nombre.length < 2 ||
                    !nombre.matches(Regex("^[\\p{L} .'-]+$")) -> {
                binding.tvMensaje.text = "Digite un nombre real"
                binding.etNombre.requestFocus()
                return
            }

            apellido.length < 2 ||
                    !apellido.matches(Regex("^[\\p{L} .'-]+$")) -> {
                binding.tvMensaje.text = "Digite un apellido real"
                binding.etApellido.requestFocus()
                return
            }

            correo.isBlank() -> {
                binding.tvMensaje.text = "No se encontró el correo de la cuenta"
                return
            }

            fecha.isBlank() -> {
                binding.tvMensaje.text = "Selecciona tu fecha de nacimiento"
                return
            }
        }

        // Data Connect recibe la fecha como dd/MM/yyyy
        val fechaParaGuardar = formatearFecha(fechaSeleccionada)

        binding.btnGuardarCambios.isEnabled = false
        binding.tvMensaje.text = "Guardando cambios..."

        usuarioRepository.actualizarUsuarioActual(
            nombre = nombre,
            apellido = apellido,
            fechaNacimiento = fechaParaGuardar
        ) { correcto ->
            if (isFinishing || isDestroyed) return@actualizarUsuarioActual

            binding.btnGuardarCambios.isEnabled = true

            if (correcto) {
                Toast.makeText(
                    this,
                    "Perfil actualizado correctamente",
                    Toast.LENGTH_SHORT
                ).show()

                setResult(RESULT_OK)
                finish()
            } else {
                binding.tvMensaje.text =
                    "No se pudieron guardar los cambios. Intenta nuevamente."
            }
        }
    }

    private fun convertirAFecha(fecha: String): Calendar {
        val resultado = Calendar.getInstance()

        try {
            val texto = fecha.trim()

            val formato = if (texto.matches(Regex("""\d{4}-\d{2}-\d{2}"""))) {
                SimpleDateFormat("yyyy-MM-dd", Locale.US)
            } else {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            }

            formato.isLenient = false
            val date = formato.parse(texto)

            if (date != null) {
                resultado.time = date
            }
        } catch (_: Exception) {
            resultado.time = Calendar.getInstance().time
        }

        return resultado
    }

    private fun formatearFecha(fecha: Calendar): String {
        return SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
        ).format(fecha.time)
    }
}
