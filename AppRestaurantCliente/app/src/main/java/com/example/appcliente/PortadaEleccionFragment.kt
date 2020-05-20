package com.example.appcliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.appcliente.databinding.FragmentPortadaEleccionBinding

/**
 * A simple [Fragment] subclass.
 */
class PortadaEleccionFragment : Fragment() {

    private var _binding: FragmentPortadaEleccionBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPortadaEleccionBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.buttonUsuario.setOnClickListener { startClicked() }
        binding.buttonEmpleado.setOnClickListener { startClicked2() }
        return view
    }

    private fun startClicked2() {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
        findNavController().navigate(R.id.action_portadaEleccionFragment_to_portadaEmpleadoFragment, null, options)
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
        findNavController().navigate(R.id.action_portadaEleccionFragment_to_portadaUsuarioFragment, null, options)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
