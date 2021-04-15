package com.example.appempleado.verEstadisticas

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appempleado.R
import com.example.appempleado.listaPedidos.ListaPedidosViewModel
import com.example.appempleado.listaPedidos.Pedido
import com.example.appempleado.listaPedidos.SwipeController
import com.example.appempleado.listaPedidos.ViewHolderCustom
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.content_item_m.view.*
import kotlinx.android.synthetic.main.fragment_estadisticas.*

/**
 * A simple [Fragment] subclass.
 */
var r : RecyclerView? = null
var l : ArrayList<Pedido>? = null

class EstadisticasFragment : Fragment() {

    var vista: View? = null
    var lista_pedidos: ArrayList<Pedido> = ArrayList()
    private lateinit var slideshowViewModel: EstadisticasViewModel
    var recaudado: Double = 0.0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        slideshowViewModel =
            ViewModelProvider(this).get(EstadisticasViewModel::class.java)
        vista = inflater.inflate(R.layout.fragment_estadisticas, container, false)
        verificarPedidos()
        println("---------------------------------------------")
        println("ESTADISTICAS")
        for (elemento in lista_pedidos){
            println("Elemento: $elemento")
            recaudado += elemento.precioPlato.toDouble()
        }
        println("REACUADADO $recaudado")

        return vista
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rvEstadistica.layoutManager = LinearLayoutManager(this.context)
        rvEstadistica.adapter = PedidoAdapter(lista_pedidos)
        r = rvEstadistica
        val swipeController = SwipeController()
        val itemTouchhelper = ItemTouchHelper(swipeController)
        itemTouchhelper.attachToRecyclerView(rvEstadistica)
        l = lista_pedidos
    }

    private fun verificarPedidos() {
        FirebaseDatabase.getInstance().reference.child("Historial")
            .addChildEventListener(object : ChildEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    println("cancelado")
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    println("movido")
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {

                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    if (p0.childrenCount > 0) {
                        val pedido = p0.getValue(Pedido::class.java)!!
                        pedido.id = p0.key.toString()
                        lista_pedidos.add(pedido)
                        recaudado += recaudado + pedido.precioPlato.toDouble()
                        println("HELLLO $recaudado")
                        if (rvEstadistica != null) {
                            rvEstadistica.adapter!!.notifyDataSetChanged()
                        }
                    } else {
                        if (rvEstadistica != null) {
                            rvEstadistica.adapter!!.notifyDataSetChanged()
                        }
                        mostrarSnackbar("No hay pedidos aun.")
                    }


                }

                override fun onChildRemoved(p0: DataSnapshot) {

                }
            })
    }

    fun mostrarSnackbar(mensaje: String) {
        activity?.findViewById<TabLayout>(R.id.tabsi)?.let {
            Snackbar.make(
                it,
                mensaje, Snackbar.LENGTH_LONG
            )
        }?.show()
    }
}

class PedidoAdapter(val resultado: ArrayList<Pedido>) : RecyclerView.Adapter<ViewHolderCustom>() {

    override fun getItemCount(): Int {
        return resultado.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCustom {
        val vh = ViewHolderCustom(
            LayoutInflater.from(parent.context).inflate(
                R.layout.content_item_m,
                parent,
                false
            )
        )
        return vh
    }

    override fun onBindViewHolder(holder: ViewHolderCustom, position: Int) {
        holder.bind(resultado.get(position))
    }

}

class ViewHolderCustom(view: View) : RecyclerView.ViewHolder(view) {

    var txtNombre = view.cardView.txtNombreViandaPedido
    //val txtPrecio = view.cardViewPedido.txtPrecioPedido
    val color = view.bannerColor
    val platoId = view.textViewPlatoIdPedido
    var hora = view.textViewHora
    var estado = view.textViewEstado
    //val btnNotificar = view.btnVerPedido
    //val btnEliminarPedido = view.btnEliminarPedido

    fun bind(p: Pedido) {
        /*
        if (p.tipo == "md") { //menuDia
            color.setBackgroundColor(Color.parseColor("#7333691E")) //menu dia
        } else if (p.tipo == "mm") { //menuMate
            color.setBackgroundColor(Color.parseColor("#73C3223A"))
        } else {
            color.setBackgroundColor(Color.parseColor("#730C184E"))
        }

         */

        if(p.estado == "EN PREPARACION"){
            color.setBackgroundColor(Color.parseColor("#F7CB73"))
        }
        else if(p.estado == "PEDIDO ENVIADO"){
            color.setBackgroundColor(Color.parseColor("#D9512C"))
        }
        else{
            color.setBackgroundColor(Color.parseColor("#077E8C")) //pendiente
        }

        hora.text = "Hora pedido: " + p.timestamp.takeLast(8).take(5)
        estado.text = "Estado: "+p.estado
        txtNombre.text = "Nombre Pedido: " +p.nombrePlato
        platoId.text = p.platoId
        // btnNotificar.setOnClickListener {}
    }

    fun marcarPedidoPendiente(viewHolder: RecyclerView.ViewHolder){
        viewHolder.itemView.txtNombreViandaPedido.text = "PEDIDO PENDIENTE CAPO!!!"
    }

}

