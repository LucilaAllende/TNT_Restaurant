package com.example.appcliente.ui.home.menudia

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appcliente.R
import kotlinx.android.synthetic.main.content_item_md.view.*


class AdapterMenuDia(var list: ArrayList<PlatoDia>) :
    RecyclerView.Adapter<AdapterMenuDia.ViewHolder>(){

    //clase para manejar nuestra vista
    //el view que vamos agregar dentro de este es el view que recibimos en la clase viewHolder
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val btnVer: Button = view.findViewById(R.id.button_ver_detalles)
        private val context: Context = view.context

        //recibimos lo datos que se agregan dentro de nuestra vista
        fun bindItems (data: PlatoDia, holder: ViewHolder){
            //variables para nuestras vistas
            val title: TextView = itemView.findViewById(R.id.txtCategoria)
            val name: TextView = itemView.findViewById(R.id.txtNombrePlato)
            val image: ImageView = itemView.findViewById(R.id.imagen)
            val precio : TextView = itemView.findViewById(R.id.txtPrecioPlato)
            val ingredientes : TextView = itemView.findViewById(R.id.txt_ingredientes)

            title.text = data.categoria
            name.text = data.nombre
            precio.text = data.precio
            ingredientes.text = data.ingredientes
            Glide.with(itemView.context).load(data.imagen).into(image)

            verificarCategoria(data, title)
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

        fun escuchame(){
            btnVer.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.button_ver_detalles -> {
                    val intent = Intent(context, DetallesActivity::class.java)
                    intent.putExtra("name", itemView.txtNombrePlato.text);
                    intent.putExtra("ingredientes", itemView.txt_ingredientes.text )
                    context.startActivity(intent)
                }
            }
        }

/*        private fun verDetalles(detallesPlatoFragment: DetallesPlatoFragment) {
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
        }*/
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
        holder.escuchame()
    }
}


