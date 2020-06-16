package com.example.appcliente.ui.historial

import android.graphics.Color
import com.example.appcliente.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.content_item_md.view.*
import kotlinx.android.synthetic.main.fragment_historial.*
import kotlinx.android.synthetic.main.item_pedido.*
import kotlinx.android.synthetic.main.item_pedido.view.*


class HistorialFragment : Fragment() {

    private lateinit var slideshowViewModel: HistorialViewModel
    var vista: View? = null
    private var viewPager: ViewPager? = null
    var lista_pedidos: ArrayList<Pedido> = ArrayList()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
                ViewModelProviders.of(this).get(HistorialViewModel::class.java)
        vista= inflater.inflate(R.layout.fragment_historial, container, false)
        activity?.findViewById<TabLayout>(R.id.tabs)?.removeAllTabs() // remove all the tabs from the action bar and deselect the current tab
        verificarPedidos()
        return vista
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rvPedido.layoutManager = LinearLayoutManager(this.context)
        rvPedido.adapter = PedidoAdapter(lista_pedidos)

    }

    private fun verificarPedidos(){
        FirebaseDatabase.getInstance().reference.child("Pedido")
            .orderByChild("clienteId")
            .equalTo(FirebaseAuth.getInstance().currentUser?.uid)
            .addChildEventListener(object: ChildEventListener{

                override fun onCancelled(p0: DatabaseError) {
                    println("cancelado")
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    println("movido")
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    if(p0.childrenCount > 0){
                        val pedido = p0.getValue(Pedido::class.java)!!
                        pedido.id = p0.key.toString()
                        lista_pedidos.add(pedido)
                        if (rvPedido != null){
                            rvPedido.adapter!!.notifyDataSetChanged()
                        }
                    }
                    else{
                        if (rvPedido != null){
                            rvPedido.adapter!!.notifyDataSetChanged()
                        }
                        mostrarSnackbar("No hay pedidos aun.")
                    }
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    if(p0.childrenCount > 0){
                        val pedido = p0.getValue(Pedido::class.java)!!
                        pedido.id = p0.key.toString()
                        lista_pedidos.add(pedido)
                        if (rvPedido != null){
                            rvPedido.adapter!!.notifyDataSetChanged()
                        }
                    }
                    else{
                        if (rvPedido != null){
                            rvPedido.adapter!!.notifyDataSetChanged()
                        }
                        mostrarSnackbar("No hay pedidos aun.")
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot) {

                    if (rvPedido != null){
                        val pedido = p0.getValue(Pedido::class.java)!!
                        pedido.id = p0.key.toString()
                        val pos = lista_pedidos.indexOf(pedido)
                        lista_pedidos.remove(pedido)
                        if (rvPedido != null){
                            rvPedido.adapter?.notifyItemRemoved(pos)
                            rvPedido.adapter?.notifyDataSetChanged()
                            if (lista_pedidos.size == 0){
                                mostrarSnackbar("No hay pedidos aun.")
                            }
                        }
                    }
                }
        })
    }
    fun mostrarSnackbar(mensaje: String){
        val snackBar = activity?.findViewById<TabLayout>(R.id.tabs)?.let {
            Snackbar.make(
                it,
                mensaje, Snackbar.LENGTH_LONG
            )
        }
        if (snackBar != null) {
            snackBar.show()
        }
    }



}

private fun eliminarPedido(idPedido: String){
    FirebaseDatabase.getInstance().reference.child("Pedido/"+idPedido).removeValue()
}

fun notificarPedido(){
   //mostrarSnackbar("Hacer la notificacion")
}

class PedidoAdapter(val resultado: ArrayList<Pedido>) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return resultado.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var vh = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pedido, parent, false))
        return vh
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(resultado.get(position))
    }
}


class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val txtNombre= view.cardViewPedido.txtNombreViandaPedido
    val txtPrecio = view.cardViewPedido.txtPrecioPedido
    val color = view.bannerColor
    val platoId = view.textViewPlatoIdPedido
    val btnEliminar = view.btnEliminarPedido
    val btnNotificar = view.btnNotificarPedido

    fun bind(p: Pedido) {
        if(p.tipo == "md"){ //menuDia
            color.setBackgroundColor(Color.parseColor("#F87652")) //menu dia
        }
        else if(p.tipo == "mm"){ //menuMate
            color.setBackgroundColor(Color.parseColor("#C3223A"))
        }
        else{
            color.setBackgroundColor(Color.parseColor("#757575"))
        }
        color.text = p.timestamp
        txtNombre.text = p.nombrePlato
        txtPrecio.text = p.precioPlato
        platoId.text = p.platoId
        btnEliminar.setOnClickListener{eliminarPedido(p.id)}
        btnNotificar.setOnClickListener{ notificarPedido()}
    }
    //view.viewCard.btnEliminarPedido
   // btnNotificarPedido.setOnClickListener{ notificarPedido()}

}


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
)

