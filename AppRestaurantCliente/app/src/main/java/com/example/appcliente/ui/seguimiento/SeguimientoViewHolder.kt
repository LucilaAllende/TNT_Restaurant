package com.example.appcliente.ui.seguimiento

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Parcelable
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.appcliente.ui.TrackActivity
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.content_item_seguimiento.view.*
import kotlin.collections.ArrayList


class SeguimientoViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    private val txtNombre: TextView = view.cardView.txtNombrePlato
    private val platoId: TextView = view.cardView.txtIdPlatoSeguimiento
    private val contexto: Context = this.itemView.context
    private var hora: TextView = view.textViewHora
    private val card: CardView = view.cardView
    private var color: TextView = view.cardView.bannerColor
    private var btn_recibido = view.cardView?.btnEliminarSeguimiento

    @SuppressLint("SetTextI18n")
    fun bind(p: com.example.appcliente.ui.pedido.Pedido, size: Int) {
        var orderStatus = "0"

        if(p.estado == "EN PREPARACION"){
            orderStatus = "1"
        }
        else if(p.estado == "PEDIDO ENVIADO"){
            orderStatus = "2"
        }

        if(p.estado == "EN PREPARACION"){
            color.setBackgroundColor(Color.parseColor("#F7CB73"))
        }
        else if(p.estado == "PEDIDO ENVIADO"){
            color.setBackgroundColor(Color.parseColor("#D9512C"))
        }
        else{
            color.setBackgroundColor(Color.parseColor("#077E8C")) //pendiente
        }

        hora.text = "Pedido el " + p.timestamp
        txtNombre.text = p.nombrePlato
        platoId.text = p.platoId
        card.setOnClickListener{
            val intent = Intent(contexto, TrackActivity::class.java)
            intent.putExtra("orderStatus",orderStatus)
            intent.putExtra("pedidoIdSeguimiento",p.id)
            intent.putExtra("horaPedidoSeguimiento",p.timestamp)
            intent.putExtra("cantPedidosSeguimiento",size.toString())
            contexto.startActivity(intent)
        }

        btn_recibido?.setOnClickListener { Toast.makeText(contexto, "Hola", Toast.LENGTH_LONG).show() }

    }
}

@Parcelize
class Pedidos: ArrayList<Pedido>(), Parcelable

@Parcelize
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
): Parcelable