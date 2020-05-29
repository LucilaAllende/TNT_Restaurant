package com.example.appcliente


import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.appcliente.databinding.FragmentPortadaBinding
import com.example.appcliente.databinding.FragmentSingingBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase



class FragmentSinging : Fragment() {
    private var _binding: FragmentSingingBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressBar : ProgressBar
    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSingingBinding.inflate(inflater, container, false)
        val view = binding.root

        var appBar: AppBarLayout? = activity?.findViewById<View>(R.id.appBar) as? AppBarLayout
        appBar!!.setExpanded(false,false)
        appBar!!.visibility = View.GONE

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        dbReference = database.reference.child("User")

        binding.btnSinging.setOnClickListener { createNewAccount() }
        return view
    }


    private fun createNewAccount() {
        var nombre = binding.txtNombre.text.toString()
        var apellido = binding.txtApellido.text.toString()
        var password = binding.txtPassword.text.toString()
        var email = binding.txtEmail.text.toString()
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        dbReference = database.reference.child("Usuarios")

        if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(apellido) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(email)){
            progressBar = binding.progressBarSinging
            progressBar.visibility = View.VISIBLE
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{
                        task ->
                    if (task.isComplete){
                        val user: FirebaseUser? = auth.currentUser
                        verifyEmail(user)
                        var userBD = dbReference.child(user!!.uid)
                        userBD.child("nombre").setValue(nombre)
                        userBD.child("apellido").setValue(apellido)
                        userBD.child("email").setValue(email)
                        action()
                    }
                }

        }
    }

    private fun action(){
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
        FirebaseAuth.getInstance().signOut()
        //findNavController().navigate(R.id.fragmentLogin,null, options)
    }

    private fun verifyEmail(user: FirebaseUser?){
        user?.sendEmailVerification()
            ?.addOnCompleteListener{
                    task ->
                if (task.isComplete){
                    Toast.makeText(context, "Email enviado", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(context, "Error al enviar mail", Toast.LENGTH_LONG).show()
                }
            }
    }
}
