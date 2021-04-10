package com.example.appcliente.ui.pedido

import android.os.Bundle
import android.os.Parcelable
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
@Suppress("UNCHECKED_CAST")
class SolicitarDireccionFragment : Fragment() {

    private var vista: View? = null

    private var btnDomicilio: Button? = null
    private var btnUbicacion: Button? = null
    private var btnRetiro: Button? = null
    private var btnCampus: Button? = null

    private var pedidos: Any? = null

    private var listaPedido: List<Pedido>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_solicitar_direccion, container, false)

        btnDomicilio = vista?.findViewById(R.id.btnEnviarDomicilio)
        btnUbicacion = vista?.findViewById(R.id.btnIngresarUbicacion)
        btnRetiro = vista?.findViewById(R.id.btnRetirarBuffet)
        btnCampus = vista?.findViewById(R.id.btnEnvioCampus)

        pedidos =  arguments?.get("pedidos")

        listaPedido = (pedidos as? ArrayList<*>)?.filterIsInstance<Pedido>()

        btnDomicilio?.setOnClickListener { mostrarDomicilioUsuario() }
        btnUbicacion?.setOnClickListener { tomarUbicacionUsuario() }
        btnRetiro?.setOnClickListener { asignarRetiroEnBuffetAPedido() }
        btnCampus?.setOnClickListener { tomarUbicacionCampusUsuario() }

        return vista
    }



    private fun asignarRetiroEnBuffetAPedido() {

        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        for (pedido in this.listaPedido!!){

            val database = FirebaseDatabase.getInstance()
            val user = FirebaseAuth.getInstance().currentUser
            val date = Calendar.getInstance().time
            val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
            val formatedDate = formatter.format(date)
            val pedidoHistorial = mapOf("timestamp" to formatedDate.toString(),
                "clienteId" to user?.uid,
                "platoId" to pedido.platoId,
                "nombrePlato" to pedido.nombrePlato,
                "precioPlato" to pedido.precioPlato,
                "direccionEnvio" to "Retira en Buffet",
                "estado" to pedido.estado, // [en preparación | en camino | entregado]
                "tipo" to pedido.tipo)
            val historialReference: DatabaseReference = database.reference.child("Pedido").push()

            historialReference.setValue(pedidoHistorial)

            database.reference.child("PedidoEnCurso/"+pedido.id).removeValue()
        }

        Toast.makeText(context, "Pedido realizado",Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.nav_home, null, options)
    }

    private fun mostrarDomicilioUsuario() {
        //Aca hay que tomar la direccion del usuario y mostrarla en el siguiente fragmento
        val bundle = Bundle()
        bundle.putParcelableArrayList("pedidos", pedidos as java.util.ArrayList<out Parcelable>?)
        findNavController().navigate(R.id.nav_direccion_usuario, bundle, null)
    }

    private fun tomarUbicacionUsuario() {
        //Aca hay que tomar la ubicacion del usuario
        val bundle = Bundle()
        bundle.putParcelableArrayList("pedidos", pedidos as java.util.ArrayList<out Parcelable>?)
        findNavController().navigate(R.id.nav_ubicacion_usuario, bundle, null)
    }


    private fun tomarUbicacionCampusUsuario() {
        val bundle = Bundle()
        bundle.putParcelableArrayList("pedidos", pedidos as java.util.ArrayList<out Parcelable>?)
        findNavController().navigate(R.id.nav_ubicacion_campus_usuario, bundle, null)
    }

}
