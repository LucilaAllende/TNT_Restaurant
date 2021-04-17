package com.example.appempleado

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.appempleado.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth


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
        binding.btnEliminarPlatos.setOnClickListener{ irEliminarPlatos() }
        setHasOptionsMenu(true)
        return view
    }

    private fun irEliminarPlatos() {
        findNavController().navigate(R.id.action_fragmentHome_to_eliminarPlatosFragment2,null, options)
    }

    private fun irVerPedidos() {
        findNavController().navigate(R.id.action_fragmentHome_to_fragmentListaPedidos,null, options)

    }

    private fun irCargarMenues() {
        findNavController().navigate(R.id.action_fragmentHome_to_fragmentCargarMenu,null, options)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        FirebaseAuth.getInstance().signOut()
        findNavController().navigate(R.id.action_fragmentHome_to_fragmentPortada,null)
        return true
    }
}
