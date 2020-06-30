package com.example.appcliente.ui.home.menusemanal

import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appcliente.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.content_item_ms.view.*
import java.text.SimpleDateFormat
import java.time.Duration
//import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class AdapterMenuSemanal(private var list: ArrayList<Vianda>, private val listener: (Vianda) -> Unit) :
    RecyclerView.Adapter<AdapterMenuSemanal.ViewHolder>(){

    //clase para manejar nuestra vista
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        //recibimos lo datos que se agregan dentro de nuestra vista
        @RequiresApi(Build.VERSION_CODES.O)
        fun bindItems (data: Vianda){

            //variables para nuestras vistas
            val title: TextView = itemView.findViewById(R.id.txtDia)
            val name: TextView = itemView.findViewById(R.id.txtNombreVianda)
            val image: ImageView = itemView.findViewById(R.id.imagenS)
            val btnAgregarPedido = itemView.button_agregar_pedido
            val btnVer: Button = itemView.findViewById(R.id.button_ver_ingredientes_vianda)
            val precio: TextView = itemView.findViewById(R.id.txtPrecioVianda)

            name.text = data.nombre
            precio.text = data.precio
            Glide.with(itemView.context).load(Uri.parse(data.imagenUrl)).into(image)
            btnAgregarPedido.setOnClickListener{ agregarPedido()}

            val fecha: List<String> = data.dia.split("/")
            title.text = "Pedilo antes del " + fecha[0] +"/"+fecha[1]
            /*
            try{
                var formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
                var date1 = LocalDate.parse(data.dia, formatter)
                var date2 = LocalDate.now()
                var resta = Duration.between(date2.atStartOfDay(), date1.atStartOfDay()).toDays()


                if (2 >= resta){
                    btnAgregarPedido.visibility = View.GONE
                    btnVer.visibility = View.GONE
                    title.text = "No Disponible"
                    title.setTextColor(Color.parseColor("#23456"))
                }
            }
            catch (e: Exception) {
                println("error")
            }*/
        }

        override fun onClick(v: View?) {

            val options = navOptions {
                anim {
                    enter = R.anim.slide_in_right
                    exit = R.anim.slide_out_left
                    popEnter = R.anim.slide_in_left
                    popExit = R.anim.slide_out_right
                }
            }

            when (v?.id) {
                R.id.button_ver_ingredientes_vianda -> {
                    NavHostFragment.findNavController(MenuSemanalFragment())
                        .navigate(R.id.nav_detalles_vianda, null, options)

                }
            }
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
                        "platoId" to itemView.textViewIdPlatoMS.text.toString(),
                        "nombrePlato" to itemView.txtNombreVianda.text.toString(),
                        "precioPlato" to itemView.txtPrecioVianda.text.toString(),
                        "direccionEnvio" to "aca va la direccion del cliente",
                        "estado" to "en preparacion", // [en preparación | en camino | entregado]
                        "tipo" to "ms")
                    val pedidoReference: DatabaseReference = database.reference.child("Pedido").push()
                    pedidoReference.setValue(pedido)
                    mostrarSnackbar("Plato agregado a Pedido!")

                } else {
                    mostrarSnackbar("Usuario no logueado")
                }
            }
            catch (e: Exception) {
                Toast.makeText(itemView.context, e.message, Toast.LENGTH_LONG).show()
            }
        }

        private fun mostrarSnackbar(mensaje: String){
            val snackBar = itemView.let {
                Snackbar.make(
                    it,
                    mensaje, Snackbar.LENGTH_LONG
                )
            }
            snackBar.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_item_ms, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(list[position])
        val btnVer: Button = holder.itemView.findViewById(R.id.button_ver_ingredientes_vianda)
        btnVer.setOnClickListener { listener(list[position]) }

        val btnPedirPlato: Button = holder.itemView.button_agregar_pedido

    }
}

/*data class Pedido(
    var id: String ="",
    var clienteId: String = "",
    var direccionEnvio: String = "",
    var estado:String = "",
    var platoId:String = "",
    var timestamp:String = "",
    var nombrePlato: String ="",
    var precioPlato:String="",
    var tipo:String=""
)*/