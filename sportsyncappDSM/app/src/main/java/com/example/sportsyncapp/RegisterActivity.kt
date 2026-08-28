package com.example.sportsyncapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.sportsyncapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar
import java.util.Locale

class RegisterActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "RegisterActivity"

        // UUID del rol Usuario en SQL Connect
        private const val ROL_USUARIO =
            "550e8400-e29b-41d4-a716-446655440001"

        private val NOMBRES_MESES = arrayOf(
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        )
    }

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    private val usuarioRepository = UsuarioRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        configurarSpinnersFecha()
        configurarListeners()
    }

    // ---------------------------------------------------------------
    // LISTENERS
    // ---------------------------------------------------------------

    private fun configurarListeners() {

        binding.btnRegistrar.setOnClickListener {
            validarYRegistrar()
        }

        binding.btnCancelar.setOnClickListener {
            volverAlLogin()
        }
    }

    // ---------------------------------------------------------------
    // SELECTS DE FECHA (dia / mes / anio)
    // ---------------------------------------------------------------

    private fun configurarSpinnersFecha() {

        val dias = (1..31).map { it.toString() }

        val adapterDias = ArrayAdapter(
            this,
            R.layout.item_spinner_fecha,
            dias
        )

        adapterDias.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spDia.adapter = adapterDias


        val adapterMeses = ArrayAdapter(
            this,
            R.layout.item_spinner_fecha,
            NOMBRES_MESES
        )

        adapterMeses.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spMes.adapter = adapterMeses


        val anioActual =
            Calendar.getInstance().get(Calendar.YEAR)

        val anios =
            (anioActual downTo anioActual - 100)
                .map { it.toString() }

        val adapterAnios = ArrayAdapter(
            this,
            R.layout.item_spinner_fecha,
            anios
        )

        adapterAnios.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spAnio.adapter = adapterAnios

        binding.spAnio.setSelection(18)

        configurarValidacionFecha()
    }
    private fun configurarValidacionFecha() {

        binding.spMes.onItemSelectedListener =
            object : android.widget.AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {
                    actualizarDiasDisponibles()
                }

                override fun onNothingSelected(
                    parent: android.widget.AdapterView<*>?
                ) {
                }
            }

        binding.spAnio.onItemSelectedListener =
            object : android.widget.AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {
                    actualizarDiasDisponibles()
                }

                override fun onNothingSelected(
                    parent: android.widget.AdapterView<*>?
                ) {
                }
            }
    }
    private fun actualizarDiasDisponibles() {

        val mes =
            binding.spMes.selectedItemPosition + 1

        val anio =
            binding.spAnio.selectedItem.toString().toInt()

        val maxDias =
            Calendar.getInstance().apply {
                set(
                    Calendar.YEAR,
                    anio
                )
                set(
                    Calendar.MONTH,
                    mes - 1
                )
                set(
                    Calendar.DAY_OF_MONTH,
                    1
                )
            }.getActualMaximum(
                Calendar.DAY_OF_MONTH
            )

        val diaAnterior =
            if (binding.spDia.adapter != null &&
                binding.spDia.selectedItem != null
            ) {
                binding.spDia.selectedItem
                    .toString()
                    .toIntOrNull()
            } else {
                1
            }

        val dias =
            (1..maxDias).map {
                it.toString()
            }

        val adapterDias =
            ArrayAdapter(
                this,
                R.layout.item_spinner_fecha,
                dias
            )

        adapterDias.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spDia.adapter =
            adapterDias

        val posicionDeseada =
            (diaAnterior ?: 1)
                .coerceAtMost(maxDias) - 1

        binding.spDia.setSelection(
            posicionDeseada
        )
    }

    /**
     * Arma el String "dd/MM/yyyy" a partir de lo elegido en los
     * 3 selects.
     */
    private fun obtenerFechaNacimientoSeleccionada(): String {

        val dia = binding.spDia.selectedItem.toString().toInt()

        // La posicion del spinner (0-11) + 1 = mes real (1-12)
        val mes = binding.spMes.selectedItemPosition + 1

        val anio = binding.spAnio.selectedItem.toString().toInt()

        return String.format(
            Locale.getDefault(),
            "%02d/%02d/%04d",
            dia,
            mes,
            anio
        )
    }

    // ---------------------------------------------------------------
    // VALIDACIÓN
    // ---------------------------------------------------------------

    private fun validarYRegistrar() {

        val nombre =
            binding.etNombre.text.toString().trim()

        val apellido =
            binding.etApellido.text.toString().trim()

        val correo =
            binding.etCorreo.text.toString().trim()

        // NO hacemos trim a la contraseña
        val contrasena =
            binding.etContrasena.text.toString()

        val contrasenaRepeticion =
            binding.etContrasenaRepeticion.text.toString()

        val fechaNacimiento =
            obtenerFechaNacimientoSeleccionada()

        binding.tvError.text = ""

        when {

            nombre.isEmpty() -> {
                mostrarError("Ingrese su nombre")
                return
            }

            !nombre.matches(
                Regex("^[\\p{L} .'-]+$")
            ) -> {
                mostrarError(
                    "El nombre solo puede contener letras"
                )
                return
            }

            apellido.isEmpty() -> {
                mostrarError("Ingrese su apellido")
                return
            }

            !apellido.matches(
                Regex("^[\\p{L} .'-]+$")
            ) -> {
                mostrarError(
                    "El apellido solo puede contener letras"
                )
                return
            }

            correo.isEmpty() -> {
                mostrarError(
                    "Ingrese su correo electrónico"
                )
                return
            }

            !Patterns.EMAIL_ADDRESS
                .matcher(correo)
                .matches() -> {
                mostrarError(
                    "Ingrese un correo electrónico válido"
                )
                return
            }

            contrasena.isEmpty() -> {
                mostrarError(
                    "Ingrese una contraseña"
                )
                return
            }

            contrasena.length < 6 -> {
                mostrarError(
                    "La contraseña debe tener al menos 6 caracteres"
                )
                return
            }

            contrasena != contrasenaRepeticion -> {
                mostrarError(
                    "Las contraseñas no coinciden"
                )
                return
            }

        }

        registrarUsuario(
            nombre = nombre,
            apellido = apellido,
            correo = correo,
            contrasena = contrasena,
            fechaNacimiento = fechaNacimiento
        )
    }

    private fun volverAlLogin() {

        // Si existe una sesión porque el usuario comenzó el registro,
        // la cerramos antes de volver al Login.
        auth.signOut()

        val intent = Intent(
            this,
            LoginActivity::class.java
        )

        startActivity(intent)
        finish()
    }

    // ---------------------------------------------------------------
    // FIREBASE AUTHENTICATION
    // ---------------------------------------------------------------

    private fun registrarUsuario(
        nombre: String,
        apellido: String,
        correo: String,
        contrasena: String,
        fechaNacimiento: String
    ) {

        establecerCargando(true)

        auth.createUserWithEmailAndPassword(
            correo,
            contrasena
        ).addOnCompleteListener(this) { tarea ->

            if (!tarea.isSuccessful) {

                establecerCargando(false)

                Log.w(
                    TAG,
                    "Fallo el registro",
                    tarea.exception
                )

                mostrarError(
                    obtenerMensajeErrorRegistro(
                        tarea.exception
                    )
                )

                return@addOnCompleteListener
            }

            val usuarioFirebase =
                auth.currentUser

            if (usuarioFirebase == null) {

                establecerCargando(false)

                mostrarError(
                    "La cuenta fue creada, pero no se pudo obtener el usuario"
                )

                return@addOnCompleteListener
            }

            Log.i(
                TAG,
                "Usuario creado en Firebase Authentication"
            )

            enviarVerificacionCorreo(
                usuarioFirebase = usuarioFirebase
            )
        }
    }

    // ---------------------------------------------------------------
    // VERIFICACIÓN DE CORREO
    // ---------------------------------------------------------------

    private fun enviarVerificacionCorreo(
        usuarioFirebase: com.google.firebase.auth.FirebaseUser
    ) {

        usuarioFirebase
            .sendEmailVerification()
            .addOnCompleteListener { verificacion ->

                establecerCargando(false)

                if (verificacion.isSuccessful) {

                    Log.i(
                        TAG,
                        "Correo de verificación enviado"
                    )

                    Toast.makeText(
                        this,
                        "Te enviamos un correo de verificación",
                        Toast.LENGTH_LONG
                    ).show()

                    mostrarDialogoVerificacion()

                } else {

                    Log.e(
                        TAG,
                        "No se pudo enviar el correo de verificación",
                        verificacion.exception
                    )

                    mostrarError(
                        "La cuenta fue creada, pero no se pudo enviar el correo de verificación"
                    )
                }
            }
    }

    private fun mostrarDialogoVerificacion() {

        AlertDialog.Builder(this)
            .setTitle("Verifica tu correo")
            .setMessage(
                "Hemos enviado un enlace de verificación a tu correo.\n\n" +
                        "Abre el mensaje, pulsa el enlace y vuelve a la aplicación."
            )
            .setPositiveButton("Ya lo verifiqué") { _, _ ->

                comprobarVerificacion()
            }
            .setNegativeButton("Cancelar") { _, _ ->

                auth.signOut()

            }
            .setCancelable(false)
            .show()
    }

    private fun comprobarVerificacion() {

        val usuario =
            auth.currentUser

        if (usuario == null) {

            mostrarError(
                "No existe una sesión activa"
            )

            return
        }

        usuario.reload()
            .addOnCompleteListener { recarga ->

                if (!recarga.isSuccessful) {

                    Log.e(
                        TAG,
                        "No se pudo actualizar el usuario",
                        recarga.exception
                    )

                    mostrarError(
                        "No se pudo comprobar la verificación"
                    )

                    return@addOnCompleteListener
                }

                val usuarioActualizado =
                    auth.currentUser

                if (
                    usuarioActualizado?.isEmailVerified == true
                ) {

                    Log.i(
                        TAG,
                        "Correo verificado correctamente"
                    )

                    guardarDatosExtra()

                } else {

                    mostrarError(
                        "Tu correo todavía no está verificado"
                    )

                    mostrarDialogoVerificacion()
                }
            }
    }

    // ---------------------------------------------------------------
    // SQL CONNECT
    // ---------------------------------------------------------------

    private fun guardarDatosExtra() {

        val nombre =
            binding.etNombre.text.toString().trim()

        val apellido =
            binding.etApellido.text.toString().trim()

        val correo =
            binding.etCorreo.text.toString().trim()

        val fechaNacimiento =
            obtenerFechaNacimientoSeleccionada()

        val usuario =
            Usuario(
                nombre = nombre,
                apellido = apellido,
                correo = correo,
                fechaNacimiento = fechaNacimiento,
                rolId = ROL_USUARIO
            )

        usuarioRepository.guardarUsuarioActual(
            usuario
        ) { exito ->

            establecerCargando(false)

            if (exito) {

                Log.i(
                    TAG,
                    "Usuario guardado correctamente en SQL Connect"
                )

                Toast.makeText(
                    this,
                    "Cuenta creada correctamente",
                    Toast.LENGTH_SHORT
                ).show()

                irAMain()

            } else {

                Log.e(
                    TAG,
                    "Firebase Auth funcionó, pero SQL Connect falló"
                )

                mostrarError(
                    "La cuenta fue verificada, pero no se pudieron guardar tus datos"
                )
            }
        }
    }

    // ---------------------------------------------------------------
    // MENSAJES DE ERROR DE FIREBASE
    // ---------------------------------------------------------------

    private fun obtenerMensajeErrorRegistro(
        error: Exception?
    ): String {

        return when {
            error?.message?.contains(
                "already in use",
                ignoreCase = true
            ) == true -> {
                "Ese correo ya está registrado"
            }

            error?.message?.contains(
                "badly formatted",
                ignoreCase = true
            ) == true -> {
                "El correo electrónico no es válido"
            }

            error?.message?.contains(
                "weak-password",
                ignoreCase = true
            ) == true -> {
                "La contraseña es demasiado débil"
            }

            else -> {
                error?.message
                    ?: "No se pudo crear la cuenta"
            }
        }
    }

    // ---------------------------------------------------------------
    // NAVEGACIÓN
    // ---------------------------------------------------------------

    private fun irAMain() {

        val intent =
            Intent(
                this,
                MainActivity::class.java
            )

        startActivity(intent)
        finish()
    }

    // ---------------------------------------------------------------
    // UI
    // ---------------------------------------------------------------

    private fun establecerCargando(
        cargando: Boolean
    ) {
        binding.btnRegistrar.isEnabled =
            !cargando
    }

    private fun mostrarError(
        mensaje: String
    ) {
        binding.tvError.text = mensaje
    }
}