package com.example.appempleado

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.appempleado.databinding.FragmentPortadaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class FragmentPortada : Fragment() {
    private var _binding: FragmentPortadaBinding? = null
    private val binding get() = _binding!!
    var callback: OnBackPressedCallback? = null

    val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                finishAffinity(requireActivity())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(),callback!!)
    }

    override fun onStop() {
        super.onStop()
        callback!!.remove()
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreate(savedInstanceState)
        var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            findNavController().navigate(R.id.action_fragmentPortada_to_fragmentHome,null)
        }
        _binding = FragmentPortadaBinding.inflate(inflater, container, false)
        binding.btnLogin.setOnClickListener { login() }
        binding.btnRegistrarse.setOnClickListener{ signin() }
        val view = binding.root
        return view
    }

    private fun login(){
        findNavController().navigate(R.id.action_fragmentPortada_to_fragmentLogin,null,options)
    }

    private fun signin(){
        findNavController().navigate(R.id.action_fragmentPortada_to_fragmentSignin,null,options)
    }

}
