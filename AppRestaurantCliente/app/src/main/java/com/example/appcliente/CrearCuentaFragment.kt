package com.example.appcliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.appcliente.databinding.FragmentCrearCuentaBinding

/**
 * A simple [Fragment] subclass.
 */
class CrearCuentaFragment : Fragment() {

    private var _binding: FragmentCrearCuentaBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrearCuentaBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.buttonInicar.setOnClickListener { startClicked() }
        return view
    }


    private fun startClicked() {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
        findNavController().navigate(R.id.action_crearCuentaFragment_to_inicioUsuarioFragment2, null, options)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
