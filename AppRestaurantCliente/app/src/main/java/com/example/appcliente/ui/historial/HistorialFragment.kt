package com.example.appcliente.ui.historial

import android.graphics.Color
import com.example.appcliente.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_historial.*
import kotlinx.android.synthetic.main.item_plato_historial.view.*
import com.google.firebase.messaging.FirebaseMessaging

class HistorialFragment : Fragment() {

    private lateinit var slideshowViewModel: HistorialViewModel
    var vista: View? = null
    var lista_pedidos: ArrayList<Pedido> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
            ViewModelProvider(this).get(HistorialViewModel::class.java)
        vista = inflater.inflate(R.layout.fragment_historial, container, false)

        val tabsi = activity?.findViewById<TabLayout>(R.id.tabs)
        tabsi!!.visibility = View.GONE // remove all the tabs from the action bar and deselect the current tab
        verificarPedidos()
        return vista

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rvPedido.layoutManager = LinearLayoutManager(this.context)
        rvPedido.adapter = PedidoAdapter(lista_pedidos)

    }

    private fun verificarPedidos() {
        FirebaseDatabase.getInstance().reference.child("Historial")
            .orderByChild("clienteId")
            .equalTo(FirebaseAuth.getInstance().currentUser?.uid)
            .addChildEventListener(object : ChildEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    println("cancelado")
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    println("movido")
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    /*
                    if (p0.childrenCount > 0) {
                        val pedido = p0.getValue(Pedido::class.java)!!
                        pedido.id = p0.key.toString()
                        lista_pedidos.add(pedido)
                        if (rvPedido != null) {
                            rvPedido.adapter!!.notifyDataSetChanged()
                        }
                    } else {
                        if (rvPedido != null) {
                            rvPedido.adapter!!.notifyDataSetChanged()
                        }
                        mostrarSnackbar("No hay pedidos aun.")
                    }
                    */
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    if (p0.childrenCount > 0) {
                        val pedido = p0.getValue(Pedido::class.java)!!
                        pedido.id = p0.key.toString()
                        lista_pedidos.add(pedido)
                        if (rvPedido != null) {
                            rvPedido.adapter!!.notifyDataSetChanged()
                        }
                    } else {
                        if (rvPedido != null) {
                            rvPedido.adapter!!.notifyDataSetChanged()
                        }
                        mostrarSnackbar("No hay pedidos aún.")
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot) {

                    if (rvPedido != null) {
                        val pedido = p0.getValue(Pedido::class.java)!!
                        pedido.id = p0.key.toString()
                        val pos = lista_pedidos.indexOf(pedido)
                        lista_pedidos.remove(pedido)
                        if (rvPedido != null) {
                            rvPedido.adapter?.notifyItemRemoved(pos)
                            rvPedido.adapter?.notifyDataSetChanged()
                            if (lista_pedidos.size == 0) {
                                mostrarSnackbar("No hay pedidos aún.")
                            }
                        }
                    }
                }
            })
    }

    fun mostrarSnackbar(mensaje: String) {
        activity?.findViewById<TabLayout>(R.id.tabs)?.let {
            Snackbar.make(
                it,
                mensaje, Snackbar.LENGTH_LONG
            )
        }?.show()
    }


}



    class PedidoAdapter(val resultado: ArrayList<Pedido>) : RecyclerView.Adapter<ViewHolder>() {

        override fun getItemCount(): Int {
            return resultado.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val vh = ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_plato_historial,
                    parent,
                    false
                )
            )
            return vh
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(resultado.get(position))
        }
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre = view.cardViewPedido.txtNombreViandaPedido
        //val txtPrecio = view.cardViewPedido.txtPrecioPedido
        val color = view.bannerColor
        val platoId = view.textViewPlatoIdPedido
        val btnNotificar = view.btnNotificarPedido
        //val btnEliminarPedido = view.btnEliminarPedido

        fun bind(p: Pedido) {
            if (p.tipo == "md") { //menuDia
                color.setBackgroundColor(Color.parseColor("#7333691E")) //menu dia
            } else if (p.tipo == "mm") { //menuMate
                color.setBackgroundColor(Color.parseColor("#73C3223A"))
            } else {
                color.setBackgroundColor(Color.parseColor("#730C184E"))
            }

            if (p.notificarme){
                btnNotificar.text = "Eliminar Suscripcion"
                btnNotificar.setTextColor(Color.parseColor("#ff0000"))
            }
            else{
                btnNotificar.text = "Suscribirme"
                btnNotificar.setTextColor(Color.parseColor("#484848"))
            }

            color.text = p.timestamp
            txtNombre.text = p.nombrePlato
            //txtPrecio.text = p.precioPlato
            platoId.text = p.platoId
            btnNotificar.setOnClickListener {
                if (btnNotificar.text.toString().contains("Suscribirme", ignoreCase = true)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(p.nombrePlato.replace(" ","_"))
                        .addOnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                println("error ")
                            }
                            btnNotificar.text = "Eliminar Suscripcion"
                            btnNotificar.setTextColor(Color.parseColor("#ff0000"))
                            FirebaseDatabase.getInstance().reference.child("Historial").child(p.id).child("notificarme").setValue(true)
                        }
                }
                else{
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(p.nombrePlato.replace(" ","_"))
                        .addOnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                println("error ")
                            }
                            btnNotificar.text = "Suscribirme"
                            btnNotificar.setTextColor(Color.parseColor("#484848"))
                            FirebaseDatabase.getInstance().reference.child("Historial").child(p.id).child("notificarme").setValue(false)
                        }
                }
            }
        }
    }




    data class Pedido(
        var id: String = "",
        var clienteId: String = "",
        var direccionEnvio: String = "",
        var estado: String = "",
        var platoId: String = "",
        var timestamp: String = "",
        var nombrePlato: String = "",
        var precioPlato: String = "",
        var tipo: String = "",
        var notificarme: Boolean = false
    )


data class Usuario(
    var apellido: String ="",
    var email: String = "",
    var nombre: String ="",
    var localidad: String = "",
    var calle: String = ""
)

