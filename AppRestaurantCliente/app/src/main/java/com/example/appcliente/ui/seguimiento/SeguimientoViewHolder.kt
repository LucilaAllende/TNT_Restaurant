package com.example.appcliente.ui.seguimiento

import android.graphics.Color
import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.content_item_seguimiento.view.*
import kotlinx.android.synthetic.main.item_plato_pedido.view.*

class SeguimientoViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val txtNombre= view.cardView.txtNombrePlato
    val platoId = view.cardView.txtIdPlatoSeguimiento
    val txtEstado = view.cardView.txtEstado

    fun bind(p: com.example.appcliente.ui.pedido.Pedido) {
        /*
        if(p.tipo == "md"){ //menuDia
            color.setBackgroundColor(Color.parseColor("#7333691E")) //menu dia
        }
        else if(p.tipo == "mm"){ //menuMate
            color.setBackgroundColor(Color.parseColor("#73C3223A"))
        }
        else{
            color.setBackgroundColor(Color.parseColor("#730C184E"))
        }

         */
        txtNombre.text = p.nombrePlato
        platoId.text = p.platoId
        txtEstado.text = p.estado
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