package com.example.appempleado.listaPedidos

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import com.example.appempleado.R
import kotlinx.android.synthetic.main.content_item_m.view.*
import java.util.*

class AdapterPedido(private var list: ArrayList<Pedido>, private val listener: (Pedido) -> Unit) :

RecyclerView.Adapter<AdapterPedido.ViewHolder>(){

    //clase para manejar nuestra vista
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener{

        private val txtNombre: TextView = view.cardView.txtNombreViandaPedido
        private val color: TextView = view.bannerColor
        private val platoId: TextView = view.textViewPlatoIdPedido
        //val btnVerPedido = view.btnVerPedido

        fun bindItems (data: Pedido){
            when (data.tipo) {
                "md" -> { //menuDia
                    color.setBackgroundColor(Color.parseColor("#7333691E")) //menu dia
                }
                "mm" -> { //menuMate
                    color.setBackgroundColor(Color.parseColor("#73C3223A"))
                }
                else -> {
                    color.setBackgroundColor(Color.parseColor("#730C184E"))
                }
            }

            color.text = data.timestamp
            txtNombre.text = data.nombrePlato
            platoId.text = data.platoId

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
            /*
            when (v?.id) {
                R.id.btnVerPedido -> {
                    NavHostFragment.findNavController(DetallePedidoFragment())
                        .navigate(R.id.nav_detalle_pedido, null, options)

                }
            }

             */
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_item_m, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(list[position])
        //val btnVer: Button = holder.itemView.findViewById(R.id.button_ver_ingredientes_almuerzo)
        //btnVer.setOnClickListener { listener(list[position]) }

    }
}

/*
class PedidoAdapter(val resultado: ArrayList<Pedido>) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return resultado.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vh = ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_plato_historial,
                parent,
                false
            )
        )
        return vh
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(resultado.get(position))
    }
}

 */


//view holder
/*
class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val txtNombre = view.cardViewPedido.txtNombreViandaPedido
    //val txtPrecio = view.cardViewPedido.txtPrecioPedido
    val color = view.bannerColor
    val platoId = view.textViewPlatoIdPedido
    val btnNotificar = view.btnNotificarPedido
    //val btnEliminarPedido = view.btnEliminarPedido

    fun bind(p: Pedido) {
        if (p.tipo == "md") { //menuDia
            color.setBackgroundColor(Color.parseColor("#7333691E")) //menu dia
        } else if (p.tipo == "mm") { //menuMate
            color.setBackgroundColor(Color.parseColor("#73C3223A"))
        } else {
            color.setBackgroundColor(Color.parseColor("#730C184E"))
        }

        if (p.notificarme){
            btnNotificar.text = "Eliminar Suscripcion"
            btnNotificar.setTextColor(Color.parseColor("#ff0000"))
        }
        else{
            btnNotificar.text = "Suscribirme"
            btnNotificar.setTextColor(Color.parseColor("#484848"))
        }

        color.text = p.timestamp
        txtNombre.text = p.nombrePlato
        //txtPrecio.text = p.precioPlato
        platoId.text = p.platoId
        btnNotificar.setOnClickListener {
            if (btnNotificar.text.toString().contains("Suscribirme", ignoreCase = true)) {
                FirebaseMessaging.getInstance().subscribeToTopic(p.nombrePlato.replace(" ","_"))
                    .addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            println("error ")
                        }
                        btnNotificar.text = "Eliminar Suscripcion"
                        btnNotificar.setTextColor(Color.parseColor("#ff0000"))
                        FirebaseDatabase.getInstance().reference.child("Historial").child(p.id).child("notificarme").setValue(true)
                    }
            }
            else{
                FirebaseMessaging.getInstance().unsubscribeFromTopic(p.nombrePlato.replace(" ","_"))
                    .addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            println("error ")
                        }
                        btnNotificar.text = "Suscribirme"
                        btnNotificar.setTextColor(Color.parseColor("#484848"))
                        FirebaseDatabase.getInstance().reference.child("Historial").child(p.id).child("notificarme").setValue(false)
                    }
            }
        }
    }
}

 */