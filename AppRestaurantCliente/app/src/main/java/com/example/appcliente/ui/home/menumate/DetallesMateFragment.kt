package com.example.appcliente.ui.home.menumate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.navArgs

import com.example.appcliente.R

/**
 * A simple [Fragment] subclass.
 */
class DetallesMateFragment : Fragment() {

    val args: DetallesMateFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista: View = inflater.inflate(R.layout.fragment_detalles_mate, container, false)

        val textDescripcion = vista.findViewById<View>(R.id.descripcionId) as TextView
        val imagenDetalle = vista.findViewById<View>(R.id.imagen) as ImageView
        textDescripcion.text = arguments?.getString("name")

        return vista
    }

}
