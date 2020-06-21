package com.example.appcliente.ui.pedido

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController

import com.example.appcliente.R

/**
 * A simple [Fragment] subclass.
 */
class SolicitarDireccionFragment : Fragment() {

    var vista: View? = null
    var btnDomicilio: Button? = null
    var btnUbicacion: Button? = null
    var btnRetiro: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_solicitar_direccion, container, false)

        btnDomicilio = vista?.findViewById(R.id.btnEnviarDomicilio)
        btnUbicacion = vista?.findViewById(R.id.btnIngresarUbicacion)
        btnRetiro = vista?.findViewById(R.id.btnRetirarBuffet)

        btnDomicilio?.setOnClickListener { mostrarDomicilioUsuario() }
        btnUbicacion?.setOnClickListener { tomarUbicacionIngresada() }
        btnRetiro?.setOnClickListener { asignarRetiroAPedido() }

        return vista
    }

    private fun asignarRetiroAPedido() {
        //TODO Aca hay que asignarle al pedido, la direccion en buffet
        //
        //TODO Borrar pedido o sacar pedido del Pedido, solo tiene que quedar en el historial.
        Toast.makeText(context, "Ya puede retirar su pedido",Toast.LENGTH_LONG).show()
    }

    private fun mostrarDomicilioUsuario() {
        //Aca hay que tomar la direccion del usuario y mostrarla en el siguiente fragmento
        findNavController().navigate(R.id.nav_direccion_usuario, null, null)
    }

    private fun tomarUbicacionIngresada() {
        //Aca hay que tomar la ubicacion del usuario
        findNavController().navigate(R.id.nav_ubicacion_usuario, null, null)
    }

}
