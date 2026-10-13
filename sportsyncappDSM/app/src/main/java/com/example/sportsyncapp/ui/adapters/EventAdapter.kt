package com.example.sportsyncapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.model.SportEvent

class EventAdapter(
    private var events: List<SportEvent>,
    private val onEventClick: (SportEvent) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvEventTitle)
        val tvDate: TextView = view.findViewById(R.id.tvEventDate)
        val tvInstitution: TextView = view.findViewById(R.id.tvEventInstitution)
        val tvSport: TextView = view.findViewById(R.id.tvEventSport)
        val tvStatusBadge: TextView = view.findViewById(R.id.tvEventStatusBadge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.tvTitle.text = event.nombre
        holder.tvDate.text = event.fecha
        holder.tvInstitution.text = "Sede / Coord: ${event.institucionNombre}"
        holder.tvSport.text = "Deporte: ${event.deporteNombre}"

        holder.tvStatusBadge.text = event.estadoNombre
        when (event.idEstado) {
            2 -> {
                holder.tvStatusBadge.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_badge_live)
            }
            3 -> {
                holder.tvStatusBadge.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_badge_approved)
            }
            else -> {
                holder.tvStatusBadge.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_badge_pending)
            }
        }

        holder.itemView.setOnClickListener { onEventClick(event) }
    }

    override fun getItemCount(): Int = events.size

    fun updateList(newList: List<SportEvent>) {
        events = newList
        notifyDataSetChanged()
    }
}

