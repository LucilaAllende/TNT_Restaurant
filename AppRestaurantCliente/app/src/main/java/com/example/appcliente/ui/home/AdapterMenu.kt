package com.example.appcliente.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appcliente.R

class AdapterMenu (var list: ArrayList<Menu>): RecyclerView.Adapter<AdapterMenu.ViewHolder>(){
    //clase para manejar nuestra vista
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){ //el view que vamos agregar dentro de este es el view que recibimos en la clase viewHolder

        //recibimos lo datos que se agregan dentro de nuestra vista
        fun bindItems (data: Menu){
            //variables para nuestras vistas
            val title: TextView = itemView.findViewById(R.id.txtTitle)
            val tempo: TextView = itemView.findViewById(R.id.txtTemporadas)
            val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)

            title.text = data.name
            tempo.text = data.descripcion.toString()

            Glide.with(itemView.context).load(data.imagen).into(thumbnail)

            itemView.setOnClickListener{
                Toast.makeText(itemView.context, "Ver ${data.name}", Toast.LENGTH_SHORT).show()
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

    override fun onBindViewHolder(holder: AdapterMenu.ViewHolder, position: Int) {
        holder.bindItems(list[position])
    }
}