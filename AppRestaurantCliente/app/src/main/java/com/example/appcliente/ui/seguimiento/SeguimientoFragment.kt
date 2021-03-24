package com.example.appcliente.ui.seguimiento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appcliente.R
import com.example.appcliente.ui.pedido.Pedido
import com.example.appcliente.ui.pedido.ViewHolder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_pedido.*
import kotlinx.android.synthetic.main.fragment_seguimiento.*

class SeguimientoFragment : Fragment() {

    private lateinit var galleryViewModel: SeguimientoViewModel
    var vista: View? = null
    var listaPedidos: ArrayList<Pedido> = ArrayList()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProvider(this).get(SeguimientoViewModel::class.java)
        vista = inflater.inflate(R.layout.fragment_seguimiento, container, false)

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
        rvSeguimientoPedido.layoutManager = LinearLayoutManager(this.context)
        val decoration = DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )
        rvSeguimientoPedido.addItemDecoration(decoration)
        rvSeguimientoPedido.adapter = SeguimientoAdapter(listaPedidos)
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
                    if(p0.childrenCount > 0){
                        val pedido = p0.getValue(Pedido::class.java)!!
                        var p = listaPedidos.filter { it.id == p0.key.toString() }.first()
                        p.estado = pedido.estado
                        listaPedidos.set(listaPedidos.indexOf(p), p)
                        if (rvSeguimientoPedido != null){
                            rvSeguimientoPedido.adapter!!.notifyDataSetChanged()
                        }
                    }
                    else{
                        if (rvSeguimientoPedido != null){
                            rvSeguimientoPedido.adapter!!.notifyDataSetChanged()
                        }
                        mostrarSnackbar("No hay pedidos aun.")
                    }
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    if(p0.childrenCount > 0){
                        val pedido = p0.getValue(Pedido::class.java)!!
                        pedido.id = p0.key.toString()
                        fab?.visibility = View.VISIBLE
                        if (deboAgregarPedido(pedido)){
                            listaPedidos.add(pedido)
                            if (rvSeguimientoPedido != null){
                                rvSeguimientoPedido.adapter!!.notifyDataSetChanged()
                            }
                        }

                    }
                    else{
                        if (rvSeguimientoPedido != null){
                            rvSeguimientoPedido.adapter!!.notifyDataSetChanged()
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
                    if (rvSeguimientoPedido != null){
                        rvSeguimientoPedido.adapter?.notifyItemRemoved(pos)
                        rvSeguimientoPedido.adapter?.notifyDataSetChanged()
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

