package com.example.appempleado.cargarMenu

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.appempleado.R
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_menu_del_dia.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MenuDelDiaActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {
    val REQUEST_CODE = 123
    private var imageUrl: Uri? = null
    private lateinit var btnImagenPlato: View
    private lateinit var btnAltaPlato: View
    private lateinit var imgPlato: ImageView
    private lateinit var txtNombre: TextView
    private lateinit var txtDescripcion: TextView
    private lateinit var txtPrecio: TextView
    private lateinit var pbAltaPlato: ProgressBar
    private var tipoPlato: String = "Vegano" /*Esto indica vegano, carnico o vegetariano*/


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



        var list_of_items = arrayOf("Vegano", "Vegetariano", "Carnico")
        spinner!!.setOnItemSelectedListener(this)
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_of_items)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.setAdapter(aa)
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

    @SuppressLint("ResourceType")
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

                        var body = txtNombre.text.toString() + " vuelve a estar disponible. Pedilo ahora!"
                        var titulo = "No te lo pierdas!"
                        var topico = txtNombre.text.toString().replace(" ", "_")

                        val icon: ImageView? = null
                        icon?.setImageResource(R.drawable.ic_restaurant_black_24dp)

                        agregarNotificacion(body,titulo,topico)

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
            tipoPlato = parent!!.getItemAtPosition(position).toString()
        }



    fun agregarNotificacion(body:String, titulo:String, topico:String) {
        val apiService = ServiceBuilder.buildService(RestApi::class.java)
        val peticion = Peticion(  to = "/topics/"+topico,
                                   notification = Notification(body=body,
                                                               title = titulo,
                                                               sound = "default",
                                                               color="#ff4455")
        )
        var call = apiService.addNotificacion(peticion)
        call.enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                if (response.isSuccessful){
                    println("respuesta exitosa")
                }
            }
            override fun onFailure(call: Call<Result>, t: Throwable) {
                println("error")
            }
        })
    }

    data class Result(
        var message_id: String=""
    )

    data class Notification (
        var body: String?,
        var title: String?,
        var sound: String?,
        var color: String?
    )

    data class Peticion (
        var to:String="",
        @SerializedName("notification") var notification: Notification
    )
    //TODO: esta clave dejarla en un archivo no harcodeada aca!
    interface RestApi {
        @Headers("Content-Type: application/json","Authorization: key=AAAA77Jxuy4:APA91bHzcGTqP3yIxNOeYZdB0a16bBAQ3UCa5sQ78DfrALwbUkKhVR9-8pxl2OGhPtRxdpB4oefP-G2-7h1ju6BAVq8JFUQJfRO4MUuOxJXSqhnxqmsJM7HoMzj4xsoZbmRI8ajAbpSZ")
        @POST("send")
        fun addNotificacion(@Body respuesta: Peticion): Call<Result>
    }

    object ServiceBuilder {
        private val client = OkHttpClient.Builder().build()

        private val retrofit = Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/fcm/") // change this IP for testing by your actual machine IP
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        fun<T> buildService(service: Class<T>): T{
            return retrofit.create(service)
        }
    }



}





