package com.example.sportsyncapp.ui.inscriptions

import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.model.Inscription
import com.example.sportsyncapp.data.repository.SportSyncRepository
import com.example.sportsyncapp.databinding.ActivityInscriptionsBinding
import com.example.sportsyncapp.ui.adapters.InscriptionAdapter
import kotlinx.coroutines.launch

class InscriptionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInscriptionsBinding
    private lateinit var repository: SportSyncRepository
    private lateinit var adapter: InscriptionAdapter
    private var allInscriptions: List<Inscription> = emptyList()
    private var currentFilterType: String = "Todas" // "Todas", "Equipos", "Individual"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInscriptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = SportSyncRepository(this)

        setupRecyclerView()
        setupFilterButtons()
        setupListeners()
        loadInscriptions()
    }

    private fun setupRecyclerView() {
        val isAdmin = repository.preferences.isAdmin()
        adapter = InscriptionAdapter(
            emptyList(),
            isAdmin = isAdmin,
            onCancelClick = { inscription ->
                cancelInscription(inscription)
            },
            onApproveClick = { inscription ->
                updateStatus(inscription, 2) // Aprobado
            },
            onRejectClick = { inscription ->
                updateStatus(inscription, 3) // Rechazado
            }
        )
        binding.rvInscriptions.layoutManager = LinearLayoutManager(this)
        binding.rvInscriptions.adapter = adapter
    }

    private fun setupFilterButtons() {
        binding.btnFilterInscAll.setOnClickListener {
            currentFilterType = "Todas"
            updateFilterUi()
            applyFilter()
        }
        binding.btnFilterInscTeams.setOnClickListener {
            currentFilterType = "Equipos"
            updateFilterUi()
            applyFilter()
        }
        binding.btnFilterInscIndividual.setOnClickListener {
            currentFilterType = "Individual"
            updateFilterUi()
            applyFilter()
        }
    }

    private fun updateFilterUi() {
        val activeBg = ContextCompat.getDrawable(this, R.drawable.bg_button_orange)
        val inactiveBg = ContextCompat.getDrawable(this, R.drawable.bg_button_gray)

        binding.btnFilterInscAll.background = if (currentFilterType == "Todas") activeBg else inactiveBg
        binding.btnFilterInscTeams.background = if (currentFilterType == "Equipos") activeBg else inactiveBg
        binding.btnFilterInscIndividual.background = if (currentFilterType == "Individual") activeBg else inactiveBg
    }

    private fun applyFilter() {
        val filtered = when (currentFilterType) {
            "Equipos" -> allInscriptions.filter { it.tipo == "Equipo" }
            "Individual" -> allInscriptions.filter { it.tipo == "Individual" }
            else -> allInscriptions
        }
        adapter.updateList(filtered)
    }

    private fun setupListeners() {
        binding.btnBackInscriptions.setOnClickListener {
            finish()
        }

        binding.btnNewInscription.setOnClickListener {
            showNewInscriptionDialog()
        }
    }

    private fun loadInscriptions() {
        lifecycleScope.launch {
            val result = repository.getInscriptions()
            result.onSuccess { list ->
                allInscriptions = list
                applyFilter()
            }.onFailure {
                Toast.makeText(this@InscriptionsActivity, "Error al cargar inscripciones", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cancelInscription(inscription: Inscription) {
        AlertDialog.Builder(this)
            .setTitle("Cancelar Solicitud")
            .setMessage("Â¿Deseas cancelar tu solicitud de inscripciÃ³n a '${inscription.eventoNombre}'?")
            .setPositiveButton("SÃ­, cancelar") { _, _ ->
                lifecycleScope.launch {
                    repository.cancelInscription(inscription.idInscripcion)
                    Toast.makeText(this@InscriptionsActivity, "Solicitud cancelada", Toast.LENGTH_SHORT).show()
                    loadInscriptions()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun updateStatus(inscription: Inscription, newStatus: Int) {
        lifecycleScope.launch {
            repository.updateInscriptionStatus(inscription.idInscripcion, newStatus)
            val actionText = if (newStatus == 2) "Aprobada" else "Rechazada"
            Toast.makeText(this@InscriptionsActivity, "Solicitud $actionText", Toast.LENGTH_SHORT).show()
            loadInscriptions()
        }
    }

    private fun showNewInscriptionDialog() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val rgType = RadioGroup(this).apply {
            orientation = LinearLayout.HORIZONTAL
        }
        val rbTeam = RadioButton(this).apply {
            text = "Por Equipo"
            isChecked = true
        }
        val rbIndividual = RadioButton(this).apply {
            text = "Individual"
        }
        rgType.addView(rbTeam)
        rgType.addView(rbIndividual)

        val etEvent = EditText(this).apply {
            hint = "Nombre del Evento (ej. Torneo Apertura 2024)"
            setText("Torneo Apertura 2024")
        }
        val etTeam = EditText(this).apply {
            hint = "Nombre del Equipo (ej. Ãguilas FC)"
            setText("Ãguilas FC")
        }

        rgType.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == rbIndividual.id) {
                etTeam.visibility = android.view.View.GONE
            } else {
                etTeam.visibility = android.view.View.VISIBLE
            }
        }

        layout.addView(rgType)
        layout.addView(etEvent)
        layout.addView(etTeam)

        AlertDialog.Builder(this)
            .setTitle("Nueva InscripciÃ³n")
            .setView(layout)
            .setPositiveButton("Enviar Solicitud") { _, _ ->
                val isTeam = rbTeam.isChecked
                val eventName = etEvent.text.toString().trim()
                val teamName = if (isTeam) etTeam.text.toString().trim() else null

                val newIns = Inscription(
                    idInscripcion = (allInscriptions.maxOfOrNull { it.idInscripcion } ?: 0) + 1,
                    idEvento = 1,
                    eventoNombre = eventName.ifEmpty { "Torneo Deportivo" },
                    tipo = if (isTeam) "Equipo" else "Individual",
                    equipoNombre = teamName,
                    nombreSolicitante = repository.preferences.currentUser?.nombre ?: "Usuario",
                    fechaInscripcion = "Hoy",
                    idEstado = 1,
                    estadoNombre = "Pendiente"
                )

                lifecycleScope.launch {
                    repository.createInscription(newIns)
                    Toast.makeText(this@InscriptionsActivity, "Â¡Solicitud de inscripciÃ³n enviada con Ã©xito!", Toast.LENGTH_SHORT).show()
                    loadInscriptions()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}

