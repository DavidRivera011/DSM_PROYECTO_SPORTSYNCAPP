package com.example.sportsyncapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.sportsyncapp.data.repository.SportSyncRepository
import com.example.sportsyncapp.databinding.FragmentProfileBinding
import com.example.sportsyncapp.ui.auth.LoginActivity
import com.example.sportsyncapp.ui.inscriptions.InscriptionsActivity
import com.example.sportsyncapp.ui.institutions.InstitutionsActivity
import com.example.sportsyncapp.ui.ranking.RankingActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: SportSyncRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = SportSyncRepository(requireContext())

        loadUserProfile()
        loadAthleteMetrics()
        setupButtons()
    }

    private fun loadUserProfile() {
        val user = repository.preferences.currentUser
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        val displayName = user?.let {
            val full = "${it.nombre} ${it.apellido}".trim()
            if (full.isBlank()) firebaseUser?.displayName ?: "Deportista SportSync" else full
        } ?: firebaseUser?.displayName ?: "Deportista SportSync"

        val email = user?.correo?.ifEmpty { null }
            ?: firebaseUser?.email
            ?: "usuario@sportsync.com"

        val birthDate = user?.fechaNacimiento?.ifEmpty { null } ?: "No registrada"
        val roleName = if (repository.preferences.isAdmin()) "ADMINISTRADOR" else "FAN"

        binding.tvProfileName.text = displayName
        binding.tvProfileEmail.text = email
        binding.tvProfileBirthDate.text = birthDate
        binding.tvProfileRole.text = roleName

        // Firebase Auth status
        if (firebaseUser != null) {
            val verified = if (firebaseUser.isEmailVerified) "✅ Verificado" else "⚠️ Sin verificar"
            val provider = firebaseUser.providerData.firstOrNull { it.providerId != "firebase" }?.providerId ?: "email"
            binding.tvFirebaseStatus.text = "$verified · Proveedor: $provider"
        } else {
            binding.tvFirebaseStatus.text = "🔒 Sesión local (modo demo)"
        }
    }

    private fun loadAthleteMetrics() {
        viewLifecycleOwner.lifecycleScope.launch {
            val profileResult = repository.getAthleteProfile()
            val inscResult = repository.getInscriptions()

            profileResult.onSuccess { profile ->
                binding.tvAthleteTeam.text = "${profile.equipoNombre} · ${profile.deporteNombre} · ${profile.tipoPerfilNombre}"
                binding.tvAthletePoints.text = profile.puntosTotales.toString()
                binding.tvAthleteMatches.text = profile.partidosJugados.toString()
            }
            inscResult.onSuccess { inscriptions ->
                val user = repository.preferences.currentUser
                val myInsc = if (user != null) {
                    inscriptions.count { it.idUsuarioSolicitante == user.idUsuario }
                } else inscriptions.size
                binding.tvAthleteInscriptions.text = myInsc.toString()
            }
        }
    }

    private fun setupButtons() {
        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }
        binding.btnViewInstitutions.setOnClickListener {
            startActivity(Intent(requireContext(), InstitutionsActivity::class.java))
        }
        binding.btnMyInscriptions.setOnClickListener {
            startActivity(Intent(requireContext(), InscriptionsActivity::class.java))
        }
        binding.btnMyRanking.setOnClickListener {
            startActivity(Intent(requireContext(), RankingActivity::class.java))
        }
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            repository.preferences.clearSession()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadUserProfile()
        loadAthleteMetrics()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
