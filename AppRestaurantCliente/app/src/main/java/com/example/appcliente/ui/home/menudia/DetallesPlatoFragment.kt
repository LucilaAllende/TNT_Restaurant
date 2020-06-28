package com.example.appcliente.ui.home.menudia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.appcliente.R
//import androidx.navigation.fragment.navArgs


/**
 * A simple [Fragment] subclass.
 */
class DetallesPlatoFragment : Fragment() {

    //val args: DetallesPlatoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val vista: View =
            inflater.inflate(R.layout.fragment_detalles_plato, container, false)

        val textIngredientes = vista.findViewById<View>(R.id.txtIngredientesAlmuerzo) as TextView
        val textNombre = vista.findViewById<View>(R.id.txtNombreAlmuerzo) as TextView
        //val imagenDetalle = vista.findViewById<View>(R.id.imagenIngredientesAlmmuerzo) as ImageView

        textNombre.text = arguments?.getString("nombre")
        textIngredientes.text = arguments?.getString("ingredientes")

        return vista
    }


}
