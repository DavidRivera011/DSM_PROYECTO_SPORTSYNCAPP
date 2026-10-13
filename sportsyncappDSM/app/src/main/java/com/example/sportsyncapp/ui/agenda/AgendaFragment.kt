package com.example.sportsyncapp.ui.agenda

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.model.SportEvent
import com.example.sportsyncapp.data.repository.SportSyncRepository
import com.example.sportsyncapp.databinding.FragmentAgendaBinding
import com.example.sportsyncapp.ui.adapters.EventAdapter
import kotlinx.coroutines.launch
import java.util.Calendar

class AgendaFragment : Fragment() {

    private var _binding: FragmentAgendaBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: SportSyncRepository
    private lateinit var adapter: EventAdapter
    private var allEvents: List<SportEvent> = emptyList()
    private var currentFilter: Int = 0
    private var searchQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgendaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = SportSyncRepository(requireContext())

        setupRecyclerView()
        setupAdminVisibility()
        setupFilterButtons()
        setupSearch()
        loadEvents()

        binding.swipeRefreshAgenda.setOnRefreshListener { loadEvents() }
        binding.btnAddEvent.setOnClickListener { showAddEventDialog() }
    }

    private fun setupRecyclerView() {
        adapter = EventAdapter(emptyList()) { event ->
            showEventDetailDialog(event)
        }
        binding.rvEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEvents.adapter = adapter
    }

    private fun setupAdminVisibility() {
        binding.btnAddEvent.visibility = if (repository.preferences.isAdmin()) View.VISIBLE else View.GONE
    }

    private fun setupSearch() {
        // Check if the binding has etSearchEvent - it might not exist yet in the XML
        // We'll add a TextWatcher if the field exists
        try {
            binding.etSearchEvent.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    searchQuery = s?.toString()?.trim() ?: ""
                    applyFilter()
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        } catch (_: Exception) { /* field not in layout yet */ }
    }

    private fun setupFilterButtons() {
        binding.btnFilterAll.setOnClickListener { currentFilter = 0; updateFilterUi(); applyFilter() }
        binding.btnFilterLive.setOnClickListener { currentFilter = 2; updateFilterUi(); applyFilter() }
        binding.btnFilterPending.setOnClickListener { currentFilter = 1; updateFilterUi(); applyFilter() }
        binding.btnFilterFinished.setOnClickListener { currentFilter = 3; updateFilterUi(); applyFilter() }
    }

    private fun updateFilterUi() {
        val active = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_orange)
        val inactive = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_gray)
        binding.btnFilterAll.background = if (currentFilter == 0) active else inactive
        binding.btnFilterLive.background = if (currentFilter == 2) active else inactive
        binding.btnFilterPending.background = if (currentFilter == 1) active else inactive
        binding.btnFilterFinished.background = if (currentFilter == 3) active else inactive
    }

    private fun applyFilter() {
        var filtered = if (currentFilter == 0) allEvents
        else allEvents.filter { it.idEstado == currentFilter }

        if (searchQuery.isNotEmpty()) {
            filtered = filtered.filter {
                it.nombre.contains(searchQuery, ignoreCase = true) ||
                it.deporteNombre.contains(searchQuery, ignoreCase = true) ||
                it.institucionNombre.contains(searchQuery, ignoreCase = true)
            }
        }
        adapter.updateList(filtered)
    }

    private fun loadEvents() {
        binding.swipeRefreshAgenda.isRefreshing = true
        viewLifecycleOwner.lifecycleScope.launch {
            val result = repository.getEvents()
            binding.swipeRefreshAgenda.isRefreshing = false
            result.onSuccess { list ->
                allEvents = list
                applyFilter()
            }.onFailure {
                Toast.makeText(requireContext(), "Error al cargar la agenda", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showEventDetailDialog(event: SportEvent) {
        val isAdmin = repository.preferences.isAdmin()
        val status = when (event.idEstado) {
            1 -> "â³ Pendiente"
            2 -> "ðŸŸ¢ En Curso"
            3 -> "âœ… Finalizado"
            else -> event.estadoNombre
        }

        val message = buildString {
            appendLine("ðŸ“… Fecha: ${event.fecha}")
            appendLine("âš½ Deporte: ${event.deporteNombre}")
            appendLine("ðŸ¢ Sede: ${event.institucionNombre}")
            appendLine("ðŸ“Œ Estado: $status")
            if (!event.descripcion.isNullOrEmpty()) {
                appendLine()
                appendLine(event.descripcion)
            }
        }

        val builder = AlertDialog.Builder(requireContext())
            .setTitle(event.nombre)
            .setMessage(message)
            .setPositiveButton("Cerrar", null)

        if (isAdmin) {
            builder.setNeutralButton("Eliminar") { _, _ ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Confirmar eliminaciÃ³n")
                    .setMessage("Â¿Eliminar '${event.nombre}'? Esta acciÃ³n no se puede deshacer.")
                    .setPositiveButton("Eliminar") { _, _ ->
                        viewLifecycleOwner.lifecycleScope.launch {
                            repository.deleteEvent(event.idEvento)
                            Toast.makeText(requireContext(), "Evento eliminado", Toast.LENGTH_SHORT).show()
                            loadEvents()
                        }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        }
        builder.show()
    }

    private fun showAddEventDialog() {
        val context = requireContext()
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val etName = EditText(context).apply { hint = "Nombre del evento (ej. Copa Salesiana)" }
        val etSport = EditText(context).apply { hint = "Deporte (FÃºtbol, Basketballâ€¦)" }
        val etInstitution = EditText(context).apply { hint = "Sede / InstituciÃ³n" }
        val etDesc = EditText(context).apply { hint = "DescripciÃ³n (opcional)"; minLines = 2 }

        // Date picker field
        val etDate = EditText(context).apply {
            hint = "Fecha (DD/MM/AAAA)"
            isFocusable = false
            isClickable = true
        }
        etDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(context, { _, y, m, d ->
                etDate.setText(String.format("%02d/%02d/%04d", d, m + 1, y))
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        layout.addView(etName)
        layout.addView(etSport)
        layout.addView(etInstitution)
        layout.addView(etDate)
        layout.addView(etDesc)

        AlertDialog.Builder(context)
            .setTitle("âž• Nuevo Evento Deportivo")
            .setView(layout)
            .setPositiveButton("Crear") { _, _ ->
                val name = etName.text.toString().trim()
                val sport = etSport.text.toString().trim()
                val institution = etInstitution.text.toString().trim()
                val date = etDate.text.toString().trim()
                val desc = etDesc.text.toString().trim()

                if (name.isEmpty()) {
                    Toast.makeText(context, "El nombre del evento es obligatorio", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val newEvent = SportEvent(
                    idEvento = (allEvents.maxOfOrNull { it.idEvento } ?: 0) + 1,
                    nombre = name,
                    deporteNombre = sport.ifEmpty { "FÃºtbol" },
                    institucionNombre = institution.ifEmpty { "UES" },
                    fecha = date.ifEmpty { "Por definir" },
                    idEstado = 1,
                    estadoNombre = "Pendiente",
                    descripcion = desc.ifEmpty { null }
                )

                viewLifecycleOwner.lifecycleScope.launch {
                    val result = repository.createEvent(newEvent)
                    result.onSuccess {
                        Toast.makeText(context, "âœ… Evento '${it.nombre}' registrado", Toast.LENGTH_SHORT).show()
                    }.onFailure {
                        Toast.makeText(context, "Error al crear evento", Toast.LENGTH_SHORT).show()
                    }
                    loadEvents()
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

