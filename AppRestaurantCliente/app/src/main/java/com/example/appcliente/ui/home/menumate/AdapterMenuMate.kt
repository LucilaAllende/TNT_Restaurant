package com.example.appcliente.ui.home.menumate

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appcliente.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.content_item_md.view.*
import kotlinx.android.synthetic.main.content_item_mm.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterMenuMate(var list: ArrayList<MenuMate>) :
    RecyclerView.Adapter<AdapterMenuMate.ViewHolder>(){

    //clase para manejar nuestra vista
    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) { //el view que vamos agregar dentro de este es el view que recibimos en la clase viewHolder

        //recibimos lo datos que se agregan dentro de nuestra vista
        fun bindItems (data: MenuMate){

            //variables para nuestras vistas
            val title: TextView = itemView.findViewById(R.id.txtSabor)
            val name: TextView = itemView.findViewById(R.id.txtNombre)
            val image: ImageView = itemView.findViewById(R.id.imagenM)
            val btnDetalles: Button = itemView.findViewById(R.id.button_ver_detalles)
            val btnPedido: Button = itemView.findViewById(R.id.button_agregar_pedido)
            var precio: TextView = itemView.findViewById(R.id.txtPrecio)

            title.text = data.sabor
            name.text = data.nombre
            precio.text = data.precio

            Glide.with(itemView.context).load(Uri.parse(data.imagenUrl)).into(image)

            btnDetalles.setOnClickListener { verDetalles() }
            btnPedido.setOnClickListener{verificarPedido()}

            itemView.setOnClickListener{
                Toast.makeText(itemView.context, "Ver ${data.nombre}", Toast.LENGTH_SHORT).show()
            }
        }

        private fun verDetalles() {

            val options = navOptions {
                anim {
                    enter = R.anim.slide_in_right
                    exit = R.anim.slide_out_left
                    popEnter = R.anim.slide_in_left
                    popExit = R.anim.slide_out_right
                }
            }

            Toast.makeText(itemView.context, "Que onda", Toast.LENGTH_LONG).show()
            //findNavController().navigate(R.id.nav_detalles_vianda, null, options)
        }


        private fun verificarPedido(){ //TODO : agregar validaciones si faltan
            agregarPedido()
        }


        private fun agregarPedido(){
            try {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    val database = FirebaseDatabase.getInstance()
                    val date = Calendar.getInstance().time
                    val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
                    val formatedDate = formatter.format(date)
                    val pedido = mapOf("timestamp" to formatedDate.toString(),
                        "clienteId" to user.uid,
                        "platoId" to itemView.txtIdPlatoMM.text.toString(),
                        "nombrePlato" to itemView.txtNombre.text.toString(),
                        "precioPlato" to itemView.txtPrecio.text.toString(),
                        "direccionEnvio" to "aca va la direccion del cliente",
                        "estado" to "en preparacion", // [en preparación | en camino | entregado]
                        "tipo" to "mm")
                    val pedidoReference: DatabaseReference = database.reference.child("Pedido").push()
                    pedidoReference.setValue(pedido)
                    mostrarSnackbar("Pedido Agregado!")

                } else {
                    mostrarSnackbar("Usuario no logueado")
                }
            }
            catch (e: Exception) {
                Toast.makeText(itemView.context, e.message, Toast.LENGTH_LONG).show()
            }
        }

        private fun mostrarSnackbar(mensaje: String){
            val snackBar = itemView?.let {
                Snackbar.make(
                    it,
                    mensaje, Snackbar.LENGTH_LONG
                )
            }
            if (snackBar != null) {
                snackBar.show()
            }
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(parent?.context).inflate(R.layout.content_item_mm, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(list[position])
    }
}

data class Pedido(
    var id: String ="",
    var clienteId: String = "",
    var direccionEnvio: String = "",
    var estado:String = "",
    var platoId:String = "",
    var timestamp:String = "",
    var nombrePlato: String ="",
    var precioPlato:String="",
    var tipo:String=""
)
