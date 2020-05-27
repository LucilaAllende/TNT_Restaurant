package com.example.appcliente.ui.home.menudia

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appcliente.R

class AdapterMenuDia (var list: ArrayList<MenuDia>): RecyclerView.Adapter<AdapterMenuDia.ViewHolder>(){
    //clase para manejar nuestra vista
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){ //el view que vamos agregar dentro de este es el view que recibimos en la clase viewHolder

        //recibimos lo datos que se agregan dentro de nuestra vista
        fun bindItems (data: MenuDia){
            //variables para nuestras vistas
            val title: TextView = itemView.findViewById(R.id.txtCategoria)
            val name: TextView = itemView.findViewById(R.id.txtNombrePlato)
            val image: ImageView = itemView.findViewById(R.id.imagen)

            title.text = data.categoria
            name.text = data.name

            Glide.with(itemView.context).load(data.imagen).into(image)

            verificarCategoria(data, title)

            itemView.setOnClickListener{
                Toast.makeText(itemView.context, "Ver ${data.name}", Toast.LENGTH_SHORT).show()
            }

        }

        @SuppressLint("ResourceAsColor")
        private fun verificarCategoria(data: MenuDia, title: TextView) = when (data.categoria) {
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(parent?.context).inflate(R.layout.content_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(list[position])
    }
}