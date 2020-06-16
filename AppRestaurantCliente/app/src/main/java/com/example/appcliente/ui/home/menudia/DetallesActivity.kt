package com.example.appcliente.ui.home.menudia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.appcliente.R

class DetallesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles)

        val extras: Bundle? = intent?.extras
        val txtDetalle: TextView? = findViewById(R.id.textDetalleNombre)
        val txtIngredientes: TextView? = findViewById(R.id.textIngredientes)
        val nombre = extras?.getString("name")
        val ingredientes = extras?.getString("ingredientes")

        if (extras != null) {
            txtDetalle?.text= nombre
            txtIngredientes?.text=ingredientes
        }


    }
}
