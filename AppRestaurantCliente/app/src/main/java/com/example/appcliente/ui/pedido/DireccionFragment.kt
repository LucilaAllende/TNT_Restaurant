package com.example.appcliente.ui.pedido

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.appcliente.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * A simple [Fragment] subclass.
 */
class DireccionFragment : Fragment() {

    var vista: View? = null
    var txtDireccion: TextView? = null
    lateinit var calleUsuario: String

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        vista =  inflater.inflate(R.layout.fragment_direccion, container, false)
        txtDireccion = vista?.findViewById(R.id.txtDireccionUsuario)


        val usuario_id = FirebaseAuth.getInstance().currentUser?.uid
        FirebaseDatabase.getInstance().reference.child("Usuarios/"+usuario_id)
            .addValueEventListener(object :
                ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    println("error trayendo datos de la base")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val usuarioDB = p0.getValue(Usuario::class.java)
                    //la variable usuarioDB ya tiene los datos del usuario
                    println("..........asdasdasd.........")
                    println(usuarioDB)
                    println("...................")
                    if (usuarioDB != null) {
                        calleUsuario = usuarioDB.calle
                        txtDireccion?.text = calleUsuario
                    }
                }
            })


        return vista
    }



    data class Usuario(
        var apellido: String ="",
        var email: String = "",
        var nombre: String ="",
        var localidad: String = "",
        var calle: String = ""
    )

}
