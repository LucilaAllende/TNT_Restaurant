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

class MenuSemanalActivity : AppCompatActivity() {

    val REQUEST_CODE = 123
    private var imageUrl: Uri? = null
    private lateinit var btnImagenPlato: View
    private lateinit var btnAltaPlato: View
    private lateinit var imgPlato: ImageView
    private lateinit var txtNombre: TextView
    private lateinit var txtDescripcion: TextView
    private lateinit var txtPrecio: TextView
    private lateinit var txtFecha: TextView
    private lateinit var pbAltaPlato: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_semanal)
        setTitle("Menu Semanal")
        btnImagenPlato =  this.findViewById<View>(R.id.btnImagenPlatoSemanal)
        btnAltaPlato =  this.findViewById<View>(R.id.btnAltaPlatoSemanal)
        imgPlato = this.findViewById(R.id.imgPlatoSemanal)
        txtNombre = this.findViewById(R.id.txtNombreSemanal)
        txtDescripcion  = this.findViewById(R.id.txtDescripcionSemanal)
        txtPrecio = this.findViewById(R.id.txtPrecioSemanal)
        txtFecha = this.findViewById(R.id.txtFechaSemanal)
        pbAltaPlato = this.findViewById(R.id.pbAltaPlatoSemanal)
        btnImagenPlato.setOnClickListener { altaImagenPlato() }
        btnAltaPlato.setOnClickListener{ cargarPlato() }

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
                    val platoReference: DatabaseReference = database.reference.child("Platos").push()
                    platoReference.child("nombre").setValue(txtNombre.text.toString())
                    platoReference.child("descripcion").setValue(txtDescripcion.text.toString())
                    platoReference.child("fechaDisponible").setValue(txtFecha)
                    platoReference.child("precio").setValue(txtPrecio.text.toString())
                    platoReference.child("imageUrl").setValue(uri.toString())
                    pbAltaPlato.visibility = View.INVISIBLE
                    Toast.makeText(this, "Plato Cargado!", Toast.LENGTH_LONG).show()
                    this.finish()
                }
            }
        }
    }
}
