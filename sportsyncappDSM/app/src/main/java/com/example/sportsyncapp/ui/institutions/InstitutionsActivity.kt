package com.example.sportsyncapp.ui.institutions

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.sportsyncapp.R
import com.example.sportsyncapp.data.repository.SportSyncRepository
import com.example.sportsyncapp.databinding.ActivityInstitutionsBinding
import kotlinx.coroutines.launch

class InstitutionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInstitutionsBinding
    private lateinit var repository: SportSyncRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInstitutionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = SportSyncRepository(this)

        binding.btnBackInstitutions.setOnClickListener { finish() }

        loadInstitutions()
    }

    private fun loadInstitutions() {
        lifecycleScope.launch {
            val result = repository.getInstitutions()
            result.onSuccess { list ->
                binding.layoutInstitutionsContainer.removeAllViews()
                for (inst in list) {
                    val card = CardView(this@InstitutionsActivity).apply {
                        radius = 40f
                        setCardBackgroundColor(ContextCompat.getColor(this@InstitutionsActivity, R.color.sportsync_card_dark))
                        cardElevation = 6f
                        val params = android.widget.LinearLayout.LayoutParams(
                            android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(0, 0, 0, 32)
                        }
                        layoutParams = params
                    }

                    val innerLayout = android.widget.LinearLayout(this@InstitutionsActivity).apply {
                        orientation = android.widget.LinearLayout.VERTICAL
                        setPadding(40, 36, 40, 36)
                    }

                    val tvName = TextView(this@InstitutionsActivity).apply {
                        text = inst.nombre
                        textSize = 20f
                        setTextColor(ContextCompat.getColor(this@InstitutionsActivity, R.color.sportsync_text_light))
                        setTypeface(null, android.graphics.Typeface.BOLD)
                    }

                    val tvDesc = TextView(this@InstitutionsActivity).apply {
                        text = inst.descripcion ?: "Sede deportiva institucional"
                        textSize = 13f
                        setTextColor(ContextCompat.getColor(this@InstitutionsActivity, R.color.sportsync_peach))
                        setPadding(0, 8, 0, 16)
                    }

                    val tvSports = TextView(this@InstitutionsActivity).apply {
                        text = "Disciplinas: ${inst.deportes.joinToString(", ")}"
                        textSize = 13f
                        setTextColor(ContextCompat.getColor(this@InstitutionsActivity, R.color.sportsync_primary_orange))
                        setTypeface(null, android.graphics.Typeface.BOLD)
                    }

                    innerLayout.addView(tvName)
                    innerLayout.addView(tvDesc)
                    innerLayout.addView(tvSports)

                    card.addView(innerLayout)
                    binding.layoutInstitutionsContainer.addView(card)
                }
            }
        }
    }
}

