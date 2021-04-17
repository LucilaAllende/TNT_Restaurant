package com.example.appempleado.eliminarPlatos

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import com.example.appempleado.R
import com.example.appempleado.listaPedidos.Pedido
import kotlinx.android.synthetic.main.content_item_eliminar_platos.view.*
import kotlinx.android.synthetic.main.content_item_m.view.*
import java.util.ArrayList
import com.bumptech.glide.Glide

//clase para manejar nuestra vista
class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

    private val txtNombre: TextView = view.txtNombrePlatoEliminar
    private val color: TextView = view.bannerColorEliminarPlatos
    private val platoId: TextView = view.textViewEliminarPlatosId
    private val imagen: ImageView = view.imageViewEliminarPedidos
    private val categoria: TextView = view.textViewCategoriaEliminar
    private val contexto: Context = this.itemView.context

    fun bindItems(plato: Plato) {
        var categoriaTxt = ""
        when (plato.categoria) {
            "md" -> { //menuDia
                color.setBackgroundColor(Color.parseColor("#7333691E")) //menu dia
                categoriaTxt = "Menu del Dia"
            }
            "mm" -> { //menuMate
                color.setBackgroundColor(Color.parseColor("#73C3223A"))
                categoriaTxt = "Menu Merienda"
            }
            else -> {
                color.setBackgroundColor(Color.parseColor("#730C184E"))
                categoriaTxt = "Menu Semanal"
            }
        }
        txtNombre.text = plato.nombrePlato
        platoId.text = plato.idPlato
        categoria.text = categoriaTxt
        Glide.with(contexto).load(Uri.parse(plato.imagen as String?)).into(imagen)
    }

    override fun onClick(v: View?) {
        TODO("not implemented")
    }

}

class AdapterPlato(val resultado: ArrayList<Plato>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.content_item_eliminar_platos, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return resultado.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(resultado[position])
    }

}



