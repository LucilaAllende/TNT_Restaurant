package com.example.appcliente.ui.pedido

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions

import com.example.appcliente.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class DireccionFragment : Fragment() {

    var vista: View? = null
    var txtCalle: TextView? = null
    var txtNumeroCalle: TextView? = null
    var txtNumeroDpto: TextView? = null
    var txtNumeroPiso: TextView? = null
    var txtLocalidad: TextView? = null

    private var btnConfirmar: Button? = null

    private var listaPedido: List<Pedido>? = null


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        vista =  inflater.inflate(R.layout.fragment_direccion, container, false)
        txtCalle = vista?.findViewById(R.id.txtCalleUsuario)
        txtNumeroCalle = vista?.findViewById(R.id.txtNumeroCalleUsuario)
        txtLocalidad = vista?.findViewById(R.id.txtLocalidadUsuario)
        txtNumeroPiso = vista?.findViewById(R.id.txtNumeroPisoUsuario)
        txtNumeroDpto = vista?.findViewById(R.id.txtNumeroDptoUsuario)

        btnConfirmar = vista?.findViewById(R.id.btnConfirmarDireccion)

        val pedidos =  arguments?.get("pedidos")
        listaPedido = (pedidos as? ArrayList<*>)?.filterIsInstance<Pedido>()

        mostrarDireccionUsuario()
        btnConfirmar?.setOnClickListener { asignarDireccionAPedido() }

        return vista
    }

    private fun asignarDireccionAPedido() {

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
            val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
            val formatedDate = formatter.format(date)
            val pedidoHistorial = mapOf(
                "timestamp" to formatedDate.toString(),
                "clienteId" to user?.uid,
                "platoId" to plato.id,
                "nombrePlato" to plato.nombrePlato,
                "precioPlato" to plato.precioPlato,
                "direccionEnvio" to "Eviar a direccion del usuario",
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

    private fun mostrarDireccionUsuario() {
        val usuario_id = FirebaseAuth.getInstance().currentUser?.uid
        FirebaseDatabase.getInstance().reference.child("Usuarios/"+usuario_id)
            .addValueEventListener(object :
                ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    println("error trayendo datos de la base")
                }

                @SuppressLint("SetTextI18n")
                override fun onDataChange(p0: DataSnapshot) {
                    val usuarioDB = p0.getValue(Usuario::class.java)
                    //la variable usuarioDB ya tiene los datos del usuario
                    if (usuarioDB != null) {
                        txtCalle?.text = "Calle: "+usuarioDB.calle_vivienda
                        txtNumeroCalle?.text = "N° Calle: "+usuarioDB.numero_calle
                        txtLocalidad?.text = "Localidad: "+usuarioDB.ciudad
                        txtNumeroPiso?.text = "N° piso: " +usuarioDB.numero_piso
                        txtNumeroDpto?.text = "N° dpto: "+usuarioDB.numero_departamento
                    }
                }
            })
    }


    data class Usuario(
        var apellido: String ="",
        var email: String = "",
        var nombre: String ="",
        var calle_vivienda: String="" ,
        var numero_calle : String="",
        var ciudad: String="",
        var numero_piso: String="",
        var numero_departamento:String=""
    )

}
