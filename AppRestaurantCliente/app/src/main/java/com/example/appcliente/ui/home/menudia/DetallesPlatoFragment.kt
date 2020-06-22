package com.example.appcliente.ui.home.menudia

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.appcliente.R


/**
 * A simple [Fragment] subclass.
 */
class DetallesPlatoFragment : Fragment() {

    var textDescripcion: TextView? = null
    var imagenDetalle: ImageView? = null
    val args: DetallesPlatoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val vista: View =
            inflater.inflate(R.layout.fragment_detalles_plato, container, false)

        val textDescripcion = vista.findViewById<View>(R.id.descripcionId) as TextView
        val imagenDetalle = vista.findViewById<View>(R.id.imagen) as ImageView
        textDescripcion.text = arguments?.getString("name")

        /*val objetoPersonaje = arguments
        var menuDia: PlatoDia? = null
        if (objetoPersonaje != null) {
            menuDia = objetoPersonaje.getSerializable("objeto") as PlatoDia
            asignarInformacion(menuDia)
        }*/

        return vista
    }


    fun asignarInformacion(menu: PlatoDia) {
        //imagenDetalle?.setImageResource(menu.imagen)
        textDescripcion?.setText(menu.ingredientes)
    }

}
