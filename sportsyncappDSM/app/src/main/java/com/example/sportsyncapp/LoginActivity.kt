package com.example.sportsyncapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.sportsyncapp.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import android.app.DatePickerDialog
import com.google.firebase.auth.FirebaseUser
import java.util.Calendar

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LoginActivity"
        private const val PREFS_NAME = "login_prefs"
        private const val KEY_CORREO_RECORDADO = "correo_recordado"
    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val usuarioRepository = UsuarioRepository()

    private val prefs by lazy { getSharedPreferences(PREFS_NAME, MODE_PRIVATE) }

    // Recibe el resultado de la pantalla de Google Sign-In
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { resultado ->
        if (resultado.resultCode == Activity.RESULT_OK) {
            val tarea = GoogleSignIn.getSignedInAccountFromIntent(resultado.data)
            try {
                val cuenta = tarea.getResult(ApiException::class.java)
                autenticarConGoogle(cuenta.idToken!!)
            } catch (error: ApiException) {
                Log.w(TAG, "Fallo el inicio de sesion con Google", error)
                mostrarError("No se pudo iniciar sesion con Google")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        configurarGoogleSignIn()
        cargarCorreoRecordado()
        configurarListeners()
    }

    override fun onStart() {
        super.onStart()
        // Si ya existe una sesion activa se omite el login
        auth.currentUser?.let { usuario ->
            Log.i(TAG, "Sesion ya activa para: ${usuario.email}")
            irAMain()
        }
    }

    // ---------------------------------------------------------------
    // Configuracion inicial
    // ---------------------------------------------------------------

    private fun configurarGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun cargarCorreoRecordado() {
        val correoGuardado = prefs.getString(KEY_CORREO_RECORDADO, null)
        if (correoGuardado != null) {
            binding.etCorreo.setText(correoGuardado)
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
            googleSignInLauncher.launch(googleSignInClient.signInIntent)
        }

        binding.btnGitHub.setOnClickListener {
            iniciarSesionConProveedor("github.com")
        }
    }

    // ---------------------------------------------------------------
    // Login con correo/contrasena
    // ---------------------------------------------------------------

    private fun validarYIniciarSesion() {
        val correo = binding.etCorreo.text.toString().trim()
        val contrasena = binding.etContrasena.text.toString().trim()

        limpiarError()

        when {
            correo.isEmpty() -> {
                mostrarError("Ingrese su correo electronico")
                return
            }
            contrasena.isEmpty() -> {
                mostrarError("Ingrese su contrasena")
                return
            }
        }

        iniciarSesion(correo, contrasena)
    }

    private fun iniciarSesion(correo: String, contrasena: String) {
        establecerCargando(true)

        auth.signInWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener(this) { tarea ->
                establecerCargando(false)

                if (tarea.isSuccessful) {
                    Log.i(TAG, "Inicio de sesion exitoso para $correo")
                    guardarOEliminarCorreoRecordado(correo)
                    Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                    irAMain()
                } else {
                    Log.w(TAG, "Fallo el inicio de sesion", tarea.exception)
                    mostrarError("Correo o contrasena incorrectos")
                }
            }
    }

    // AQUI SE RECUERDA EL CORREO 
    private fun guardarOEliminarCorreoRecordado(correo: String) {
        if (binding.cbRecordar.isChecked) {
            prefs.edit().putString(KEY_CORREO_RECORDADO, correo).apply()
        } else {
            prefs.edit().remove(KEY_CORREO_RECORDADO).apply()
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

    private fun autenticarConGoogle(idToken: String) {

        establecerCargando(true)

        val credencial =
            GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credencial)
            .addOnCompleteListener(this) { tarea ->

                if (tarea.isSuccessful) {

                    Log.i(
                        TAG,
                        "Inicio de sesion con Google exitoso"
                    )

                    verificarUsuarioSQL()

                } else {

                    establecerCargando(false)

                    Log.w(
                        TAG,
                        "Fallo el inicio de sesion con Google",
                        tarea.exception
                    )

                    mostrarError(
                        "No se pudo iniciar sesion con Google"
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
                Log.w(TAG, "Fallo el inicio de sesion con $idProveedor", error)
                mostrarError("No se pudo iniciar sesion con ese proveedor")
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
            nombreCompleto.split(" ")

        val nombre =
            if (partesNombre.isNotEmpty()) {
                partesNombre[0]
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

        val inputFecha = EditText(this)

        inputFecha.hint = "Fecha de nacimiento (dd/MM/yyyy)"
        inputFecha.inputType = android.text.InputType.TYPE_CLASS_DATETIME

        AlertDialog.Builder(this)
            .setTitle("Completa tu perfil")
            .setMessage(
                "Necesitamos tu fecha de nacimiento para completar tu registro."
            )
            .setView(inputFecha)
            .setPositiveButton("Continuar") { _, _ ->

                val fechaNacimiento =
                    inputFecha.text.toString().trim()

                if (fechaNacimiento.isEmpty()) {

                    establecerCargando(false)

                    mostrarError(
                        "Debes ingresar tu fecha de nacimiento"
                    )

                    return@setPositiveButton
                }

                guardarUsuarioOAuth(
                    nombre = nombre,
                    apellido = apellido,
                    correo = correo,
                    fechaNacimiento = fechaNacimiento,
                    fotoPerfil = usuarioFirebase.photoUrl?.toString()
                )
            }
            .setNegativeButton("Cancelar") { _, _ ->

                auth.signOut()
                establecerCargando(false)

            }
            .setCancelable(false)
            .show()
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