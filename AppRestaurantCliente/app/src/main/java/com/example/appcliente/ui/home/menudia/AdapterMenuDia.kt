package com.example.appcliente.ui.home.menudia

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appcliente.R


class AdapterMenuDia(var list: ArrayList<PlatoDia>) :
    RecyclerView.Adapter<AdapterMenuDia.ViewHolder>(){

    //clase para manejar nuestra vista
    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) { //el view que vamos agregar dentro de este es el view que recibimos en la clase viewHolder
        val detallesPlatoFragment: DetallesPlatoFragment= itemView.findViewById(R.id.detallesPlatoFragment)
        //recibimos lo datos que se agregan dentro de nuestra vista
        fun bindItems (data: PlatoDia, holder: ViewHolder){
            //variables para nuestras vistas
            val title: TextView = itemView.findViewById(R.id.txtCategoria)
            val name: TextView = itemView.findViewById(R.id.txtNombrePlato)
            val image: ImageView = itemView.findViewById(R.id.imagen)
            val btnDetalles: Button = itemView.findViewById(R.id.button_ver_detalles)
            val precio : TextView = itemView.findViewById(R.id.txtPrecioPlato)

            title.text = data.categoria
            name.text = data.nombre
            precio.text = data.precio
            Glide.with(itemView.context).load(data.imagen).into(image)
            verificarCategoria(data, title)
            btnDetalles.setOnClickListener { verDetalles(detallesPlatoFragment) }
            //btnDetalles.setOnClickListener(fragmentDetallePlato.showDialog())
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
        // TODO :
        private fun verDetalles(detallesPlatoFragment: DetallesPlatoFragment) {
            val options = navOptions {
                anim {
                    enter = R.anim.slide_in_right
                    exit = R.anim.slide_out_left
                    popEnter = R.anim.slide_in_left
                    popExit = R.anim.slide_out_right
                }
            }
            Toast.makeText(itemView.context, "Que onda", Toast.LENGTH_LONG).show()
            detallesPlatoFragment.showDialog()
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
    }
}


