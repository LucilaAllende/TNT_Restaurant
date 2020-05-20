package com.example.restaurante

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.restaurante.databinding.FragmentPortadaUsuarioBinding

/**
 * A simple [Fragment] subclass.
 */
class portadaUsuarioFragment : Fragment() {

    private var _binding: FragmentPortadaUsuarioBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPortadaUsuarioBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.buttonIniciarSesion.setOnClickListener { startClicked() }
        binding.buttonCrearCuenta.setOnClickListener { startClicked2() }
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
        findNavController().navigate(R.id.action_portadaUsuarioFragment_to_crearCuentaFragment, null, options)
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
        findNavController().navigate(R.id.action_portadaUsuarioFragment_to_inicioUsuarioFragment, null, options)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
