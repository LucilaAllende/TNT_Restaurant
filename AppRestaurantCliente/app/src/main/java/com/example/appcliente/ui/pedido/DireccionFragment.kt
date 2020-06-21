package com.example.appcliente.ui.pedido

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.appcliente.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

/**
 * A simple [Fragment] subclass.
 */
class DireccionFragment : Fragment() {

    var vista: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        vista =  inflater.inflate(R.layout.fragment_direccion, container, false)

       /* val usuario_id = FirebaseAuth.getInstance().currentUser?.uid

        FirebaseDatabase.getInstance().reference.child("Usuarios/"+usuario_id).addChildEventListener(object :
            ChildEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                println("error trayendo datos de la base")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val usuarioDB = p0.getValue(Usuario::class.java)
                //la variable usuarioDB ya tiene los datos del usuario
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })*/


        return vista
    }

    data class Usuario(
        var apellido: String ="",
        var email: String = "",
        var nombre: String =""
    )

}
