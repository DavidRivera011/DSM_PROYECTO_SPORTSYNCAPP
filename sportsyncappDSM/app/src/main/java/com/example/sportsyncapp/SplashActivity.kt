package com.example.sportsyncapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.sportsyncapp.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "SplashActivity"
    }

    private lateinit var binding: ActivitySplashBinding
    private lateinit var auth: FirebaseAuth

    private val usuarioRepository = UsuarioRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        Log.d(
            TAG,
            "SplashActivity iniciada"
        )

        comprobarSesion()
    }

    private fun comprobarSesion() {

        val usuario = auth.currentUser

        if (usuario == null) {

            Log.i(
                TAG,
                "No existe una sesión activa"
            )

            irALogin()

            return
        }

        Log.i(
            TAG,
            "Sesión encontrada para: ${usuario.email}"
        )

        comprobarUsuarioSQL()
    }

    private fun comprobarUsuarioSQL() {

        usuarioRepository.obtenerUsuarioActual { usuarioExistente ->

            if (usuarioExistente != null) {

                Log.i(
                    TAG,
                    "Usuario encontrado en SQL Connect"
                )

                irAMain()

            } else {

                Log.i(
                    TAG,
                    "Existe sesión en Firebase pero no existe perfil en SQL Connect"
                )

                /*
                 * Volvemos a LoginActivity para que su lógica
                 * determine qué debe hacer con el usuario OAuth.
                 */
                irALogin()
            }
        }
    }

    private fun irALogin() {

        val intent =
            Intent(
                this,
                LoginActivity::class.java
            )

        startActivity(intent)

        finish()
    }

    private fun irAMain() {

        val intent =
            Intent(
                this,
                MainActivity::class.java
            )

        startActivity(intent)

        finish()
    }
}