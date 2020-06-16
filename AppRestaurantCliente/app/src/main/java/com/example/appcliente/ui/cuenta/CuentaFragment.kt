package com.example.appcliente.ui.cuenta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.appcliente.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout

class CuentaFragment : Fragment() {

    private lateinit var galleryViewModel: CuentaViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
                ViewModelProviders.of(this).get(CuentaViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_cuenta, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        activity?.findViewById<TabLayout>(R.id.tabs)?.removeAllTabs() // remove all the tabs from the action bar and deselect the current tab
        return root
    }


}
