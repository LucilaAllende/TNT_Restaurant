package com.example.appcliente.ui.pedido

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appcliente.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_pedido.*
import kotlinx.android.synthetic.main.item_plato_pedido.view.*


class PedidoFragment : Fragment() {

    private lateinit var galleryViewModel: PedidoViewModel
    var vista: View? = null
    var listaPedidos: ArrayList<Pedido> = ArrayList()
    var fab: FloatingActionButton? = null

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

        fab= vista?.findViewById(R.id.fab)
        fab?.visibility = View.INVISIBLE
        fab?.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelableArrayList("pedidos", listaPedidos)
            findNavController().navigate(R.id.nav_solicitar_direccion_pedido, bundle, options)
        }


        val tabsi = activity?.findViewById<TabLayout>(R.id.tabs)
        tabsi!!.visibility = View.GONE

        return vista
    }

    override fun onStart() {
        super.onStart()
        verificarPedidos()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rvPedidoVerdad.layoutManager = LinearLayoutManager(this.context)
        val decoration = DividerItemDecoration(
            context,
            VERTICAL
        )
        rvPedidoVerdad.addItemDecoration(decoration)
        rvPedidoVerdad.adapter = PedidoAdapter(listaPedidos)
    }

    private fun deboAgregarPedido(p: Pedido):Boolean{
        var encontrado = true

        for (c in listaPedidos) {
            if (c.id == p.id &&
                c.clienteId == p.clienteId &&
                c.direccionEnvio == p.direccionEnvio &&
                c.estado == p.estado &&
                c.platoId == p.platoId &&
                c.timestamp == p.timestamp &&
                c.nombrePlato == p.nombrePlato &&
                c.precioPlato == p.precioPlato &&
                c.tipo == p.tipo) {
                encontrado = false
            }
        }
        return encontrado

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
                    /*
                    if(p0.childrenCount > 0){
                        val pedido = p0.getValue(Pedido::class.java)!!
                        pedido.id = p0.key.toString()
                        listaPedidos.add(pedido)
                        if (rvPedidoVerdad != null){
                            fab?.visibility = View.VISIBLE
                            rvPedidoVerdad.adapter!!.notifyDataSetChanged()
                        }
                    }
                    else{
                        if (rvPedidoVerdad != null){
                            rvPedidoVerdad.adapter!!.notifyDataSetChanged()
                        }
                        mostrarSnackbar("No hay pedidos aun.")
                    }*/
                    println("cambiANDOOOO!!!")
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    if(p0.childrenCount > 0){
                        val pedido = p0.getValue(Pedido::class.java)!!
                        pedido.id = p0.key.toString()
                        fab?.visibility = View.VISIBLE
                        if (deboAgregarPedido(pedido)){
                            listaPedidos.add(pedido)
                            if (rvPedidoVerdad != null){
                                rvPedidoVerdad.adapter!!.notifyDataSetChanged()
                            }
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
                    fab?.visibility = View.VISIBLE
                    val pedido = p0.getValue(Pedido::class.java)!!
                    pedido.id = p0.key.toString()
                    val pos = listaPedidos.indexOf(pedido)
                    listaPedidos.remove(pedido)
                    if (rvPedidoVerdad != null){
                        rvPedidoVerdad.adapter?.notifyItemRemoved(pos)
                        rvPedidoVerdad.adapter?.notifyDataSetChanged()
                        if (listaPedidos.size == 0){
                            mostrarSnackbar("No hay pedidos aun.")
                            fab?.visibility = View.INVISIBLE
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
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_plato_pedido, parent, false))
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
    val txtEstado = view.txt_estado_pedido
    val btnEliminar = view.btnEliminarPedido

    fun bind(p: Pedido) {
        if(p.tipo == "md"){ //menuDia
            color.setBackgroundColor(Color.parseColor("#7333691E")) //menu dia
        }
        else if(p.tipo == "mm"){ //menuMate
            color.setBackgroundColor(Color.parseColor("#73C3223A"))
        }
        else{
            color.setBackgroundColor(Color.parseColor("#730C184E"))
        }
        color.text = p.timestamp
        txtNombre.text = p.nombrePlato
        txtPrecio.text = p.precioPlato
        platoId.text = p.platoId
        txtEstado.text = p.estado
        btnEliminar.setOnClickListener{eliminarPedido(p.id)}
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
