package com.example.sportsyncapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.sportsyncapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val usuarioRepository = UsuarioRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cargarDatosUsuario()

        binding.btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }
    }

    /**
     * Consulta Firestore para mostrar el nombre completo del usuario.
     * Si el documento todavia no existe (usuario creado solo en
     * Authentication, sin perfil en Firestore), muestra el correo.
     */
    private fun cargarDatosUsuario() {
        val correo = FirebaseAuth.getInstance().currentUser?.email ?: "usuario"

        binding.tvBienvenida.text = "Cargando..."

        usuarioRepository.obtenerUsuarioActual { usuario ->
            binding.tvBienvenida.text = if (usuario != null) {
                "Bienvenido, ${usuario.nombre} ${usuario.apellido}"
            } else {
                "Sesion iniciada: $correo"
            }
        }
    }

    private fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        // Limpia la pila de activities para que no se pueda regresar a Main
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}