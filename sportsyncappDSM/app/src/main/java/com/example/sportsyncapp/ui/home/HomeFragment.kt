package com.example.sportsyncapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.repository.SportSyncRepository
import com.example.sportsyncapp.databinding.FragmentHomeBinding
import com.example.sportsyncapp.ui.inscriptions.InscriptionsActivity
import com.example.sportsyncapp.ui.ranking.RankingActivity
import com.example.sportsyncapp.ui.main.MainActivity
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: SportSyncRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = SportSyncRepository(requireContext())

        setupUserGreeting()
        setupQuickAccessClicks()
        loadDashboard()

        binding.swipeRefresh.setOnRefreshListener {
            loadDashboard()
        }
    }

    private fun setupUserGreeting() {
        val user = repository.preferences.currentUser
        val firstName = user?.nombre?.takeIf { it.isNotEmpty() } ?: "Deportista"
        val role = if (repository.preferences.isAdmin()) "Administrador ⚙️" else "FAN 🏅"
        binding.tvWelcomeUser.text = "¡Hola, $firstName!"
        binding.tvUserSubtitle.text = "Modo: $role"
    }

    private fun setupQuickAccessClicks() {
        val mainActivity = activity as? MainActivity

        binding.cardNextEvent.setOnClickListener {
            mainActivity?.selectTab(R.id.nav_agenda)
        }
        binding.cardSports.setOnClickListener {
            mainActivity?.selectTab(R.id.nav_sports)
        }
        binding.cardPlayers.setOnClickListener {
            startActivity(Intent(requireContext(), RankingActivity::class.java))
        }
        binding.cardAwards.setOnClickListener {
            startActivity(Intent(requireContext(), InscriptionsActivity::class.java))
        }
        binding.btnQuickInscriptions.setOnClickListener {
            startActivity(Intent(requireContext(), InscriptionsActivity::class.java))
        }
    }

    private fun loadDashboard() {
        binding.swipeRefresh.isRefreshing = true
        viewLifecycleOwner.lifecycleScope.launch {
            // Load all data in parallel
            val eventsDeferred = async { repository.getEvents() }
            val matchesDeferred = async { repository.getMatches() }
            val inscDeferred = async { repository.getInscriptions() }
            val sportsDeferred = async { repository.getSports() }

            val eventsResult = eventsDeferred.await()
            val matchesResult = matchesDeferred.await()
            val inscResult = inscDeferred.await()
            val sportsResult = sportsDeferred.await()

            binding.swipeRefresh.isRefreshing = false

            // Stats
            eventsResult.onSuccess { events ->
                binding.tvStatEvents.text = events.size.toString()
                binding.tvQuickEventCount.text = "${events.count { it.idEstado == 2 }} en curso"
            }
            matchesResult.onSuccess { matches ->
                binding.tvStatMatches.text = matches.size.toString()

                // Featured match (prefer "En curso", fallback to first)
                val featured = matches.firstOrNull { it.idEstado == 2 } ?: matches.firstOrNull()
                if (featured != null) {
                    binding.tvFeaturedMatchTourney.text = "${featured.eventoNombre ?: "Torneo"} · ${featured.estadoNombre}"
                    binding.tvFeaturedLocal.text = featured.equipoLocalNombre
                    binding.tvFeaturedVisitor.text = featured.equipoVisitanteNombre
                    binding.tvFeaturedScore.text = "${featured.marcadorLocal} - ${featured.marcadorVisitante}"
                    binding.tvFeaturedMatchStatus.text = if (featured.idEstado == 2) "EN VIVO" else featured.estadoNombre.uppercase()
                }
            }
            inscResult.onSuccess { inscriptions ->
                binding.tvStatInscriptions.text = inscriptions.size.toString()
            }
            sportsResult.onSuccess { sports ->
                binding.tvQuickSportCount.text = "${sports.size} disciplinas"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
