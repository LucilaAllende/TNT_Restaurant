package com.example.restaurante

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.restaurante.databinding.FragmentInicioUsuarioBinding

/**
 * A simple [Fragment] subclass.
 */
class InicioUsuarioFragment : Fragment() {

    private var _binding: FragmentInicioUsuarioBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInicioUsuarioBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.buttonMenuDia.setOnClickListener { startClicked() }
        binding.buttonMenuSemanal.setOnClickListener { startClicked2() }
        binding.buttonMennuMerienda.setOnClickListener { startClicked3() }
        binding.buttonHistorial.setOnClickListener { startClicked4() }
        return view
    }

    private fun startClicked4() {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
        findNavController().navigate(R.id.action_inicioUsuarioFragment_to_historialPedidosFragment, null, options)
    }

    private fun startClicked3() {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
        findNavController().navigate(R.id.action_inicioUsuarioFragment_to_meriendaDesayunoFragment, null, options)
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
        findNavController().navigate(R.id.action_inicioUsuarioFragment_to_menuSemanalFragment, null, options)
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
        findNavController().navigate(R.id.action_inicioUsuarioFragment_to_menuDiaFragment, null, options)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
