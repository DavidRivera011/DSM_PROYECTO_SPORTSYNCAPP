package com.example.sportsyncapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sportsyncapp.ui.deportes.DeportesFragment
import com.example.sportsyncapp.ui.eventos.EventosFragment
import com.example.sportsyncapp.ui.home.AdminHomeFragment
import com.example.sportsyncapp.ui.home.HomeFragment
import com.example.sportsyncapp.ui.partidos.PartidosFragment
import com.example.sportsyncapp.ui.perfil.PerfilFragment
import com.example.sportsyncapp.ui.registros.RegistrosFragment

class MainPagerAdapter(
    activity: AppCompatActivity,
    private val esAdministrador: Boolean
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return if (esAdministrador) {
            when (position) {
                0 -> AdminHomeFragment()
                1 -> RegistrosFragment()
                2 -> EventosFragment()
                3 -> PartidosFragment()
                4 -> PerfilFragment()
                else -> AdminHomeFragment()
            }
        } else {
            when (position) {
                0 -> HomeFragment()
                1 -> EventosFragment()
                2 -> DeportesFragment()
                3 -> PartidosFragment()
                4 -> PerfilFragment()
                else -> HomeFragment()
            }
        }
    }
}
