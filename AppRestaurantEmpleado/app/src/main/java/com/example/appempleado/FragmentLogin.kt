package com.example.appempleado

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.appempleado.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class FragmentLogin : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.btnLogin.setOnClickListener { login() }
        return view
    }

    private fun login() {
        auth = FirebaseAuth.getInstance()
        var usuario = binding.txtUsuario.text.toString()
        var password = binding.txtPassword.text.toString()
        if (!TextUtils.isEmpty(usuario) && !TextUtils.isEmpty(password)){
            binding.progressBarLogin.visibility = View.VISIBLE
            auth.signInWithEmailAndPassword(usuario,password)
                .addOnCompleteListener{
                        task ->
                    if(task.isSuccessful){
                        findNavController().navigate(R.id.action_fragmentLogin_to_fragmentHome,null, options)
                    }
                    else{
                        binding.progressBarLogin.visibility = View.INVISIBLE
                        Toast.makeText(context, task.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }

        }
    }
}
