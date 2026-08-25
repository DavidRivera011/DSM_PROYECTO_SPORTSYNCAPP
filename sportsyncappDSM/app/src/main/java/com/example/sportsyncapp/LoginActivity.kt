package com.example.sportsyncapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.sportsyncapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LoginActivity"
    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        configurarListeners()
    }

    override fun onStart() {
        super.onStart()
        // Si ya existe una sesion activa, se omite el login
        auth.currentUser?.let { usuario ->
            Log.i(TAG, "Sesion ya activa para: ${usuario.email}")
            irAMain()
        }
    }

    private fun configurarListeners() {
        binding.btnIniciarSesion.setOnClickListener {
            validarYIniciarSesion()
        }

        binding.tvIrARegistro.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

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
                    Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                    irAMain()
                } else {
                    Log.w(TAG, "Fallo el inicio de sesion", tarea.exception)
                    mostrarError("Correo o contrasena incorrectos")
                }
            }
    }

    private fun irAMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

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