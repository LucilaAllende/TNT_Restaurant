package com.example.appempleado

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar.*


class ListaPedidosFragment : Fragment() {
    private var vista: View? = null
    private val listaPedidos = ArrayList<Pedido>()
    private var recyclerView: RecyclerView? = null
    private lateinit var slideshowViewModel: ListaPedidosViewModel

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        slideshowViewModel =
            ViewModelProvider(this).get(ListaPedidosViewModel::class.java)
        vista = inflater.inflate(R.layout.fragment_lista_pedidos, container, false)
        verificarPedidos()
        return vista
    }

    @SuppressLint("ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView = activity?.findViewById(R.id.recyclear)

        if (recyclerView != null) {
            recyclerView!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        recyclerView?.adapter = AdapterPedido(listaPedidos){
                item -> Toast.makeText(context, "Desde el fragment Lista Pedidos", Toast.LENGTH_SHORT).show()

            val options = navOptions {
                anim {
                    enter = R.anim.slide_in_right
                    exit = R.anim.slide_out_left
                    popEnter = R.anim.slide_in_left
                    popExit = R.anim.slide_out_right
                }
            }

            val bundle = bundleOf("nombre" to item.nombrePlato,"precio" to item.precioPlato)
            findNavController().navigate(R.id.nav_detalle_pedido, bundle, options)
        }

    }


    private fun verificarPedidos() {
        FirebaseDatabase.getInstance().reference.child("ListadoPedidos")
            .orderByChild("clienteId")
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
                        listaPedidos.add(pedido)
                        if (recyclerView != null) {
                            recyclerView!!.adapter!!.notifyDataSetChanged()
                        }
                    } else {
                        if (recyclerView != null) {
                            recyclerView!!.adapter!!.notifyDataSetChanged()
                        }
                        mostrarSnackbar("No hay pedidos aun.")
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot) {

                    if (recyclerView != null) {
                        val pedido = p0.getValue(Pedido::class.java)!!
                        pedido.id = p0.key.toString()
                        val pos = listaPedidos.indexOf(pedido)
                        listaPedidos.remove(pedido)
                        if (recyclerView != null) {
                            recyclerView!!.adapter?.notifyItemRemoved(pos)
                            recyclerView!!.adapter?.notifyDataSetChanged()
                            if (listaPedidos.size == 0) {
                                mostrarSnackbar("No hay pedidos aun.")
                            }
                        }
                    }
                }
            })
    }

    fun mostrarSnackbar(mensaje: String) {

        recyclerView?.let {
            make(
                it,
                mensaje, LENGTH_LONG
            )
        }?.show()
    }

}
