package com.example.appcliente.ui.historial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.example.appcliente.R
import com.google.android.material.tabs.TabLayout


class HistorialFragment : Fragment() {

    private lateinit var slideshowViewModel: HistorialViewModel
    var vista: View? = null
    private var viewPager: ViewPager? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
                ViewModelProviders.of(this).get(HistorialViewModel::class.java)
        vista= inflater.inflate(R.layout.fragment_historial, container, false)

        activity?.findViewById<TabLayout>(R.id.tabs)?.removeAllTabs() // remove all the tabs from the action bar and deselect the current tab

        return vista
    }
}
