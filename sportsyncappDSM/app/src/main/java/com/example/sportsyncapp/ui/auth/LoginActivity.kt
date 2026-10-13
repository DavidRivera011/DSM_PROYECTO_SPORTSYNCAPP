package com.example.sportsyncapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import com.example.sportsyncapp.data.local.PreferencesManager
import com.example.sportsyncapp.data.model.User
import com.example.sportsyncapp.data.repository.SportSyncRepository
import com.example.sportsyncapp.databinding.ActivityLoginBinding
import com.example.sportsyncapp.ui.main.MainActivity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var prefs: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        prefs = PreferencesManager(this)

        setupSavedCredentials()
        setupListeners()
    }

    private fun setupSavedCredentials() {
        if (prefs.rememberEmail && prefs.savedEmail.isNotEmpty()) {
            binding.etEmail.setText(prefs.savedEmail)
            binding.cbRememberMe.isChecked = true
        }
    }

    private fun setupListeners() {
        binding.btnConfirmLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "La contraseña debe tener mínimo 6 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Guardar preferencia de recordar
            prefs.rememberEmail = binding.cbRememberMe.isChecked
            if (binding.cbRememberMe.isChecked) {
                prefs.savedEmail = email
            } else {
                prefs.savedEmail = ""
            }

            loginWithFirebase(email, password)
        }

        binding.tvForgotPassword.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Ingresa tu correo para restablecer la contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    Toast.makeText(this, "Correo de restablecimiento enviado con éxito", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }

        binding.tvGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnGoogle.setOnClickListener {
            loginWithGoogle()
        }

        binding.btnGithub.setOnClickListener {
            loginWithOAuth("github.com")
        }

        // Accesos rápidos para evaluación de roles
        binding.btnQuickFan.setOnClickListener {
            prefs.isLoggedIn = true
            prefs.userRole = 1 // FAN
            prefs.currentUser = User(
                idUsuario = 1,
                nombre = "Luis Fernando",
                apellido = "Gómez",
                correo = "usuario@gmail.com",
                fechaNacimiento = "15/06/2002",
                idRol = 1,
                rolNombre = "FAN"
            )
            goToHome()
        }

        binding.btnQuickAdmin.setOnClickListener {
            prefs.isLoggedIn = true
            prefs.userRole = 2 // ADMIN
            prefs.currentUser = User(
                idUsuario = 99,
                nombre = "Administrador",
                apellido = "SportSync",
                correo = "admin@sportsync.com",
                fechaNacimiento = "01/01/1990",
                idRol = 2,
                rolNombre = "ADMINISTRADOR"
            )
            goToHome()
        }
    }

    private fun loginWithFirebase(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                prefs.isLoggedIn = true
                prefs.userRole = if (email.contains("admin", ignoreCase = true)) 2 else 1
                prefs.currentUser = User(
                    idUsuario = 1,
                    nombre = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                    correo = email,
                    idRol = prefs.userRole
                )
                SportSyncRepository(this).syncCurrentUserToFirebase()
                Toast.makeText(this, "¡Bienvenido a SportSync!", Toast.LENGTH_SHORT).show()
                goToHome()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Error al iniciar sesión: ${error.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun loginWithGoogle() {
        val webClientId = "486798513111-t5rv898mp8ohi7lb1vkcr3hep5vlg2od.apps.googleusercontent.com"
        val credentialManager = CredentialManager.create(this)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = this@LoginActivity
                )
                val credential = result.credential
                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

                    auth.signInWithCredential(firebaseCredential)
                        .addOnSuccessListener {
                            val user = auth.currentUser
                            prefs.isLoggedIn = true
                            prefs.userRole = 1
                            prefs.currentUser = User(
                                idUsuario = 1,
                                nombre = user?.displayName ?: "Usuario Google",
                                correo = user?.email ?: "",
                                idRol = 1
                            )
                            SportSyncRepository(this@LoginActivity).syncCurrentUserToFirebase()
                            Toast.makeText(this@LoginActivity, "¡Sesión iniciada con Google!", Toast.LENGTH_SHORT).show()
                            goToHome()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this@LoginActivity, "Error Firebase: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Cancelado o Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginWithOAuth(providerId: String) {
        val provider = OAuthProvider.newBuilder(providerId)
        auth.startActivityForSignInWithProvider(this, provider.build())
            .addOnSuccessListener {
                val user = auth.currentUser
                prefs.isLoggedIn = true
                prefs.userRole = 1
                prefs.currentUser = User(
                    idUsuario = 1,
                    nombre = user?.displayName ?: "Usuario $providerId",
                    correo = user?.email ?: "",
                    idRol = 1
                )
                SportSyncRepository(this).syncCurrentUserToFirebase()
                Toast.makeText(this, "¡Sesión iniciada con $providerId!", Toast.LENGTH_SHORT).show()
                goToHome()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error en $providerId: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun goToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
