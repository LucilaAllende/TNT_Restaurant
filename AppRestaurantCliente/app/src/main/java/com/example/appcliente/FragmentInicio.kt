package com.example.appcliente


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import com.example.appcliente.databinding.FragmentInicioBinding
import com.example.appcliente.FragmentInicioArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
//import com.google.firebase.database.R
import kotlinx.android.synthetic.main.fragment_inicio.view.*

@IgnoreExtraProperties
data class Usuario(
    var nombre: String? = "",
    var apellido: String = "",
    var email: String = ""
)

class FragmentInicio : Fragment() {

    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: FirebaseDatabase
    private lateinit var dbReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.btnLogout.setOnClickListener { logout() }
        val safeArgs: FragmentInicioArgs by navArgs()


        dbReference = FirebaseDatabase.getInstance().getReference()
        val user = FirebaseAuth.getInstance().currentUser
        dbReference.child("Usuarios").child(user!!.uid.toString()).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println("hubo un errro")
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val usuario = dataSnapshot.getValue(Usuario::class.java)
                view.textViewUser.text = "Bienvenido "+ usuario?.nombre.toString()
            }
        })
        return view
    }


    private fun logout(){
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
        FirebaseAuth.getInstance().signOut()
        findNavController().navigate(R.id.fragmentPortada,null,options)
    }
}
