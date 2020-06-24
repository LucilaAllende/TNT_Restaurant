package com.example.appcliente.ui.home.menusemanal

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
class DetallesViandaFragment : Fragment() {

    val args: DetallesViandaFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista: View = inflater.inflate(R.layout.fragment_detalles_vianda, container, false)

        val textDescripcion = vista.findViewById<View>(R.id.txtIngredientesVianda) as TextView
        val imagenDetalle = vista.findViewById<View>(R.id.imagenIngredientesVianda) as ImageView
        textDescripcion.text = arguments?.getString("name")

        return vista
    }

}
