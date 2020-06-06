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

class MenuMeriendaActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    val REQUEST_CODE = 123
    private var imageUrl: Uri? = null
    private lateinit var btnImagenPlato: View
    private lateinit var btnAltaPlato: View
    private lateinit var imgPlato: ImageView
    private lateinit var txtNombre: TextView
    private lateinit var txtDescripcion: TextView
    private lateinit var txtPrecio: TextView
    private lateinit var pbAltaPlato: ProgressBar
    private var sabor: String = "salado" /*Esto indica vegano, carnico o vegetariano*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_merienda)
        setTitle("Desayuno y Merienda")
        btnImagenPlato =  this.findViewById<View>(R.id.btnImagenPlatoMerienda)
        btnAltaPlato =  this.findViewById<View>(R.id.btnAltaPlatoMerienda)
        imgPlato = this.findViewById(R.id.imgPlatoMerienda)
        txtNombre = this.findViewById(R.id.txtNombreMerienda)
        txtDescripcion  = this.findViewById(R.id.txtDescripcionMerienda)
        txtPrecio = this.findViewById(R.id.txtPrecioMerienda)
        pbAltaPlato = this.findViewById(R.id.pbAltaPlatoMerienda)
        btnImagenPlato.setOnClickListener { altaImagenPlato() }
        btnAltaPlato.setOnClickListener{ cargarPlato() }

        val spinner: Spinner = findViewById(R.id.spinnerMerienda)
        ArrayAdapter.createFromResource(
            this,
            R.array.sabor_plato_array,
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
        if (imageUrl != null) {
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
                    val platoReference: DatabaseReference = database.reference.child("desayunoMerienda").push()
                    platoReference.child("nombre").setValue(txtNombre.text.toString())
                    platoReference.child("ingredientes").setValue(txtDescripcion.text.toString())
                    platoReference.child("precio").setValue(txtPrecio.text.toString())
                    platoReference.child("imagenUrl").setValue(uri.toString())
                    platoReference.child("sabor").setValue(sabor) //TODO: evitar el harcoding aca
                    pbAltaPlato.visibility = View.INVISIBLE
                    Toast.makeText(this, "Plato Cargado!", Toast.LENGTH_LONG).show()
                    this.finish()
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        sabor = "Vegano"
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            sabor = parent.getItemAtPosition(position).toString()
        }
    }
}
