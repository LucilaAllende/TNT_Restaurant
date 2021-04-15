package com.example.appempleado.listaPedidos

import android.annotation.SuppressLint
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appempleado.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.content_item_m.view.*
import kotlinx.android.synthetic.main.fragment_lista_pedidos.*
import java.util.*
import kotlin.collections.ArrayList

var r : RecyclerView? = null
var l : ArrayList<Pedido>? = null

class ListaPedidosFragment : Fragment() {
    private var vista: View? = null
    private val lista_pedidos = ArrayList<Pedido>()
    private lateinit var slideshowViewModel: ListaPedidosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        slideshowViewModel =
            ViewModelProvider(this).get(ListaPedidosViewModel::class.java)
        vista = inflater.inflate(R.layout.fragment_lista_pedidos, container, false)
        verificarPedidos()
        setHasOptionsMenu(true)
        return vista
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        FirebaseAuth.getInstance().signOut()
        findNavController().navigate(R.id.action_fragmentListaPedidos_to_fragmentPortada,null)
        return true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rvPedido.layoutManager = LinearLayoutManager(this.context)
        rvPedido.adapter = PedidoAdapter(lista_pedidos)
        r = rvPedido
        val swipeController = SwipeController()
        val itemTouchhelper = ItemTouchHelper(swipeController)
        itemTouchhelper.attachToRecyclerView(rvPedido)
        l = lista_pedidos
    }


    private fun deboAgregarPedido(p: Pedido):Boolean{
        var agregar = true
        if (p.estado == "PEDIDO ENVIADO"){
            agregar = false
        }
        else{
            for (c in lista_pedidos) {
                if (c.id == p.id &&
                    c.clienteId == p.clienteId &&
                    c.direccionEnvio == p.direccionEnvio &&
                    c.estado == p.estado &&
                    c.platoId == p.platoId &&
                    c.timestamp == p.timestamp &&
                    c.nombrePlato == p.nombrePlato &&
                    c.precioPlato == p.precioPlato &&
                    c.tipo == p.tipo ) {
                    agregar = false
                }
            }
        }
        return agregar
    }

    private fun verificarPedidos() {
        FirebaseDatabase.getInstance().reference.child("Pedido")
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
                        if (deboAgregarPedido(pedido)) {
                            lista_pedidos.add(pedido)
                            if (rvPedido != null) {
                                rvPedido.adapter!!.notifyDataSetChanged()
                            }
                        }

                    } else {
                        if (rvPedido != null) {
                            rvPedido.adapter!!.notifyDataSetChanged()
                        }
                        mostrarSnackbar("No hay pedidos aun.")
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    val pedido = p0.getValue(Pedido::class.java)!!
                    pedido.id = p0.key.toString()
                    val pos = lista_pedidos.indexOf(pedido)
                    lista_pedidos.remove(pedido)
                    if (rvPedido != null) {
                        rvPedido.adapter?.notifyItemRemoved(pos)
                        rvPedido.adapter?.notifyDataSetChanged()
                        if (lista_pedidos.size == 0) {
                            mostrarSnackbar("No hay pedidos aun.")
                        }
                    }
                }
            })
    }


    fun mostrarSnackbar(mensaje: String){
        activity?.findViewById<TabLayout>(R.id.tabs)?.let {
            Snackbar.make(
                it,
                mensaje, Snackbar.LENGTH_LONG
            )
        }?.show()
    }
}

fun actualizarEstadoPedido(pedido: Pedido){
    FirebaseDatabase.getInstance()
        .reference.child("Pedido")
        .child(pedido.id)
        .child("estado")
        .setValue(pedido.estado)
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
            if(p1 == ItemTouchHelper.RIGHT){
                pedido = l?.get(p0.adapterPosition)
                if (pedido != null) {
                    pedido.estado = "EN PREPARACION"
                    actualizarEstadoPedido(pedido)
                }
                l?.set(p0.adapterPosition, pedido!!)
            }
            else{
                pedido = l?.get(p0.adapterPosition)
                if (pedido != null) {
                    pedido.estado = "PEDIDO ENVIADO"
                    actualizarEstadoPedido(pedido)
                }
                l?.set(p0.adapterPosition, pedido!!)
            }
            //r?.itemAnimator?.changeDuration = 0
            //var vh = p0 as ViewHolderCustom
            //vh.marcarPedidoPendiente(p0)
             //r?.adapter?.notifyItemChanged(p0.getAdapterPosition())
            //r?.adapter?.notifyDataSetChanged()
            //println(".............................................................")
            //println(p0.adapterPosition)

            r?.adapter?.notifyItemChanged(p0.getAdapterPosition())

        }
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

       // var vh = viewHolder as ViewHolderCustom
        //vh.marcarPedidoPendiente(viewHolder)

        val itemView = viewHolder.itemView
        /*
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive




        if (isCanceled) {
            clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }*/

        // Draw the red delete background

        //FUNCIONA PERO NO ME SIRVE AUN
        /*
        background_derecha.color = Color.RED
        background_derecha.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
        background_derecha.draw(c)


        background_izquierda.color = Color.BLUE
        background_izquierda.setBounds(itemView.left , itemView.top, itemView.left + dX.toInt(), itemView.bottom)
        background_izquierda.draw(c)

         */


        // Calculate position of delete icon
        /*
        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + intrinsicHeight

        // Draw the delete icon
        deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteIcon.draw(c)

         */

        var newDx:Float = dX;
        if (newDx >= 150f) {
            newDx = 200f
        }

        if (newDx <= -150f) {
            newDx = -200f
        }

        super.onChildDraw(c, recyclerView, viewHolder, newDx, dY, actionState, isCurrentlyActive);
    }
}

class PedidoAdapter(val resultado: ArrayList<Pedido>) : RecyclerView.Adapter<ViewHolderCustom>() {

    override fun getItemCount(): Int {
        return resultado.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCustom {
        return ViewHolderCustom(
            LayoutInflater.from(parent.context).inflate(
                R.layout.content_item_m,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolderCustom, position: Int) {
        holder.bind(resultado[position])
    }
}


class ViewHolderCustom(view: View) : RecyclerView.ViewHolder(view) {

    var txtNombre = view.cardView.txtNombreViandaPedido
    val color = view.bannerColor
    val platoId = view.textViewPlatoIdPedido
    var hora = view.textViewHora
    var estado = view.textViewEstado
    var direction = view.textViewDireccion

    //val txtPrecio = view.cardViewPedido.txtPrecioPedido
    //val btnNotificar = view.btnVerPedido
    //val btnEliminarPedido = view.btnEliminarPedido

    @SuppressLint("SetTextI18n")
    fun bind(p: Pedido) {

        when (p.estado) {
            "EN PREPARACION" -> {
                color.setBackgroundColor(Color.parseColor("#F7CB73"))
            }
            "PEDIDO ENVIADO" -> {
                color.setBackgroundColor(Color.parseColor("#D9512C"))
            }
            else -> {
                color.setBackgroundColor(Color.parseColor("#077E8C")) //pendiente
            }
        }

        hora.text = "Hora: " + p.timestamp.takeLast(8).take(5)
        estado.text = "Estado: "+p.estado
        txtNombre.text = "Pedido: " +p.nombrePlato
        platoId.text = p.platoId

        if (p.direccionEnvio.contains("Aula")){
            println("ENTRE")
            val partes = p.direccionEnvio.split(" ")
            if (partes[1] == "") {
                direction.text = "Envío a: Oficina ${partes[3]}"
            }
            else if(partes[3] == "") {
                direction.text = "Envío a: Aula ${partes[1]}"
            }
            else {
                direction.text = "Envío a: Aula ${partes[1]} - Oficina ${partes[3]}"
            }
        }
        else if (p.direccionEnvio.contains("Longitud")){
            val partes = p.direccionEnvio.split(" ")
            val direccionObtenida = getCompleteAddressString(partes[1].toDouble(), partes[3].toDouble())

            direction.text = "Envío a: $direccionObtenida"
        }
        else if (p.direccionEnvio.contains("Buffet")){
            direction.text = p.direccionEnvio
        }
        else{
            direction.text = "Envío a: ${p.direccionEnvio}"
        }

    }

    fun marcarPedidoPendiente(viewHolder: RecyclerView.ViewHolder){
        viewHolder.itemView.txtNombreViandaPedido.text = "PEDIDO PENDIENTE CAPO!!!"
    }

    fun getCompleteAddressString(
        LATITUDE: Double,
        LONGITUDE: Double
    ): String? {
        var strAdd = ""

        val geocoder = Geocoder(itemView.context, Locale.getDefault())
        try {
            val addresses: List<Address> = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            val returnedAddress: Address = addresses[0]
            val strReturnedAddress = StringBuilder("")
            for (i in 0..returnedAddress.maxAddressLineIndex) {
                strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
            }
            strAdd = strReturnedAddress.toString()
            println("My Current loction address")
        } catch (e: Exception) {
            e.printStackTrace()
            println("My Current loction address Canont get Address!")
        }
        return strAdd
    }



}
//---------------------------------------------------------------------------
data class Usuario(
    var apellido: String ="",
    var email: String = "",
    var nombre: String ="",
    var localidad: String = "",
    var calle: String = ""
)





