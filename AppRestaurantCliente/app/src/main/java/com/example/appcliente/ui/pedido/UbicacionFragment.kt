package com.example.appcliente.ui.pedido

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.appcliente.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class UbicacionFragment : Fragment() {

    var vista: View? = null

    private var btnConfirmar: Button? = null
    private var listaPedido: List<Pedido>? = null

    var longitude = ""
    var latitude = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_ubicacion, container, false)

        val pedidos =  arguments?.get("pedidos")
        listaPedido = (pedidos as? ArrayList<*>)?.filterIsInstance<Pedido>()

        asignarUbicacionAPedido1()
        btnConfirmar = vista?.findViewById(R.id.btnConfirmarUbicacion)
        btnConfirmar?.setOnClickListener { asignarUbicacionAPedido(latitude,longitude) }


        return vista
    }

    private fun asignarUbicacionAPedido1() {

        FirebaseDatabase.getInstance().reference.child("ltlg")
            .orderByChild("clienteId")
            .equalTo(FirebaseAuth.getInstance().currentUser?.uid)
            .addChildEventListener(object: ChildEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    println("cancelado")
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    println("movido")
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    println("cambiANDOOOO!!!")
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    if(p0.childrenCount > 0){
                        val ltlg = p0.getValue(Ltlg::class.java)!!
                        ltlg.id = p0.key.toString()

                        latitude = ltlg.latitudd
                        longitude = ltlg.longitudd

                    }
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    println("Eliminando!!!")

                }
            })

    }

    private fun asignarUbicacionAPedido(lati: String,longi: String) {

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
                "platoId" to plato.platoId,
                "nombrePlato" to plato.nombrePlato,
                "precioPlato" to plato.precioPlato,
                "direccionEnvio" to "Latitud $lati Longitud $longi",
                "estado" to plato.estado, // [en preparación | en camino | entregado]
                "tipo" to plato.tipo
            )

            val historialReference: DatabaseReference =
                database.reference.child("Pedido").push()

            historialReference.setValue(pedidoHistorial)

            database.reference.child("PedidoEnCurso/" + plato.id).removeValue()
        }

        val database = FirebaseDatabase.getInstance()
        database.reference.child("ltlg").removeValue()

        Toast.makeText(context, "Su pedido llegara pronto :)", Toast.LENGTH_LONG).show()

        findNavController().navigate(R.id.nav_home, null, options)
    }
}

@Parcelize
data class Ltlg(
    var id: String ="",
    var latitudd: String = "",
    var clienteId: String = "",
    var longitudd:String = "",
    var timestamp:String = ""
): Parcelable
