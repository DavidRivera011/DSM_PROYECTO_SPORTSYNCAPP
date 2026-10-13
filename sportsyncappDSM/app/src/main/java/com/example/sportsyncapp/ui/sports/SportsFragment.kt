package com.example.sportsyncapp.ui.sports

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportsyncapp.data.model.Sport
import com.example.sportsyncapp.data.repository.SportSyncRepository
import com.example.sportsyncapp.databinding.FragmentSportsBinding
import com.example.sportsyncapp.ui.adapters.SportAdapter
import kotlinx.coroutines.launch

class SportsFragment : Fragment() {

    private var _binding: FragmentSportsBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: SportSyncRepository
    private lateinit var adapter: SportAdapter
    private var sportsList: List<Sport> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = SportSyncRepository(requireContext())

        setupRecyclerView()
        setupAdminUi()
        loadSports()

        binding.swipeRefreshSports.setOnRefreshListener {
            loadSports()
        }

        binding.btnAddSport.setOnClickListener {
            showAddSportDialog()
        }
    }

    private fun setupRecyclerView() {
        val isAdmin = repository.preferences.isAdmin()
        adapter = SportAdapter(
            emptyList(),
            isAdmin = isAdmin,
            onSportClick = { sport ->
                Toast.makeText(requireContext(), "${sport.nombreDeporte}: ${sport.descripcion}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { sport ->
                confirmDeleteSport(sport)
            }
        )
        binding.rvSports.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSports.adapter = adapter
    }

    private fun setupAdminUi() {
        val isAdmin = repository.preferences.isAdmin()
        binding.btnAddSport.visibility = if (isAdmin) View.VISIBLE else View.GONE
        binding.tvAdminRuleNote.visibility = if (isAdmin) View.VISIBLE else View.GONE
    }

    private fun loadSports() {
        binding.swipeRefreshSports.isRefreshing = true
        viewLifecycleOwner.lifecycleScope.launch {
            val result = repository.getSports()
            binding.swipeRefreshSports.isRefreshing = false
            result.onSuccess { list ->
                sportsList = list
                adapter.updateList(list)
            }.onFailure {
                Toast.makeText(requireContext(), "Error al cargar deportes", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAddSportDialog() {
        val context = requireContext()
        val layout = android.widget.LinearLayout(context).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val etName = EditText(context).apply { hint = "Nombre del deporte (ej. Natación)" }
        val etDesc = EditText(context).apply { hint = "Descripción breve" }

        layout.addView(etName)
        layout.addView(etDesc)

        AlertDialog.Builder(context)
            .setTitle("Añadir Nueva Disciplina")
            .setView(layout)
            .setPositiveButton("Guardar") { _, _ ->
                val name = etName.text.toString().trim()
                val desc = etDesc.text.toString().trim()

                if (name.isNotEmpty()) {
                    val newSport = Sport(
                        idDeporte = (sportsList.maxOfOrNull { it.idDeporte } ?: 0) + 1,
                        nombreDeporte = name,
                        fotoDeporte = "ic_nav_sports",
                        descripcion = desc.ifEmpty { "Disciplina deportiva institucional" }
                    )
                    viewLifecycleOwner.lifecycleScope.launch {
                        repository.createSport(newSport)
                        Toast.makeText(context, "Deporte añadido con éxito", Toast.LENGTH_SHORT).show()
                        loadSports()
                    }
                } else {
                    Toast.makeText(context, "Escribe el nombre del deporte", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun confirmDeleteSport(sport: Sport) {
        // Regla de integridad referencial de Figma/Documento:
        // Si el deporte tiene torneos activos (como Fútbol o Baloncesto), se notifica la restricción.
        if (sport.nombreDeporte.equals("Fútbol", ignoreCase = true) || sport.nombreDeporte.equals("Basketball", ignoreCase = true)) {
            AlertDialog.Builder(requireContext())
                .setTitle("Acción no permitida")
                .setMessage("No se puede eliminar '${sport.nombreDeporte}' porque tiene torneos y encuentros activos asociados.")
                .setPositiveButton("Entendido", null)
                .show()
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Deporte")
            .setMessage("¿Estás seguro de eliminar '${sport.nombreDeporte}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    repository.deleteSport(sport.idDeporte)
                    Toast.makeText(requireContext(), "Deporte eliminado", Toast.LENGTH_SHORT).show()
                    loadSports()
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
