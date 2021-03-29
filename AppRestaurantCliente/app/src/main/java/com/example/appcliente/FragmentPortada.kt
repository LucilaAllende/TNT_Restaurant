package com.example.appcliente

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.appcliente.databinding.FragmentPortadaBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout


class FragmentPortada : Fragment() {
    private var _binding: FragmentPortadaBinding? = null
    private val binding get() = _binding!!
    val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentPortadaBinding.inflate(inflater, container, false)
        val view = binding.root

        /*
        var appBar: AppBarLayout? = activity?.findViewById<View>(R.id.appBar) as? AppBarLayout
        appBar!!.setExpanded(false,false)
        appBar!!.visibility = View.GONE


        var hide:Boolean = true
        var lp: ViewGroup.LayoutParams = appBar!!.layoutParams
        lp.height = if (hide) 0 else lp.height
        appBar!!.setLayoutParams(lp)
        appBar!!.setExpanded(!hide, true)*/

/*        val appBar: AppBarLayout? = activity?.findViewById<View>(R.id.appBar) as? AppBarLayout
        appBar!!.visibility = View.GONE

                val tolBar: Toolbar? = activity?.findViewById<View>(R.id.toolbar) as? Toolbar
        tolBar?.visibility = View.GONE
        */

        val appBar: AppBarLayout? = activity?.findViewById<View>(R.id.appBar) as? AppBarLayout
        appBar!!.visibility = View.GONE

        val tolBar: Toolbar? = activity?.findViewById<View>(R.id.toolbar) as? Toolbar
        tolBar?.visibility = View.GONE

        val tabsi = activity?.findViewById<TabLayout>(R.id.tabs)
        tabsi?.visibility = View.GONE


        binding.btnLogin.setOnClickListener { login() }
        binding.btnRegistrarse.setOnClickListener{ signin() }
        return view
    }

    private fun login(){
        findNavController().navigate(R.id.action_fragmentPortada_to_fragmentLogin,null,options)
    }

    private fun signin(){
        findNavController().navigate(R.id.action_fragmentPortada_to_fragmentSinging,null,options)
    }

}
