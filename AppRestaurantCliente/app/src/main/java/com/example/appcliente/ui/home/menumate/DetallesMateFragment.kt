package com.example.appcliente.ui.home.menumate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.appcliente.R

//import androidx.navigation.fragment.navArgs



/**
 * A simple [Fragment] subclass.
 */
class DetallesMateFragment : Fragment() {

    //val args: DetallesMateFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista: View = inflater.inflate(R.layout.fragment_detalles_mate, container, false)

        val textNombre = vista.findViewById<View>(R.id.txtNombreDesayuno) as TextView
        val textIngredientes = vista.findViewById<View>(R.id.txtIngredientesDesayunoMerienda) as TextView
        //val imagenDetalle = vista.findViewById<View>(R.id.imagenIngredientesDesayunoMerienda) as ImageView

        textNombre.text = arguments?.getString("nombre")
        textIngredientes.text = arguments?.getString("ingredientes")

        return vista
    }

}
