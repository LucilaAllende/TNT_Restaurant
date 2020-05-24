package com.example.appcliente.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.appcliente.R
import com.example.appcliente.ui.adapters.SeccionesAdapter
import com.example.appcliente.ui.home.menus.MenuDiaFragment
import com.example.appcliente.ui.home.menus.MenuSemanalFragment
import com.example.appcliente.ui.utils.Utilidades
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContenedorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

open class HomeFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var vista: View? = null
    private var appBar: AppBarLayout? = null
    private var pestanas: TabLayout? = null
    private var viewPager: ViewPager? = null

    private var rotacion :Int ?=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        vista=inflater.inflate(R.layout.fragment_home, container, false);

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
            pestanas!!.tabGravity = TabLayout.GRAVITY_FILL
            activity?.findViewById<TabLayout>(R.id.tabs)?.setupWithViewPager(viewPager)
        } else {
            rotacion = 1
        }

        return vista;
    }

    private fun llenarViewPager(viewPager: ViewPager) {
        val adapter = SeccionesAdapter(fragmentManager)
        adapter.addFragment(MenuDiaFragment(), "Menu del día")
        adapter.addFragment(MenuSemanalFragment(), "Viandas de la semana")
        viewPager.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (rotacion === 0) {
            appBar?.removeView(pestanas)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ContenedorFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
