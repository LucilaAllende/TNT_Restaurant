package com.example.appcliente.ui.home.menudia

import android.annotation.SuppressLint
import android.net.Uri
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.content_item_md.view.*
import com.example.appcliente.R
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


class AdapterMenuDia(var list: ArrayList<PlatoDia>, private val listener: (PlatoDia) -> Unit) :
    RecyclerView.Adapter<AdapterMenuDia.ViewHolder>(){

    //clase para manejar nuestra vista
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        private val btnVer: Button = view.findViewById(R.id.button_ver_detalles)
        private val context: Context = view.context


        fun bindItems (data: PlatoDia, holder: ViewHolder){
            //variables para nuestras vistas
            val title: TextView = itemView.findViewById(R.id.txtCategoria)
            val name: TextView = itemView.findViewById(R.id.txtNombrePlato)
            val image: ImageView = itemView.findViewById(R.id.imagen)
            val precio : TextView = itemView.findViewById(R.id.txtPrecioPlato)
            val idPlato: TextView = itemView.txtIdPlatoMD
            val ingredientes : TextView = itemView.findViewById(R.id.txt_ingredientes)

            idPlato.text = data.id
            title.text = data.categoria
            name.text = data.nombre
            precio.text = data.precio
	        ingredientes.text = data.ingredientes
            Glide.with(itemView.context).load(Uri.parse(data.imagenUrl)).into(image)
            //image.setImageURI(Uri.parse(data.imagen))


            verificarCategoria(data, title)
            itemView.button_agregar_pedido.setOnClickListener{verificarPedido()}
            itemView.setOnClickListener{
                Toast.makeText(itemView.context, "Ver ${data.nombre}", Toast.LENGTH_SHORT).show()
            }
        }


        @SuppressLint("ResourceAsColor")
        private fun verificarCategoria(data: PlatoDia, title: TextView) = when (data.categoria) {
            "Vegano" -> {
                title.setBackgroundColor(R.color.color_vegano)
            }
            "Vegetariano" -> {
                title.setBackgroundColor(R.color.color_vegetariano)
            }
            else -> {
                title.setBackgroundColor(R.color.color_carnico)
            }
        }

/*
        fun escuchame(){
            btnVer.setOnClickListener(this)
        }
*/

        fun onClick(v: View?) {

            val options = navOptions {
                anim {
                    enter = R.anim.slide_in_right
                    exit = R.anim.slide_out_left
                    popEnter = R.anim.slide_in_left
                    popExit = R.anim.slide_out_right
                }
            }

            when (v?.id) {
                R.id.button_ver_detalles -> {
                    findNavController(DetallesPlatoFragment()).navigate(R.id.nav_detalles_dia, null, options)

                }
            }
        }


        private fun verificarPedido(){

            /*
            if (itemView.button_agregar_pedido.text.toString().contains("PEDIR PLATO", ignoreCase = true)) {
                //Toast.makeText(itemView.context, "ELIMINAR PEDIDO", Toast.LENGTH_LONG).show()
                itemView.button_agregar_pedido.text = "ELIMINAR PEDIDO"
                itemView.button_agregar_pedido.setTextColor(Color.parseColor("#ff0000"))
            }
            else{
                //Toast.makeText(itemView.context, "ELIMINAR PEDIDO" , Toast.LENGTH_LONG).show()
                itemView.button_agregar_pedido.text = "PEDIR PLATO"
                itemView.button_agregar_pedido.setTextColor(Color.parseColor("#484848"))
            }
            */
            //eliminarPedido()
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
                                            "platoId" to itemView.txtIdPlatoMD.text.toString(),
                                            "nombrePlato" to itemView.txtNombrePlato.text.toString(),
                                            "precioPlato" to itemView.txtPrecioPlato.text.toString(),
                                            "direccionEnvio" to "aca va la direccion del cliente",
                                            "estado" to "en preparacion", // [en preparación | en camino | entregado]
                                            "tipo" to "md")
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
        var v = LayoutInflater.from(parent?.context).inflate(R.layout.content_item_md, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(list[position], holder)
        //set eventos
        //holder.escuchame()
        holder.itemView.setOnClickListener { listener(list[position]) }
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
