package com.example.sportsyncapp.ui.matches

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.model.Match
import com.example.sportsyncapp.data.repository.SportSyncRepository
import com.example.sportsyncapp.databinding.FragmentMatchesBinding
import com.example.sportsyncapp.ui.adapters.MatchAdapter
import kotlinx.coroutines.launch

class MatchesFragment : Fragment() {

    private var _binding: FragmentMatchesBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: SportSyncRepository
    private lateinit var adapter: MatchAdapter
    private var allMatches: List<Match> = emptyList()
    private var currentFilter: Int = 0 // 0=Todos, 1=Programado, 2=En curso, 3=Finalizado

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = SportSyncRepository(requireContext())

        setupRecyclerView()
        setupFilterButtons()
        loadMatches()

        binding.swipeRefreshMatches.setOnRefreshListener { loadMatches() }
    }

    private fun setupRecyclerView() {
        val isAdmin = repository.preferences.isAdmin()
        adapter = MatchAdapter(
            emptyList(),
            isAdmin = isAdmin,
            onMatchClick = { match -> showMatchDetailDialog(match) },
            onUpdateScoreClick = { match -> if (isAdmin) showUpdateScoreDialog(match) }
        )
        binding.rvMatches.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMatches.adapter = adapter
    }

    private fun setupFilterButtons() {
        binding.btnFilterMatchAll.setOnClickListener { currentFilter = 0; updateFilterUi(); applyFilter() }
        binding.btnFilterMatchLive.setOnClickListener { currentFilter = 2; updateFilterUi(); applyFilter() }
        binding.btnFilterMatchScheduled.setOnClickListener { currentFilter = 1; updateFilterUi(); applyFilter() }
        binding.btnFilterMatchFinished.setOnClickListener { currentFilter = 3; updateFilterUi(); applyFilter() }
    }

    private fun updateFilterUi() {
        val active = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_orange)
        val inactive = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_gray)
        binding.btnFilterMatchAll.background = if (currentFilter == 0) active else inactive
        binding.btnFilterMatchLive.background = if (currentFilter == 2) active else inactive
        binding.btnFilterMatchScheduled.background = if (currentFilter == 1) active else inactive
        binding.btnFilterMatchFinished.background = if (currentFilter == 3) active else inactive
    }

    private fun applyFilter() {
        val filtered = if (currentFilter == 0) allMatches
        else allMatches.filter { it.idEstado == currentFilter }
        adapter.updateList(filtered)
    }

    private fun loadMatches() {
        binding.swipeRefreshMatches.isRefreshing = true
        viewLifecycleOwner.lifecycleScope.launch {
            val result = repository.getMatches()
            binding.swipeRefreshMatches.isRefreshing = false
            result.onSuccess { matches ->
                allMatches = matches
                applyFilter()
            }.onFailure {
                Toast.makeText(requireContext(), "Error al cargar partidos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showMatchDetailDialog(match: Match) {
        val status = when (match.idEstado) {
            1 -> "📅 Programado"
            2 -> "🟢 En Curso"
            3 -> "✅ Finalizado"
            else -> match.estadoNombre
        }
        val msg = buildString {
            appendLine("🏆 ${match.eventoNombre ?: "Torneo"} ")
            appendLine("📅 Fecha: ${match.fecha} · ${match.hora}")
            appendLine("📌 Estado: $status")
            appendLine()
            appendLine("${match.equipoLocalNombre}  ${match.marcadorLocal} — ${match.marcadorVisitante}  ${match.equipoVisitanteNombre}")
        }
        AlertDialog.Builder(requireContext())
            .setTitle("Detalle del Partido")
            .setMessage(msg)
            .setPositiveButton("Cerrar", null)
            .also { builder ->
                if (repository.preferences.isAdmin()) {
                    builder.setNeutralButton("Actualizar Marcador") { _, _ ->
                        showUpdateScoreDialog(match)
                    }
                }
            }
            .show()
    }

    private fun showUpdateScoreDialog(match: Match) {
        val ctx = requireContext()
        val layout = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 30, 50, 10)
        }

        val tvTitle = TextView(ctx).apply {
            text = "${match.equipoLocalNombre} vs ${match.equipoVisitanteNombre}"
            textSize = 14f
            setTextColor(ContextCompat.getColor(ctx, R.color.sportsync_text_dark))
            setPadding(0, 0, 0, 16)
        }

        val etLocal = EditText(ctx).apply {
            hint = "Goles ${match.equipoLocalNombre}"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
            setText(match.marcadorLocal.toString())
        }
        val etVisitante = EditText(ctx).apply {
            hint = "Goles ${match.equipoVisitanteNombre}"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
            setText(match.marcadorVisitante.toString())
        }

        layout.addView(tvTitle)
        layout.addView(etLocal)
        layout.addView(etVisitante)

        AlertDialog.Builder(ctx)
            .setTitle("⚽ Actualizar Marcador")
            .setView(layout)
            .setPositiveButton("Guardar") { _, _ ->
                val local = etLocal.text.toString().toIntOrNull() ?: 0
                val visitante = etVisitante.text.toString().toIntOrNull() ?: 0
                viewLifecycleOwner.lifecycleScope.launch {
                    val result = repository.updateMatchScore(match.idPartido, local, visitante)
                    result.onSuccess {
                        Toast.makeText(ctx, "Marcador actualizado: $local - $visitante", Toast.LENGTH_SHORT).show()
                    }.onFailure {
                        Toast.makeText(ctx, "Error al actualizar marcador", Toast.LENGTH_SHORT).show()
                    }
                    loadMatches()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
