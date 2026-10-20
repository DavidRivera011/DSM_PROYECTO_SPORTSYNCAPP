package com.example.sportsyncapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.model.RankingItem

class RankingAdapter(
    private var rankings: List<RankingItem>
) : RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    class RankingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPos: TextView = view.findViewById(R.id.tvRankPosition)
        val tvName: TextView = view.findViewById(R.id.tvRankName)
        val tvTeam: TextView = view.findViewById(R.id.tvRankTeam)
        val tvPoints: TextView = view.findViewById(R.id.tvRankPoints)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ranking, parent, false)
        return RankingViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val item = rankings[position]
        holder.tvPos.text = "#${item.posicion}"
        holder.tvName.text = item.nombre
        holder.tvTeam.text = "${item.equipoOInstitucion} - ${item.deporte}"
        holder.tvPoints.text = "${item.puntos} pts"

        // Gold, Silver, Bronze highlights for top 3
        when (item.posicion) {
            1 -> holder.tvPos.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_badge_pending)
            2 -> holder.tvPos.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_button_gray)
            3 -> holder.tvPos.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_button_orange)
            else -> holder.tvPos.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_button_gray)
        }
    }

    override fun getItemCount(): Int = rankings.size

    fun updateList(newList: List<RankingItem>) {
        rankings = newList
        notifyDataSetChanged()
    }
}
