package com.example.restaurante

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.restaurante.databinding.FragmentMenuSemanalBinding

/**
 * A simple [Fragment] subclass.
 */
class MenuSemanalFragment : Fragment() {

    private var _binding: FragmentMenuSemanalBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuSemanalBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.button5.setOnClickListener { startClicked() }
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
        findNavController().navigate(R.id.action_menuSemanalFragment_to_realizarPedidoFragment, null, options)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
