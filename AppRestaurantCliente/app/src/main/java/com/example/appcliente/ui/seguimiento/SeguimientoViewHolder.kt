package com.example.appcliente.ui.seguimiento

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.appcliente.ui.TrackActivity
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.content_item_seguimiento.view.*


class SeguimientoViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val txtNombre= view.cardView.txtNombrePlato
    val platoId = view.cardView.txtIdPlatoSeguimiento
    val contexto = this.itemView.context
    var hora = view.textViewHora
    val card = view.cardView
    var color = view.cardView.bannerColor

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
            var intent = Intent(contexto, TrackActivity::class.java)
            intent.putExtra("orderStatus",orderStatus)
            intent.putExtra("pedidoIdSeguimiento",p.id)
            intent.putExtra("horaPedidoSeguimiento",p.timestamp)
            intent.putExtra("cantPedidosSeguimiento",size.toString())
            contexto.startActivity(intent)
        }
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