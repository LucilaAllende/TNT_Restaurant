package com.example.appcliente.ui.home

import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.appcliente.R
import com.example.appcliente.ui.adapters.SeccionesAdapter
import com.example.appcliente.ui.home.menudia.MenuDiaFragment
import com.example.appcliente.ui.home.menumate.MenuMateFragment
import com.example.appcliente.ui.home.menusemanal.MenuSemanalFragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout

open class HomeFragment : Fragment() {

    var vista: View? = null
    private var appBar: AppBarLayout? = null
    private var pestanas: TabLayout? = null
    private var viewPager: ViewPager? = null

    private var rotacion :Int ?=0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        vista=inflater.inflate(R.layout.fragment_home, container, false)



        if (rotacion === 0) {
            val parent = container!!.parent as View
            if (appBar == null) {
                appBar = parent.findViewById<View>(R.id.appBar) as? AppBarLayout
                pestanas = TabLayout(requireActivity())
                pestanas!!.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"))
                appBar?.addView(pestanas)
                viewPager =
                    vista!!.findViewById<View>(R.id.idViewPagerInformacion) as ViewPager

                llenarViewPager(viewPager!!)
                viewPager!!.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                })
                pestanas!!.setupWithViewPager(viewPager)
            }

            //ESTE CODIGO UTILIZO PARA VOLVER A MOSTRAR LA BARRA
            val appBar: AppBarLayout? = activity?.findViewById<View>(R.id.appBar) as? AppBarLayout
            appBar!!.visibility = View.VISIBLE


            pestanas!!.tabGravity = TabLayout.GRAVITY_FILL
            activity?.findViewById<TabLayout>(R.id.tabs)?.setupWithViewPager(viewPager)
            val tabsi = activity?.findViewById<TabLayout>(R.id.tabs)
            tabsi?.visibility = View.VISIBLE
        } else {
            rotacion = 1
        }
        return vista;
    }

    private fun llenarViewPager(viewPager: ViewPager) {
        val adapter = SeccionesAdapter(getChildFragmentManager())
        adapter.addFragment(MenuDiaFragment(), "Almuerzo")
        adapter.addFragment(MenuSemanalFragment(), "Viandas")
        adapter.addFragment(MenuMateFragment(), "Para el Mate")
        viewPager.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()

        if (this.rotacion == 0) {
            appBar?.removeView(pestanas)
        }
    }

}
