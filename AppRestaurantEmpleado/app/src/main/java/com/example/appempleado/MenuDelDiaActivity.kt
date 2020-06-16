package com.example.appempleado

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class MenuDelDiaActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener{
    val REQUEST_CODE = 123
    private var imageUrl: Uri? = null
    private lateinit var btnImagenPlato: View
    private lateinit var btnAltaPlato: View
    private lateinit var imgPlato: ImageView
    private lateinit var txtNombre: TextView
    private lateinit var txtDescripcion: TextView
    private lateinit var txtPrecio: TextView
    private lateinit var pbAltaPlato: ProgressBar
    private var tipoPlato: String = "vegano" /*Esto indica vegano, carnico o vegetariano*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_del_dia)
        setTitle("Menu del Dia")
        btnImagenPlato =  this.findViewById<View>(R.id.btnImagenPlato)
        btnAltaPlato =  this.findViewById<View>(R.id.btnAltaPlato)
        imgPlato = this.findViewById(R.id.imgPlato)
        txtNombre = this.findViewById(R.id.txtNombre)
        txtDescripcion  = this.findViewById(R.id.txtDescripcion)
        txtPrecio = this.findViewById(R.id.txtPrecio)
        pbAltaPlato = this.findViewById(R.id.pbAltaPlato)
        btnImagenPlato.setOnClickListener { altaImagenPlato() }
        btnAltaPlato.setOnClickListener{ cargarPlato() }

        val spinner: Spinner = findViewById(R.id.spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.tipo_plato_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            //Obtengo una URI asociada a la imagen cargada desde la galeria del telefono y la guardo para desp cargar en firebase
            imageUrl = data?.data
            imgPlato.setImageURI(data?.data)
        }
    }

    private fun altaImagenPlato(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun cargarPlato(){
        if (imageUrl != null) { /*TODO: checkear los demas atributos tamnbien, no solo la imagen! */
            pbAltaPlato.visibility = View.VISIBLE
            val databaseStorage = FirebaseStorage.getInstance()
            val imgReference = databaseStorage.reference.child("images/${imageUrl?.lastPathSegment}")
            var uploadTask = imgReference.putFile(imageUrl!!)
            uploadTask.addOnFailureListener {
                pbAltaPlato.visibility = View.INVISIBLE
                Toast.makeText(this, "Error al cargar imagen", Toast.LENGTH_LONG).show()
            }.addOnSuccessListener {
                    taskSnapshot ->
                    val downloadUrl: Task<Uri> = imgReference.downloadUrl
                    downloadUrl.addOnSuccessListener { uri ->
                        val database = FirebaseDatabase.getInstance()
                        val plato = mapOf("nombre" to txtNombre.text.toString(),
                                            "ingredientes" to txtDescripcion.text.toString(),
                                            "imagenUrl" to uri.toString(),
                                            "precio" to txtPrecio.text.toString(),
                                            "categoria" to tipoPlato)
                        val platoReference: DatabaseReference = database.reference.child("platoDia").push()
                        platoReference.setValue(plato)
                        pbAltaPlato.visibility = View.INVISIBLE
                        Toast.makeText(this, "Plato Cargado!", Toast.LENGTH_LONG).show()
                        this.finish()
                    }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        tipoPlato = "Vegano"
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            tipoPlato = parent.getItemAtPosition(position).toString()
        }
    }

}
