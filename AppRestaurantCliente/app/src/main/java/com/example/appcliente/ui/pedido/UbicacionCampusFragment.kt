package com.example.appcliente.ui.pedido

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions

import com.example.appcliente.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class UbicacionCampusFragment : Fragment() {

    private var vista: View? = null
    private var btnConfirmar: Button? = null
    private var listaPedido: List<Pedido>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_ubicacion_campus, container, false)

        val pedidos =  arguments?.get("pedidos")
        listaPedido = (pedidos as? ArrayList<*>)?.filterIsInstance<Pedido>()

        btnConfirmar = vista?.findViewById(R.id.btnConfirmarUbicacionCampus)
        btnConfirmar?.setOnClickListener { asignarUbicacionCampusAPedido() }

        return vista
    }

    private fun asignarUbicacionCampusAPedido() {

        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        for (plato in this.listaPedido!!) {

            val database = FirebaseDatabase.getInstance()
            val user = FirebaseAuth.getInstance().currentUser
            val date = Calendar.getInstance().time
            val formatter = java.text.SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
            val formatedDate = formatter.format(date)
            val pedidoHistorial = mapOf(
                "timestamp" to formatedDate.toString(),
                "clienteId" to user?.uid,
                "platoId" to plato.id,
                "nombrePlato" to plato.nombrePlato,
                "precioPlato" to plato.precioPlato,
                "direccionEnvio" to "Eviar a ubicacion del usuario",
                "estado" to plato.estado, // [en preparación | en camino | entregado]
                "tipo" to plato.tipo
            )

            val historialReference: DatabaseReference = database.reference.child("Historial").push()

            historialReference.setValue(pedidoHistorial)

            database.reference.child("Pedido/" + plato.id).removeValue()
        }

        Toast.makeText(context, "Su pedido llegara pronto :)", Toast.LENGTH_LONG).show()

        findNavController().navigate(R.id.nav_home, null, options)
    }


}
