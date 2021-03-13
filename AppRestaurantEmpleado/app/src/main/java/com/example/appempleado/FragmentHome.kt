package com.example.appempleado

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.appempleado.databinding.FragmentHomeBinding


class FragmentHome : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var callback: OnBackPressedCallback? = null

    private val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    override fun onStart() {
        super.onStart()
        callback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Seguro quieres salir?")
                    .setCancelable(false)
                    .setPositiveButton("Si") { _, _ ->
                        ActivityCompat.finishAffinity(requireActivity())
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(),callback!!)
    }

    override fun onStop() {
        super.onStop()
        callback!!.remove()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.btnCargarMenues.setOnClickListener { irCargarMenues() }
        binding.btnVerPedidos.setOnClickListener { irVerPedidos() }

        return view
    }

    private fun irVerPedidos() {
        findNavController().navigate(R.id.action_fragmentHome_to_fragmentListaPedidos,null, options)

    }

    private fun irCargarMenues() {
        findNavController().navigate(R.id.action_fragmentHome_to_fragmentCargarMenu,null, options)
    }

}
