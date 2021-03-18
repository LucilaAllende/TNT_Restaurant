package com.example.appcliente.ui.seguimiento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.appcliente.R
import com.google.android.material.tabs.TabLayout

class SeguimientoFragment : Fragment() {

    private lateinit var galleryViewModel: SeguimientoViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
                ViewModelProvider(this).get(SeguimientoViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_seguimiento, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        // remove all the tabs from the action bar and deselect the current tab
        //activity?.findViewById<TabLayout>(R.id.tabs)?.removeAllTabs()
        val tabsi = activity?.findViewById<TabLayout>(R.id.tabs)
        tabsi!!.visibility = View.GONE
        return root
    }


}
