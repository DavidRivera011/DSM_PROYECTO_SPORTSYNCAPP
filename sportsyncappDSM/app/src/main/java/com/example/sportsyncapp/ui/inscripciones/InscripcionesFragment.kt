package com.example.sportsyncapp.ui.registros

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportsyncapp.data.model.Inscripcion
import com.example.sportsyncapp.data.repository.InscripcionRepository
import com.example.sportsyncapp.databinding.FragmentInscripcionesBinding
import com.example.sportsyncapp.ui.adapters.InscripcionesAdapter

class InscripcionesFragment : Fragment() {

    private var _binding: FragmentInscripcionesBinding? = null
    private val binding get() = _binding!!

    private val repository = InscripcionRepository()

    private lateinit var adapter: InscripcionesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInscripcionesBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        configurarLista()

        binding.swipeRefreshInscripciones.setOnRefreshListener {
            cargarInscripciones()
        }

        cargarInscripciones()
    }

    private fun configurarLista() {
        adapter = InscripcionesAdapter(emptyList())

        binding.rvInscripciones.layoutManager =
            LinearLayoutManager(requireContext())

        binding.rvInscripciones.adapter = adapter
    }

    private fun cargarInscripciones() {
        binding.swipeRefreshInscripciones.isRefreshing = true
        binding.tvInscripcionesVacio.visibility = View.GONE

        repository.obtenerInscripciones { resultado ->

            val bindingActual = _binding ?: return@obtenerInscripciones

            bindingActual.swipeRefreshInscripciones.isRefreshing = false

            if (resultado != null) {
                adapter.actualizarLista(resultado)

                bindingActual.tvInscripcionesVacio.visibility =
                    if (resultado.isEmpty()) View.VISIBLE else View.GONE

            } else {
                adapter.actualizarLista(emptyList())

                bindingActual.tvInscripcionesVacio.visibility = View.VISIBLE
                bindingActual.tvInscripcionesVacio.text =
                    "No se pudieron cargar las inscripciones."

                Toast.makeText(
                    requireContext(),
                    "Error al consultar las inscripciones",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
