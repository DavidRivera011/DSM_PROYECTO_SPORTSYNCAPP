package com.example.sportsyncapp.ui.adapters

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsyncapp.R

data class FiltroDeporte(
    val id: String?,
    val nombre: String
)

class FiltrosDeportesAdapter(
    private var filtros: List<FiltroDeporte>,
    private var seleccionadoId: String?,
    private val onSeleccionar: (FiltroDeporte) -> Unit
) : RecyclerView.Adapter<FiltrosDeportesAdapter.FiltroViewHolder>() {

    class FiltroViewHolder(val texto: TextView) :
        RecyclerView.ViewHolder(texto)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FiltroViewHolder {
        val texto = TextView(parent.context).apply {
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                40.dp(parent)
            ).apply {
                marginEnd = 8.dp(parent)
            }

            gravity = Gravity.CENTER
            setPadding(18.dp(parent), 0, 18.dp(parent), 0)
            textSize = 13f
            maxLines = 1
            includeFontPadding = false
            elevation = 0f
            isClickable = true
            isFocusable = true
        }

        return FiltroViewHolder(texto)
    }

    override fun onBindViewHolder(
        holder: FiltroViewHolder,
        position: Int
    ) {
        val filtro = filtros[position]
        val seleccionado = filtro.id == seleccionadoId

        holder.texto.apply {
            text = filtro.nombre

            setTextColor(
                if (seleccionado) {
                    Color.WHITE
                } else {
                    Color.rgb(91, 85, 79)
                }
            )

            setTypeface(
                null,
                if (seleccionado) Typeface.BOLD else Typeface.NORMAL
            )

            background = ContextCompat.getDrawable(
                context,
                if (seleccionado) {
                    R.drawable.bg_filter_selected
                } else {
                    R.drawable.bg_filter_unselected
                }
            )

            alpha = 1f

            setOnClickListener {
                if (seleccionadoId != filtro.id) {
                    seleccionadoId = filtro.id
                    notifyDataSetChanged()
                    onSeleccionar(filtro)
                }
            }
        }
    }

    override fun getItemCount(): Int = filtros.size

    fun actualizar(
        nuevosFiltros: List<FiltroDeporte>,
        nuevoSeleccionadoId: String?
    ) {
        filtros = nuevosFiltros
        seleccionadoId = nuevoSeleccionadoId
        notifyDataSetChanged()
    }

    private fun Int.dp(parent: ViewGroup): Int {
        return (this * parent.resources.displayMetrics.density).toInt()
    }
}
