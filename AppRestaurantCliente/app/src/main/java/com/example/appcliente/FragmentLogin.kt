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
import com.example.appcliente.databinding.FragmentLoginBinding
import com.example.appcliente.databinding.FragmentPortadaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class FragmentLogin : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressBar : ProgressBar
    private lateinit var database: FirebaseDatabase
    private lateinit var dbReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.btnLogin.setOnClickListener { login() }
        binding.btnRegistrarse.setOnClickListener{registrarse()}
        return view
    }

    private fun login() {
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        dbReference = database.reference.child(("User"))

        var usuario = binding.txtUsuario.text.toString()
        var password = binding.txtPassword.text.toString()

        if (!TextUtils.isEmpty(usuario) && !TextUtils.isEmpty(password)){
            progressBar = binding.progressBarLogin
            progressBar.visibility = View.VISIBLE
            auth.signInWithEmailAndPassword(usuario,password)
                .addOnCompleteListener{
                        task ->
                    if(task.isSuccessful){
                        action()
                    }
                    else{
                        progressBar.visibility = View.INVISIBLE
                        Toast.makeText(context, "Error credenciales incorrenctas", Toast.LENGTH_LONG).show()
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
        findNavController().navigate(R.id.fragmentInicio,null, options)
    }

    private fun registrarse(){
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
        findNavController().navigate(R.id.fragmentSinging,null,options)
    }
}
