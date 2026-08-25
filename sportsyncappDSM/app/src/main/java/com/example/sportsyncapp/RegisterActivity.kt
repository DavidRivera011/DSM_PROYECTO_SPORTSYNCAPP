package com.example.sportsyncapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sportsyncapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

/**
 * RegisterActivity
 *
 * Flujo de registro:
 *
 * 1. Valida los datos del formulario.
 * 2. Crea la cuenta en Firebase Authentication usando correo y contraseña.
 * 3. Firebase genera el UID del usuario.
 * 4. Guarda los datos adicionales del usuario en SQL Connect.
 *
 * La contraseña NO se guarda en SQL Connect.
 */
class RegisterActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "RegisterActivity"

        /**
         * UUID del rol Usuario en SQL Connect.
         *
         * Este debe coincidir con el registro:
         * 550e8400-e29b-41d4-a716-446655440001 -> Usuario
         */
        private const val ROL_USUARIO =
            "550e8400-e29b-41d4-a716-446655440001"
    }

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    /**
     * Repository que posteriormente utilizará SQL Connect.
     */
    private val usuarioRepository = UsuarioRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        configurarListeners()
    }

    // ---------------------------------------------------------------
    // LISTENERS
    // ---------------------------------------------------------------

    private fun configurarListeners() {
        binding.btnRegistrar.setOnClickListener {
            validarYRegistrar()
        }
    }

    // ---------------------------------------------------------------
    // VALIDACIÓN
    // ---------------------------------------------------------------

    private fun validarYRegistrar() {

        val nombre = binding.etNombre.text.toString().trim()
        val apellido = binding.etApellido.text.toString().trim()
        val correo = binding.etCorreo.text.toString().trim()
        val contrasena = binding.etContrasena.text.toString().trim()
        val fechaNacimiento = binding.etFechaNacimiento.text.toString().trim()

        binding.tvError.text = ""

        when {

            nombre.isEmpty() -> {
                mostrarError("Ingrese su nombre")
                return
            }

            apellido.isEmpty() -> {
                mostrarError("Ingrese su apellido")
                return
            }

            correo.isEmpty() -> {
                mostrarError("Ingrese su correo electronico")
                return
            }

            contrasena.length < 6 -> {
                mostrarError(
                    "La contrasena debe tener al menos 6 caracteres"
                )
                return
            }

            fechaNacimiento.isEmpty() -> {
                mostrarError("Ingrese su fecha de nacimiento")
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

            if (tarea.isSuccessful) {

                val usuarioFirebase = auth.currentUser

                if (usuarioFirebase == null) {

                    establecerCargando(false)

                    Log.e(
                        TAG,
                        "Firebase creó la cuenta pero no se obtuvo currentUser"
                    )

                    mostrarError(
                        "La cuenta fue creada, pero ocurrió un error al obtener el usuario"
                    )

                    return@addOnCompleteListener
                }

                Log.i(
                    TAG,
                    "Usuario creado en Firebase Authentication"
                )

                Log.i(
                    TAG,
                    "UID: ${usuarioFirebase.uid}"
                )

                /*
                 * Authentication ya creó:
                 *
                 * correo
                 * contraseña
                 * UID
                 *
                 * Ahora guardamos los datos adicionales
                 * en SQL Connect.
                 */
                guardarDatosExtra(
                    nombre = nombre,
                    apellido = apellido,
                    correo = correo,
                    fechaNacimiento = fechaNacimiento
                )

            } else {

                establecerCargando(false)

                Log.w(
                    TAG,
                    "Fallo el registro",
                    tarea.exception
                )

                mostrarError(
                    tarea.exception?.message
                        ?: "No se pudo crear la cuenta"
                )
            }
        }
    }

    // ---------------------------------------------------------------
    // SQL CONNECT
    // ---------------------------------------------------------------

    private fun guardarDatosExtra(
        nombre: String,
        apellido: String,
        correo: String,
        fechaNacimiento: String
    ) {

        /*
         * El objeto NO contiene contraseña.
         *
         * El ID del usuario en SQL Connect será el UID
         * de Firebase Authentication mediante:
         *
         * id: String! @default(expr: "auth.uid")
         *
         * El rol inicial será "Usuario".
         */
        val usuario = Usuario(
            nombre = nombre,
            apellido = apellido,
            correo = correo,
            fechaNacimiento = fechaNacimiento,
            rolId = ROL_USUARIO
        )

        usuarioRepository.guardarUsuarioActual(usuario) { exito ->

            establecerCargando(false)

            if (exito) {

                Log.i(
                    TAG,
                    "Datos del usuario guardados en SQL Connect"
                )

                Toast.makeText(
                    this,
                    "Cuenta creada correctamente",
                    Toast.LENGTH_SHORT
                ).show()

                irAMain()

            } else {

                /*
                 * Authentication sí creó la cuenta,
                 * pero SQL Connect falló.
                 */
                Log.e(
                    TAG,
                    "La cuenta fue creada en Auth, pero falló SQL Connect"
                )

                mostrarError(
                    "La cuenta fue creada, pero no se pudieron guardar tus datos"
                )
            }
        }
    }

    // ---------------------------------------------------------------
    // NAVEGACIÓN
    // ---------------------------------------------------------------

    private fun irAMain() {

        val intent = Intent(
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
        binding.btnRegistrar.isEnabled = !cargando
    }

    private fun mostrarError(
        mensaje: String
    ) {
        binding.tvError.text = mensaje
    }
}