package com.example.appcliente.ui.pedido

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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
class UbicacionFragment : Fragment() {

    var vista: View? = null

    private var btnConfirmar: Button? = null
    private var listaPedido: List<Pedido>? = null


    private var txtLocalidad: EditText? = null
    private var txtCalle: EditText? = null
    private var txtNumCalle: EditText? = null
    private var txtPiso: EditText? = null
    private var txtDpto: EditText? = null
    private var txtCalle1: EditText? = null
    private var txtCalle2: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_ubicacion, container, false)

        val pedidos =  arguments?.get("pedidos")
        listaPedido = (pedidos as? ArrayList<*>)?.filterIsInstance<Pedido>()


        txtLocalidad = vista?.findViewById(R.id.editCiudad)
        txtCalle = vista?.findViewById(R.id.editCalle)
        txtNumCalle = vista?.findViewById(R.id.editNumCalle)
        txtPiso = vista?.findViewById(R.id.editPiso)
        txtDpto = vista?.findViewById(R.id.editDpto)
        txtCalle1 = vista?.findViewById(R.id.editCalle1)
        txtCalle2 = vista?.findViewById(R.id.editCalle2)


        btnConfirmar = vista?.findViewById(R.id.btnConfirmarUbicacion)
        btnConfirmar?.setOnClickListener { asignarUbicacionAPedido() }

        return vista
    }

    private fun asignarUbicacionAPedido() {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        val localidad = txtLocalidad?.text.toString()
        val calle = txtCalle?.text.toString()
        val numCalle = txtNumCalle?.text.toString()
        val numPiso = txtPiso?.text.toString()
        val numDpto = txtDpto?.text.toString()
        val calle1 = txtCalle1?.text.toString()
        val calle2 = txtCalle2?.text.toString()

        if (!TextUtils.isEmpty(localidad) &&
            !TextUtils.isEmpty(calle) &&
            !TextUtils.isEmpty(numCalle) &&
            !TextUtils.isEmpty(calle1) &&
            !TextUtils.isEmpty(calle2) ||
            !TextUtils.isEmpty(numPiso) ||
            !TextUtils.isEmpty(numDpto) ) {
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
                    "direccionEnvio" to "Localidad$localidad,Calle$calle,NumCalle$numCalle,NumPiso$numPiso,NumDpto$numDpto,Calle1$calle1,Calle2$calle2",
                    "estado" to plato.estado, // [en preparación | en camino | entregado]
                    "tipo" to plato.tipo
                )

                val historialReference: DatabaseReference =
                    database.reference.child("Pedido").push()

                historialReference.setValue(pedidoHistorial)

                database.reference.child("PedidoEnCurso/" + plato.id).removeValue()
            }

            Toast.makeText(context, "Su pedido llegara pronto :)", Toast.LENGTH_LONG).show()

            findNavController().navigate(R.id.nav_home, null, options)
        }
        else{
            Toast.makeText(context, "Debe ingresar una ubicacion", Toast.LENGTH_LONG).show()
        }
    }

}
