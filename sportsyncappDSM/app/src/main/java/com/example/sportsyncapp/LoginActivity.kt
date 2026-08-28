package com.example.sportsyncapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.sportsyncapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import android.util.Patterns

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LoginActivity"
        private const val PREFS_NAME = "login_prefs"
        private const val KEY_CORREO_RECORDADO = "correo_recordado"
        private const val KEY_CONTRASENA_RECORDADA = "contrasena_recordada"
    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager: CredentialManager
    private val usuarioRepository = UsuarioRepository()
    private var verificandoSesion = false

    private val prefs by lazy { getSharedPreferences(PREFS_NAME, MODE_PRIVATE) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        configurarGoogleSignIn()
        cargarCorreoRecordado()
        cargarContrasenaRecordado()
        configurarListeners()
    }
    // Este OnStart lo que va a hacer es verificar si el usuario ya esta logueado
    // y si tambien el usuario existe en la base de datos, no solo en Authentication
    override fun onStart() {
        super.onStart()

        Log.d(
            TAG,
            "onStart: LoginActivity iniciada"
        )

        val usuario = auth.currentUser

        if (usuario != null && !verificandoSesion) {

            Log.i(
                TAG,
                "Sesión activa para: ${usuario.email}"
            )

            verificarSesionActual()
        }
    }
    private fun verificarSesionActual() {

        verificandoSesion = true

        usuarioRepository.obtenerUsuarioActual { usuarioExistente ->

            verificandoSesion = false

            if (usuarioExistente != null) {

                Log.i(
                    TAG,
                    "Usuario encontrado en SQL Connect"
                )

                irAMain()

            } else {

                Log.i(
                    TAG,
                    "Existe sesión en Firebase pero no en SQL Connect"
                )
            }
        }
    }
    override fun onResume() {
        super.onResume()

        Log.d(
            TAG,
            "LoginActivity reanudada"
        )
    }
    override fun onPause() {
        super.onPause()

        Log.d(
            TAG,
            "LoginActivity pausada"
        )
    }
    override fun onStop() {
        super.onStop()

        Log.d(
            TAG,
            "LoginActivity detenida"
        )
    }
    override fun onSaveInstanceState(
        outState: Bundle
    ) {
        super.onSaveInstanceState(outState)

        outState.putString(
            "correo",
            binding.etCorreo.text.toString()
        )
    }

    // ---------------------------------------------------------------
    // Configuracion inicial
    // ---------------------------------------------------------------

    private fun configurarGoogleSignIn() {
        credentialManager = CredentialManager.create(this)
    }

    private fun cargarCorreoRecordado() {
        val correoGuardado = prefs.getString(KEY_CORREO_RECORDADO, null)
        if (correoGuardado != null) {
            binding.etCorreo.setText(correoGuardado)
            binding.cbRecordar.isChecked = true
        }
    }
    private fun cargarContrasenaRecordado() {
        val contrasenaGuardado = prefs.getString(KEY_CONTRASENA_RECORDADA, null)
        if (contrasenaGuardado != null) {
            binding.etContrasena.setText(contrasenaGuardado)
            binding.cbRecordar.isChecked = true
        }
    }

    private fun configurarListeners() {
        binding.btnIniciarSesion.setOnClickListener {
            validarYIniciarSesion()
        }

        binding.tvIrARegistro.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnOlvideContrasena.setOnClickListener {
            mostrarDialogoRecuperarContrasena()
        }

        binding.btnGoogle.setOnClickListener {
            iniciarSesionConGoogle()
        }

        binding.btnGitHub.setOnClickListener {
            iniciarSesionConProveedor("github.com")
        }
    }

    // ---------------------------------------------------------------
    // Login con correo/contrasena
    // ---------------------------------------------------------------

    private fun validarYIniciarSesion() {

        val correo =
            binding.etCorreo.text.toString().trim()

        val contrasena =
            binding.etContrasena.text.toString()

        limpiarError()

        when {
            correo.isEmpty() -> {
                mostrarError("Ingrese su correo electrónico")
                return
            }
            !Patterns.EMAIL_ADDRESS
                .matcher(correo)
                .matches() -> {

                mostrarError("Ingrese un correo electrónico válido")
                return
            }
            contrasena.isEmpty() -> {
                mostrarError("Ingrese su contraseña")
                return
            }
            contrasena.length < 6 -> {
                mostrarError("La contraseña debe tener al menos 6 caracteres")
                return
            }
        }

        iniciarSesion(
            correo,
            contrasena
        )
    }

    private fun iniciarSesion(correo: String, contrasena: String) {
        establecerCargando(true)

        auth.signInWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener(this) { tarea ->
                establecerCargando(false)

                if (tarea.isSuccessful) {
                    Log.i(TAG, "Inicio de sesion exitoso para $correo")
                    guardarOEliminarCorreoRecordado(correo)
                    guardarOEliminarContrasenaRecordada(contrasena)
                    Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                    irAMain()
                } else {
                    Log.w(TAG, "Fallo el inicio de sesion", tarea.exception)
                    mostrarError("Correo o contrasena incorrectos")
                }
            }
    }

    // AQUI SE RECUERDA EL CORREO Y LA CONTRASEÑA
    private fun guardarOEliminarCorreoRecordado(correo: String) {
        if (binding.cbRecordar.isChecked) {
            prefs.edit {
                putString(KEY_CORREO_RECORDADO, correo)
            }
        } else {
            prefs.edit {
                remove(KEY_CORREO_RECORDADO)
            }
        }
    }

    private fun guardarOEliminarContrasenaRecordada(contrasena: String) {
        if (binding.cbRecordar.isChecked) {
            prefs.edit {
                putString(KEY_CONTRASENA_RECORDADA, contrasena)
            }
        } else {
            prefs.edit {
                remove(KEY_CONTRASENA_RECORDADA)
            }
        }
    }

    // ---------------------------------------------------------------
    // Recuperar contrasena
    // ---------------------------------------------------------------

    private fun mostrarDialogoRecuperarContrasena() {
        val input = EditText(this)
        input.hint = "Correo electronico"
        input.setText(binding.etCorreo.text.toString())

        AlertDialog.Builder(this)
            .setTitle("Recuperar contrasena")
            .setMessage("Ingresa tu correo y te enviaremos un enlace para restablecer tu contrasena")
            .setView(input)
            .setPositiveButton("Enviar") { _, _ ->
                val correo = input.text.toString().trim()
                if (correo.isEmpty()) {
                    Toast.makeText(this, "Ingresa un correo valido", Toast.LENGTH_SHORT).show()
                } else {
                    enviarCorreoRecuperacion(correo)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun enviarCorreoRecuperacion(correo: String) {
        auth.sendPasswordResetEmail(correo)
            .addOnCompleteListener { tarea ->
                if (tarea.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Revisa tu correo para restablecer tu contrasena",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Log.w(TAG, "Fallo el envio de recuperacion", tarea.exception)
                    Toast.makeText(
                        this,
                        "No se pudo enviar el correo: ${tarea.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    // ---------------------------------------------------------------
    // Login con Google
    // ---------------------------------------------------------------

    private fun iniciarSesionConGoogle() {

        establecerCargando(true)

        val googleIdOption =
            GetGoogleIdOption.Builder()
                .setServerClientId(
                    getString(R.string.default_web_client_id)
                )
                .setFilterByAuthorizedAccounts(false)
                .build()

        val request =
            GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

        lifecycleScope.launch {

            try {

                val result =
                    credentialManager.getCredential(
                        context = this@LoginActivity,
                        request = request
                    )

                val credential = result.credential

                if (
                    credential is androidx.credentials.CustomCredential &&
                    credential.type ==
                    GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {

                    val googleCredential =
                        try {
                            GoogleIdTokenCredential
                                .createFrom(credential.data)

                        } catch (e: GoogleIdTokenParsingException) {

                            Log.e(
                                TAG,
                                "No se pudo interpretar la credencial de Google",
                                e
                            )

                            establecerCargando(false)

                            mostrarError(
                                "No se pudo obtener la cuenta de Google"
                            )

                            return@launch
                        }

                    val idToken =
                        googleCredential.idToken

                    autenticarFirebaseConGoogle(idToken)

                } else {

                    establecerCargando(false)

                    mostrarError(
                        "La credencial obtenida no es de Google"
                    )
                }

            } catch (e: GetCredentialException) {

                establecerCargando(false)

                Log.e(
                    TAG,
                    "Error obteniendo credencial de Google",
                    e
                )

                mostrarError(
                    "No se pudo iniciar sesión con Google"
                )
            }
        }
    }

    private fun autenticarFirebaseConGoogle(idToken: String) {

        val credencial =
            GoogleAuthProvider.getCredential(
                idToken,
                null
            )

        auth.signInWithCredential(credencial)
            .addOnCompleteListener(this) { tarea ->

                if (tarea.isSuccessful) {

                    Log.i(
                        TAG,
                        "Inicio de sesión con Google exitoso"
                    )

                    verificarUsuarioSQL()

                } else {

                    establecerCargando(false)

                    Log.e(
                        TAG,
                        "Falló autenticación de Firebase con Google",
                        tarea.exception
                    )

                    mostrarError(
                        "No se pudo iniciar sesión con Google"
                    )
                }
            }
    }

    // ---------------------------------------------------------------
    // Login con GitHub
    // ---------------------------------------------------------------

    private fun iniciarSesionConProveedor(idProveedor: String) {
        establecerCargando(true)

        val proveedor = OAuthProvider.newBuilder(idProveedor)

        auth.startActivityForSignInWithProvider(this, proveedor.build())
            .addOnSuccessListener { resultado ->
                Log.i(
                    TAG,
                    "Inicio de sesion exitoso con $idProveedor: ${resultado.user?.email}"
                )
                verificarUsuarioSQL()
            }
            .addOnFailureListener { error ->

                establecerCargando(false)

                Log.e(
                    TAG,
                    "Error de autenticación con $idProveedor",
                    error
                )

                when (error) {

                    is com.google.firebase.auth.FirebaseAuthUserCollisionException -> {

                        mostrarError(
                            "Ya existe una cuenta con ese correo. " +
                                    "Debes iniciar sesión con el método original."
                        )
                    }

                    else -> {

                        mostrarError(
                            "No se pudo iniciar sesión con GitHub"
                        )
                    }
                }
            }
    }

    private fun verificarUsuarioSQL() {

        usuarioRepository.obtenerUsuarioActual { usuarioExistente ->

            if (usuarioExistente != null) {

                Log.i(
                    TAG,
                    "El usuario ya existe en SQL Connect"
                )

                establecerCargando(false)
                irAMain()

            } else {

                Log.i(
                    TAG,
                    "El usuario existe en Auth pero no en SQL Connect"
                )

                mostrarDialogoCompletarRegistro()

            }
        }
    }

    private fun mostrarDialogoCompletarRegistro() {

        val usuarioFirebase = auth.currentUser

        if (usuarioFirebase == null) {
            establecerCargando(false)
            mostrarError("No se pudo obtener el usuario")
            return
        }

        val nombreCompleto =
            usuarioFirebase.displayName?.trim().orEmpty()

        val partesNombre =
            nombreCompleto
                .split("\\s+".toRegex())
                .filter { it.isNotBlank() }

        val nombre =
            if (partesNombre.isNotEmpty()) {
                partesNombre.first()
            } else {
                "Usuario"
            }

        val apellido =
            if (partesNombre.size > 1) {
                partesNombre.drop(1).joinToString(" ")
            } else {
                ""
            }

        val correo =
            usuarioFirebase.email

        if (correo.isNullOrEmpty()) {
            establecerCargando(false)

            mostrarError(
                "El proveedor no proporcionó un correo electrónico"
            )

            return
        }

        mostrarSelectorFecha(
            nombre = nombre,
            apellido = apellido,
            correo = correo,
            fotoPerfil = usuarioFirebase.photoUrl?.toString()
        )
    }
    private fun mostrarSelectorFecha(
        nombre: String,
        apellido: String,
        correo: String,
        fotoPerfil: String?
    ) {

        val constraints =
            CalendarConstraints.Builder()
                .setEnd(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Fecha de nacimiento")
                .setTheme(
                    com.google.android.material.R.style.ThemeOverlay_Material3_MaterialCalendar
                )
                .setCalendarConstraints(constraints)
                .build()

        datePicker.addOnPositiveButtonClickListener { fechaMillis ->

            val fechaNacimiento =
                convertirMillisAFecha(fechaMillis)

            if (!validarFechaNacimiento(fechaNacimiento)) {
                return@addOnPositiveButtonClickListener
            }

            guardarUsuarioOAuth(
                nombre = nombre,
                apellido = apellido,
                correo = correo,
                fechaNacimiento = fechaNacimiento,
                fotoPerfil = fotoPerfil
            )
        }

        datePicker.addOnCancelListener {
            establecerCargando(false)
        }

        datePicker.addOnDismissListener {
            // No hacemos nada aquí porque el resultado ya se procesa
            // en addOnPositiveButtonClickListener.
        }

        datePicker.show(
            supportFragmentManager,
            "DATE_PICKER"
        )
    }

    private fun convertirMillisAFecha(
        fechaMillis: Long
    ): String {

        val formato =
            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            )

        formato.timeZone = TimeZone.getTimeZone("UTC")

        return formato.format(Date(fechaMillis))
    }

    // Validacion de fechas para registro
    private fun validarFechaNacimiento(
        fechaNacimiento: String
    ): Boolean {

        return try {

            val formato =
                SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.getDefault()
                )

            formato.isLenient = false

            val fecha =
                formato.parse(fechaNacimiento)

            if (fecha == null) {
                mostrarError(
                    "La fecha de nacimiento no es válida"
                )
                return false
            }

            val hoy = Date()

            if (fecha.after(hoy)) {
                mostrarError(
                    "La fecha de nacimiento no puede ser futura"
                )
                return false
            }

            true

        } catch (e: Exception) {

            Log.e(
                TAG,
                "Error validando fecha de nacimiento",
                e
            )

            mostrarError(
                "La fecha de nacimiento no es válida"
            )

            false
        }
    }
    private fun guardarUsuarioOAuth(
        nombre: String,
        apellido: String,
        correo: String,
        fechaNacimiento: String,
        fotoPerfil: String?
    ) {

        val usuario = Usuario(
            nombre = nombre,
            apellido = apellido,
            correo = correo,
            fechaNacimiento = fechaNacimiento,
            rolId = UsuarioRepository.ROL_USUARIO.toString(),
            fotoPerfil = fotoPerfil
        )

        usuarioRepository.guardarUsuarioActual(usuario) { exito ->

            if (exito) {

                Log.i(
                    TAG,
                    "Usuario OAuth guardado en SQL Connect"
                )

                establecerCargando(false)

                Toast.makeText(
                    this,
                    "Cuenta creada correctamente",
                    Toast.LENGTH_SHORT
                ).show()

                irAMain()

            } else {

                establecerCargando(false)

                Log.e(
                    TAG,
                    "Firebase Auth funcionó pero SQL Connect falló"
                )

                mostrarError(
                    "La cuenta se creó, pero no se pudieron guardar tus datos"
                )
            }
        }
    }

    // ---------------------------------------------------------------
    // Navegacion
    // ---------------------------------------------------------------

    private fun irAMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    // ---------------------------------------------------------------
    // Utilidades de interfaz
    // ---------------------------------------------------------------

    private fun establecerCargando(cargando: Boolean) {
        binding.btnIniciarSesion.isEnabled = !cargando
    }

    private fun mostrarError(mensaje: String) {
        binding.tvError.text = mensaje
    }

    private fun limpiarError() {
        binding.tvError.text = ""
    }
}