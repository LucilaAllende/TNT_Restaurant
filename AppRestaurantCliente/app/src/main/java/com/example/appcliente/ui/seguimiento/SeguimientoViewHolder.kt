package com.example.appcliente.ui.seguimiento

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appcliente.R
import com.example.appcliente.ui.TrackActivity
import com.example.appcliente.ui.home.menudia.PlatoDia
import com.example.appcliente.ui.home.menumate.MenuMate
import com.example.appcliente.ui.home.menusemanal.Vianda
import com.example.appcliente.ui.pedido.Pedido
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.firebase.database.*
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_track.*
import kotlinx.android.synthetic.main.content_item_seguimiento.view.*
import java.io.Serializable
import kotlin.collections.ArrayList


class SeguimientoViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    private val txtNombre: TextView = view.cardView.txtNombrePlato
    private val platoId: TextView = view.cardView.txtIdPlatoSeguimiento
    private val contexto: Context = this.itemView.context
    private var hora: TextView = view.textViewHora
    private val card: CardView = view.cardView
    private var color: TextView = view.cardView.bannerColor
    private var imagen = view.cardView?.imageViewSeguimientoPlato

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")
    fun bind(p: com.example.appcliente.ui.pedido.Pedido, size: Int) {

        var orderStatus = "0"

        if (p.estado == "EN PREPARACION") {
            orderStatus = "1"
        } else if (p.estado == "PEDIDO ENVIADO") {
            orderStatus = "2"
        }

        if (p.estado == "EN PREPARACION") {
            color.setBackgroundColor(Color.parseColor("#F7CB73"))
        } else if (p.estado == "PEDIDO ENVIADO") {
            color.setBackgroundColor(Color.parseColor("#D9512C"))
        } else {
            color.setBackgroundColor(Color.parseColor("#077E8C")) //pendiente
        }

        hora.text = "Pedido el " + p.timestamp
        txtNombre.text = p.nombrePlato
        platoId.text = p.platoId
        var tabla_plato = "desayunoMerienda"

        if (p.tipo == "md"){
            tabla_plato = "platoDia"
        }
        if (p.tipo == "ms"){
            tabla_plato = "vianda"
        }


        val database = FirebaseDatabase.getInstance()
        var DatabaseReference = database.reference.child(tabla_plato+'/'+p.platoId)
             DatabaseReference.addChildEventListener(object : ChildEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildChanged(
                    snapshot: DataSnapshot,
                    previousChildName: String?
                ) {


                }

                override fun onChildAdded(
                    dataSnapshot: DataSnapshot,
                    prevChildKey: String?
                ) {
                    println(".....44.........")
                    println(dataSnapshot)
                    println(dataSnapshot.value)
                    /*
                    println("CARGANDO IMGEEEEEE")
                    println(dataSnapshot)
                    println(dataSnapshot.value)
                    if (tabla_plato == "desayunoMerienda"){
                        val platoDia = dataSnapshot.getValue(MenuMate2::class.java)
                        Glide.with(itemView.context).load(Uri.parse(platoDia!!.imagenUrl)).into(imagen)
                    }
                    if (tabla_plato == "platoDia"){
                        val platoDia = dataSnapshot.getValue(PlatoDia::class.java)
                        Glide.with(itemView.context).load(Uri.parse(platoDia!!.imagenUrl)).into(imagen)
                    }
                    if (tabla_plato == "vianda"){
                        val platoDia = dataSnapshot.getValue(Vianda::class.java)
                        Glide.with(itemView.context).load(Uri.parse(platoDia!!.imagenUrl)).into(imagen)
                    }
                     */
                    if(dataSnapshot.key == "imagenUrl"){
                        Glide.with(contexto).load(Uri.parse(dataSnapshot.value as String?)).into(imagen)
                    }
                } // ...

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })




        card.setOnClickListener {
            val intent = Intent(contexto, TrackActivity::class.java)
            intent.putExtra("orderStatus", orderStatus)
            intent.putExtra("pedidoIdSeguimiento", p.id)
            intent.putExtra("horaPedidoSeguimiento", p.timestamp)
            intent.putExtra("cantPedidosSeguimiento", size.toString())
            val options = navOptions {
                anim {
                    enter = R.anim.slide_in_right
                    exit = R.anim.slide_out_left
                    popEnter = R.anim.slide_in_left
                    popExit = R.anim.slide_out_right
                }
                // PARA AGREGAR MOVIMIENTO , ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                contexto.startActivity(intent)
            }
            /*
            btn_recibido?.setOnClickListener {
                Toast.makeText(contexto, "Hola", Toast.LENGTH_LONG).show()
            }

             */

        }
    }

    @Parcelize
    class Pedidos : ArrayList<Pedido>(), Parcelable

    @Parcelize
    data class Pedido(
        var id: String = "",
        var clienteId: String = "",
        var direccionEnvio: String = "",
        var estado: String = "",
        var platoId: String = "",
        var timestamp: String = "",
        var nombrePlato: String = "",
        var precioPlato: String = "",
        var tipo: String = ""
    ) : Parcelable

    data class MenuMate2 (var id:String="",var nombre: String="", var ingredientes: String="", var imagenUrl: String="", var sabor: String="", var precio:String="") :
        Serializable
}