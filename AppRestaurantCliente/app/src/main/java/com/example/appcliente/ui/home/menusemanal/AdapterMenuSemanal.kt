package com.example.appcliente.ui.home.menusemanal

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

class AdapterMenuSemanal(var list: ArrayList<Vianda>) :
    RecyclerView.Adapter<AdapterMenuSemanal.ViewHolder>(){

    //clase para manejar nuestra vista
    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) { //el view que vamos agregar dentro de este es el view que recibimos en la clase viewHolder

        //recibimos lo datos que se agregan dentro de nuestra vista
        fun bindItems (data: Vianda){

            //variables para nuestras vistas
            val title: TextView = itemView.findViewById(R.id.txtDia)
            val name: TextView = itemView.findViewById(R.id.txtNombreVianda)
            val image: ImageView = itemView.findViewById(R.id.imagenS)
            val btnDetalles: Button = itemView.findViewById(R.id.button_ver_detalles)
            val precio: TextView = itemView.findViewById(R.id.txtPrecioVianda)

            title.text = data.dia
            name.text = data.nombre
            precio.text = data.precio

            Glide.with(itemView.context).load(data.imagen).into(image)

            btnDetalles.setOnClickListener { verDetalles() }

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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(parent?.context).inflate(R.layout.content_item_ms, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(list[position])
    }
}