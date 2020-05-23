package com.example.appcliente.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class SeccionesAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!) {

    private val listaFragments: MutableList<Fragment> = ArrayList()
    private val listaTitulos: MutableList<String> = ArrayList()

    fun addFragment(fragment: Fragment?, titulo: String?) {
        if (fragment != null) {
            listaFragments.add(fragment)
        }
        if (titulo != null) {
            listaTitulos.add(titulo)
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return listaTitulos[position]
    }

    override fun getItem(position: Int): Fragment {
        return listaFragments[position]
    }

    override fun getCount(): Int {
        return listaFragments.size
    }

}