package com.example.appcliente.ui.home.menusemanal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.appcliente.R
import com.google.android.material.tabs.TabLayout

//import androidx.navigation.fragment.navArgs

/**
 * A simple [Fragment] subclass.
 */
class DetallesViandaFragment : Fragment() {

    //val args: DetallesViandaFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista: View = inflater.inflate(R.layout.fragment_detalles_vianda, container, false)

        val textIngredientes = vista.findViewById<View>(R.id.txtIngredientesVianda) as TextView
        val textNombre = vista.findViewById<View>(R.id.txtNombreVianda) as TextView
        //val imagenDetalle = vista.findViewById<View>(R.id.imagenIngredientesVianda) as ImageView

        textNombre.text = arguments?.getString("nombre")
        textIngredientes.text = arguments?.getString("ingredientes")

        val tabsi = activity?.findViewById<TabLayout>(R.id.tabs)
        tabsi!!.visibility = View.GONE

        return vista
    }

}
