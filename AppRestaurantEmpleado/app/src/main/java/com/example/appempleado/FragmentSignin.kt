package com.example.appempleado

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
import com.example.appempleado.databinding.FragmentSigningBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FragmentSignin : Fragment() {
    private var _binding: FragmentSigningBinding? = null
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
        _binding = FragmentSigningBinding.inflate(inflater, container, false)
        val view = binding.root
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
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
                        if (user!=null){
                            verifyEmail(user)
                            var userBD = dbReference.child(user!!.uid)
                            userBD.child("nombre").setValue(nombre)
                            userBD.child("apellido").setValue(apellido)
                            userBD.child("email").setValue(email)
                            action()
                        }
                        else{ //TODO: mejorar esto adentro de un try catch!
                            progressBar.visibility = View.INVISIBLE
                            Toast.makeText(context, task.exception.toString(), Toast.LENGTH_LONG).show()
                        }

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
        findNavController().navigate(R.id.fragmentLogin,null, options)
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
