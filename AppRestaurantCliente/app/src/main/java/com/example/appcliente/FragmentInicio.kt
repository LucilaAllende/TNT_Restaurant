package com.example.appcliente

import android.app.ActionBar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.appcliente.databinding.FragmentInicioBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.app_bar_main.*


class FragmentInicio : Fragment() {
    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!
    val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        val view = binding.root

        //ESTE CODIGO UTILIZO PARA OCULTAR LA BARRA
        var appBar: AppBarLayout? = activity?.findViewById<View>(R.id.appBar) as? AppBarLayout
        appBar!!.setExpanded(false,false)
        appBar!!.visibility = View.GONE

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            findNavController().navigate(R.id.action_fragmentInicio2_to_nav_home,null,options)
        } else {
            findNavController().navigate(R.id.action_fragmentInicio2_to_fragmentPortada,null,options)
        }
    }
}

/*

        //FirebaseAuth.getInstance().signOut()


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

        data class Usuario(
    var nombre: String? = "",
    var apellido: String = "",
    var email: String = ""
)
 */