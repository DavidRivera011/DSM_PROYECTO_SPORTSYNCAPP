package com.example.sportsyncapp.ui.auth

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sportsyncapp.data.local.PreferencesManager
import com.example.sportsyncapp.data.model.User
import com.example.sportsyncapp.databinding.ActivityRegisterBinding
import com.example.sportsyncapp.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var prefs: PreferencesManager

    // Temporal storage between steps
    private var firstName: String = ""
    private var lastName: String = ""
    private var birthDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        prefs = PreferencesManager(this)

        setupDatePicker()
        setupListeners()
    }

    private fun setupDatePicker() {
        binding.etBirthDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR) - 18
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                binding.etBirthDate.setText(formattedDate)
            }, year, month, day)

            dpd.show()
        }
    }

    private fun setupListeners() {
        // --- PASO 1 ---
        binding.btnCancelStep1.setOnClickListener {
            finish()
        }

        binding.btnContinueStep1.setOnClickListener {
            firstName = binding.etName.text.toString().trim()
            lastName = binding.etLastName.text.toString().trim()
            birthDate = binding.etBirthDate.text.toString().trim()

            if (firstName.isEmpty() || lastName.isEmpty() || birthDate.isEmpty()) {
                Toast.makeText(this, "Completa todos los datos personales", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cambiar a Paso 2
            binding.layoutStep1.visibility = View.GONE
            binding.layoutStep2.visibility = View.VISIBLE
            binding.tvStepIndicator.text = "Paso 2 de 2: Credenciales de acceso"
        }

        // --- PASO 2 ---
        binding.btnBackStep2.setOnClickListener {
            binding.layoutStep2.visibility = View.GONE
            binding.layoutStep1.visibility = View.VISIBLE
            binding.tvStepIndicator.text = "Paso 1 de 2: Datos Personales"
        }

        binding.btnConfirmRegister.setOnClickListener {
            val email = binding.etRegisterEmail.text.toString().trim()
            val password = binding.etRegisterPassword.text.toString().trim()
            val repeatPassword = binding.etRepeatPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != repeatPassword) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!binding.cbTerms.isChecked) {
                Toast.makeText(this, "Debes aceptar los Términos y Condiciones", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            performRegistration(email, password)
        }
    }

    private fun performRegistration(email: String, pass: String) {
        // Show loading state
        binding.btnConfirmRegister.isEnabled = false
        binding.btnConfirmRegister.text = "Registrando..."

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener { result ->
                // Set display name in Firebase Auth
                val displayName = "$firstName $lastName"
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build()
                result.user?.updateProfile(profileUpdates)

                // Send email verification
                result.user?.sendEmailVerification()

                saveUserLocally(email, idUsuario = result.user?.uid?.hashCode()?.and(0x7FFFFFFF) ?: 1)

                Toast.makeText(
                    this,
                    "¡Cuenta creada con éxito! Revisa tu correo para verificarla.",
                    Toast.LENGTH_LONG
                ).show()
                goToHome()
            }
            .addOnFailureListener { e ->
                binding.btnConfirmRegister.isEnabled = true
                binding.btnConfirmRegister.text = "Confirmar Registro"

                when (e) {
                    is FirebaseAuthUserCollisionException -> {
                        // Email already in use → show real error
                        Toast.makeText(this, "Este correo ya está registrado. Inicia sesión.", Toast.LENGTH_LONG).show()
                    }
                    is FirebaseAuthWeakPasswordException -> {
                        Toast.makeText(this, "La contraseña es demasiado débil.", Toast.LENGTH_LONG).show()
                    }
                    is FirebaseAuthInvalidCredentialsException -> {
                        Toast.makeText(this, "El correo electrónico no es válido.", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        // Network error, project not configured, etc. → allow demo/offline mode
                        val isNetworkError = e.message?.contains("network", ignoreCase = true) == true ||
                                e.message?.contains("connection", ignoreCase = true) == true ||
                                e.message?.contains("CONFIGURATION_NOT_FOUND", ignoreCase = true) == true ||
                                e.message?.contains("unable to resolve host", ignoreCase = true) == true

                        if (isNetworkError) {
                            saveUserLocally(email, idUsuario = 1)
                            Toast.makeText(this, "Registro local completado (sin conexión). Datos guardados.", Toast.LENGTH_LONG).show()
                            goToHome()
                        } else {
                            Toast.makeText(this, "Error al registrar: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
    }

    private fun saveUserLocally(email: String, idUsuario: Int) {
        val newUser = User(
            idUsuario = idUsuario,
            nombre = firstName,
            apellido = lastName,
            correo = email,
            fechaNacimiento = birthDate,
            idRol = 1,
            rolNombre = "FAN"
        )
        prefs.isLoggedIn = true
        prefs.userRole = 1
        prefs.currentUser = newUser
    }

    private fun goToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
