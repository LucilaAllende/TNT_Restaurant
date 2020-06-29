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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class SolicitarDireccionFragment : Fragment() {

    var vista: View? = null
    var btnDomicilio: Button? = null
    var btnUbicacion: Button? = null
    var btnRetiro: Button? = null

    var listaPedido: List<Pedido>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_solicitar_direccion, container, false)

        btnDomicilio = vista?.findViewById(R.id.btnEnviarDomicilio)
        btnUbicacion = vista?.findViewById(R.id.btnIngresarUbicacion)
        btnRetiro = vista?.findViewById(R.id.btnRetirarBuffet)

        val pedidos =  arguments?.get("pedidos")

        listaPedido = (pedidos as? ArrayList<*>)?.filterIsInstance<Pedido>()

        println("................................................****")
        println(listaPedido)

        btnDomicilio?.setOnClickListener { mostrarDomicilioUsuario() }
        btnUbicacion?.setOnClickListener { tomarUbicacionIngresada() }
        btnRetiro?.setOnClickListener { asignarRetiroAPedido() }

        return vista
    }

    private fun asignarRetiroAPedido() {
        //TODO Aca hay que asignarle al pedido, la direccion en buffet
        //TODO Borrar pedido o sacar pedido del Pedido, solo tiene que quedar en el historial.
        println("***************************")
        println(listaPedido)

        for (plato in this.listaPedido!!){
            println("***************************")
            println(plato)


            val database = FirebaseDatabase.getInstance()
            val user = FirebaseAuth.getInstance().currentUser
            val date = Calendar.getInstance().time
            val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
            val formatedDate = formatter.format(date)
            val pedidoHistorial = mapOf("timestamp" to formatedDate.toString(),
                "clienteId" to user?.uid,
                "platoId" to plato.id,
                "nombrePlato" to plato.nombrePlato,
                "precioPlato" to plato.precioPlato,
                "direccionEnvio" to "Retira en Buffet",
                "estado" to plato.estado, // [en preparación | en camino | entregado]
                "tipo" to plato.tipo)
            val historialReference: DatabaseReference = database.reference.child("Historial").push()


            historialReference.setValue(pedidoHistorial)

            database.reference.child("Pedido/"+plato.id).removeValue()
        }

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
