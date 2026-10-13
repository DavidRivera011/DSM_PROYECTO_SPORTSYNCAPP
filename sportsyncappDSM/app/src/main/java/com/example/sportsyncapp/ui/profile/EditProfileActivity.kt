package com.example.sportsyncapp.ui.profile

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sportsyncapp.data.local.PreferencesManager
import com.example.sportsyncapp.data.model.User
import com.example.sportsyncapp.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import java.util.Calendar

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var prefs: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = PreferencesManager(this)

        loadCurrentData()
        setupDatePicker()
        setupListeners()
    }

    private fun loadCurrentData() {
        val user = prefs.currentUser
        val fbUser = FirebaseAuth.getInstance().currentUser

        // Prefer locally saved data; fall back to Firebase Auth fields
        binding.etEditName.setText(user?.nombre?.ifEmpty { fbUser?.displayName?.split(" ")?.firstOrNull() ?: "Usuario" } ?: fbUser?.displayName ?: "Usuario")
        binding.etEditLastName.setText(user?.apellido ?: "")
        binding.etEditEmail.setText(user?.correo?.ifEmpty { fbUser?.email ?: "" } ?: fbUser?.email ?: "")
        binding.etEditBirthDate.setText(user?.fechaNacimiento ?: "")
    }

    private fun setupDatePicker() {
        binding.etEditBirthDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR) - 20
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formatted = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                binding.etEditBirthDate.setText(formatted)
            }, year, month, day).show()
        }
    }

    private fun setupListeners() {
        binding.btnBackEditProfile.setOnClickListener { finish() }

        binding.btnSaveChanges.setOnClickListener {
            val name = binding.etEditName.text.toString().trim()
            val lastName = binding.etEditLastName.text.toString().trim()
            val email = binding.etEditEmail.text.toString().trim()
            val birthDate = binding.etEditBirthDate.text.toString().trim()
            val newPass = binding.etEditPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "El nombre y el correo son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 1) Save locally in SharedPreferences
            val updatedUser = User(
                idUsuario = prefs.currentUser?.idUsuario ?: 1,
                nombre = name,
                apellido = lastName,
                correo = email,
                fechaNacimiento = birthDate,
                idRol = prefs.userRole,
                rolNombre = if (prefs.isAdmin()) "ADMINISTRADOR" else "FAN"
            )
            prefs.currentUser = updatedUser

            // 2) Sync display name to Firebase Auth (fire-and-forget; UI doesn't block)
            val fbUser = FirebaseAuth.getInstance().currentUser
            if (fbUser != null) {
                val displayName = if (lastName.isNotEmpty()) "$name $lastName" else name
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build()
                fbUser.updateProfile(profileUpdates)
                    .addOnFailureListener { e ->
                        // Non-critical: local data already saved
                        Toast.makeText(this, "Firebase sync: ${e.message}", Toast.LENGTH_SHORT).show()
                    }

                // 3) Update password in Firebase if provided
                if (newPass.isNotEmpty() && newPass.length >= 6) {
                    fbUser.updatePassword(newPass)
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al cambiar contraseña: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                }

                // 4) Update email in Firebase if changed
                val oldEmail = prefs.currentUser?.correo ?: ""
                if (email != fbUser.email && email.isNotEmpty()) {
                    fbUser.verifyBeforeUpdateEmail(email)
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Verifica el nuevo correo antes del cambio: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                }
            }

            // Show confirmation dialog (as in Figma)
            AlertDialog.Builder(this)
                .setTitle("¡Éxito!")
                .setMessage("Perfil actualizado correctamente.")
                .setPositiveButton("Aceptar") { _, _ ->
                    finish()
                }
                .setCancelable(false)
                .show()
        }
    }
}
