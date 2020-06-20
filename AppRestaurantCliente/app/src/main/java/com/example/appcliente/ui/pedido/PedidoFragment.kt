package com.example.appcliente.ui.pedido

import android.graphics.Color
import com.example.appcliente.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_pedido.*
import kotlinx.android.synthetic.main.fragment_pedido.view.*
import kotlinx.android.synthetic.main.item_plato_pedido.view.*


class PedidoFragment : Fragment() {

    private lateinit var galleryViewModel: PedidoViewModel
    var vista: View? = null
    var lista_pedidos: ArrayList<Pedido> = ArrayList()
    lateinit var btnFab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
        galleryViewModel =
            ViewModelProviders.of(this).get(PedidoViewModel::class.java)
        vista = inflater.inflate(R.layout.fragment_pedido, container, false)
        //btnFab = vista?.fab!!
        val fab: View? = vista?.findViewById(R.id.fab)
        fab?.setOnClickListener { view ->
            Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
            //findNavController(R.id.nav_direccion_pedido, null, options)
        }

        activity?.findViewById<TabLayout>(R.id.tabs)?.removeAllTabs()
        verificarPedidos()
        return vista
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rvPedidoVerdad.layoutManager = LinearLayoutManager(this.context)
        rvPedidoVerdad.adapter = PedidoAdapter(lista_pedidos)
    }

    private fun verificarPedidos(){
        FirebaseDatabase.getInstance().reference.child("Pedido")
            .orderByChild("clienteId")
            .equalTo(FirebaseAuth.getInstance().currentUser?.uid)
            .addChildEventListener(object: ChildEventListener {

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
                        if (rvPedidoVerdad != null){
                            rvPedidoVerdad.adapter!!.notifyDataSetChanged()
                        }
                    }
                    else{
                        if (rvPedidoVerdad != null){
                            rvPedidoVerdad.adapter!!.notifyDataSetChanged()
                        }
                        mostrarSnackbar("No hay pedidos aun.")
                    }
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    if(p0.childrenCount > 0){
                        val pedido = p0.getValue(Pedido::class.java)!!
                        pedido.id = p0.key.toString()
                        lista_pedidos.add(pedido)
                        if (rvPedidoVerdad != null){
                            rvPedidoVerdad.adapter!!.notifyDataSetChanged()
                        }
                    }
                    else{
                        if (rvPedidoVerdad != null){
                            rvPedidoVerdad.adapter!!.notifyDataSetChanged()
                        }
                        mostrarSnackbar("No hay pedidos aun.")
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot) {

                    if (rvPedidoVerdad != null){
                        val pedido = p0.getValue(Pedido::class.java)!!
                        pedido.id = p0.key.toString()
                        val pos = lista_pedidos.indexOf(pedido)
                        lista_pedidos.remove(pedido)
                        if (rvPedidoVerdad != null){
                            rvPedidoVerdad.adapter?.notifyItemRemoved(pos)
                            rvPedidoVerdad.adapter?.notifyDataSetChanged()
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


class PedidoAdapter(val resultado: ArrayList<Pedido>) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return resultado.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var vh = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_plato_pedido, parent, false))
        return vh
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(resultado.get(position))
    }
}


class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val txtNombre= view.cardViewPedido.txtNombrePlatoPedido
    val txtPrecio = view.cardViewPedido.txtPrecioPedido
    val color = view.bannerColor
    val platoId = view.textViewPlatoIdPedido
    val btnEliminar = view.btnEliminarPedido

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
    }
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
