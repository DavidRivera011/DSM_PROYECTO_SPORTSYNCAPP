package com.example.sportsyncapp.ui.ranking

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportsyncapp.data.repository.SportSyncRepository
import com.example.sportsyncapp.databinding.ActivityRankingBinding
import com.example.sportsyncapp.ui.adapters.RankingAdapter
import kotlinx.coroutines.launch

class RankingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRankingBinding
    private lateinit var repository: SportSyncRepository
    private lateinit var adapter: RankingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRankingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = SportSyncRepository(this)

        setupRecyclerView()
        setupListeners()
        loadRanking()
    }

    private fun setupRecyclerView() {
        adapter = RankingAdapter(emptyList())
        binding.rvRanking.layoutManager = LinearLayoutManager(this)
        binding.rvRanking.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnBackRanking.setOnClickListener {
            finish()
        }
    }

    private fun loadRanking() {
        lifecycleScope.launch {
            val result = repository.getRanking()
            result.onSuccess { list ->
                adapter.updateList(list)
            }.onFailure {
                Toast.makeText(this@RankingActivity, "Error al cargar el ranking", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
