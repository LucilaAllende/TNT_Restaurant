package com.example.appcliente.ui.seguimiento

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appcliente.R
import com.example.appcliente.ui.pedido.Pedido
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_pedido.*
import kotlinx.android.synthetic.main.fragment_seguimiento.*
import kotlin.collections.ArrayList

var r : RecyclerView? = null
var l : ArrayList<Pedido>? = null

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
        r = rvSeguimientoPedido
        val swipeController = SwipeController()
        val itemTouchhelper = ItemTouchHelper(swipeController)
        itemTouchhelper.attachToRecyclerView(rvSeguimientoPedido)
        l = listaPedidos
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
                        val p = listaPedidos.filter { it.id == p0.key.toString() }.first()
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
                        mostrarSnackbar("No hay pedidos aún.")
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
                        mostrarSnackbar("No hay pedidos aún.")
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
                            mostrarSnackbar("No hay pedidos aún.")
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
        snackBar?.show()
    }
}

fun eliminarPedidoDeEnCurso(pedido: Pedido){
    val database = FirebaseDatabase.getInstance()
    val historialReference: DatabaseReference = database.reference.child("Historial").push()
    historialReference.setValue(pedido)
    database.reference.child("Pedido/"+pedido.id).removeValue()
}


class SwipeController : ItemTouchHelper.Callback() {
    private val background_izquierda = ColorDrawable()
    private  val background_derecha = ColorDrawable()


    override fun getMovementFlags(p0: RecyclerView, p1: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        )
    }

    override fun onMove(
        p0: RecyclerView,
        p1: RecyclerView.ViewHolder,
        p2: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }


    override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
        var pedido: Pedido? = null
        if ((p1 == ItemTouchHelper.RIGHT) or(p1 == ItemTouchHelper.LEFT)) {
            pedido = l?.get(p0.adapterPosition)
            if (pedido != null && pedido.estado == "PEDIDO ENVIADO") {
                eliminarPedidoDeEnCurso(pedido)
                l?.removeAt(p0.adapterPosition)
                r?.adapter?.notifyDataSetChanged()
            }
        }
        r?.adapter?.notifyItemChanged(p0.getAdapterPosition())
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 0.1f;
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        var pedido = l?.get(viewHolder.adapterPosition)
        var newDx:Float = dX;
        if (newDx >= 150f) {
            if (pedido!!.estado != "PEDIDO ENVIADO"){
                newDx = 200f
            }
        }

        if (newDx <= -150f) {
            if (pedido!!.estado != "PEDIDO ENVIADO"){
                newDx = -200f
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, newDx, dY, actionState, isCurrentlyActive);
    }
}


