package com.example.sportsyncapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.model.Inscription

class InscriptionAdapter(
    private var inscriptions: List<Inscription>,
    private val isAdmin: Boolean,
    private val onCancelClick: (Inscription) -> Unit,
    private val onApproveClick: (Inscription) -> Unit,
    private val onRejectClick: (Inscription) -> Unit
) : RecyclerView.Adapter<InscriptionAdapter.InscriptionViewHolder>() {

    class InscriptionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEvent: TextView = view.findViewById(R.id.tvInscriptionEvent)
        val tvStatus: TextView = view.findViewById(R.id.tvInscriptionStatus)
        val tvType: TextView = view.findViewById(R.id.tvInscriptionType)
        val tvApplicant: TextView = view.findViewById(R.id.tvInscriptionApplicant)
        val tvDate: TextView = view.findViewById(R.id.tvInscriptionDate)
        val layoutAdmin: LinearLayout = view.findViewById(R.id.layoutAdminActions)
        val btnApprove: Button = view.findViewById(R.id.btnApprove)
        val btnReject: Button = view.findViewById(R.id.btnReject)
        val btnCancel: Button = view.findViewById(R.id.btnCancelInscription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InscriptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_inscription, parent, false)
        return InscriptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: InscriptionViewHolder, position: Int) {
        val item = inscriptions[position]
        holder.tvEvent.text = item.eventoNombre
        holder.tvStatus.text = item.estadoNombre

        when (item.idEstado) {
            2 -> {
                holder.tvStatus.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_badge_approved)
            }
            3 -> {
                holder.tvStatus.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_badge_rejected)
            }
            else -> {
                holder.tvStatus.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_badge_pending)
            }
        }

        val typeText = if (item.tipo == "Equipo") {
            "Modalidad: Equipo (${item.equipoNombre ?: "Equipo"})"
        } else {
            "Modalidad: Individual"
        }
        holder.tvType.text = typeText
        holder.tvApplicant.text = "Solicitante: ${item.nombreSolicitante}"
        holder.tvDate.text = "Fecha: ${item.fechaInscripcion}"

        if (isAdmin) {
            holder.layoutAdmin.visibility = if (item.idEstado == 1) View.VISIBLE else View.GONE
            holder.btnApprove.setOnClickListener { onApproveClick(item) }
            holder.btnReject.setOnClickListener { onRejectClick(item) }
            holder.btnCancel.visibility = View.GONE
        } else {
            holder.layoutAdmin.visibility = View.GONE
            holder.btnCancel.visibility = if (item.idEstado == 1) View.VISIBLE else View.GONE
            holder.btnCancel.setOnClickListener { onCancelClick(item) }
        }
    }

    override fun getItemCount(): Int = inscriptions.size

    fun updateList(newList: List<Inscription>) {
        inscriptions = newList
        notifyDataSetChanged()
    }
}

