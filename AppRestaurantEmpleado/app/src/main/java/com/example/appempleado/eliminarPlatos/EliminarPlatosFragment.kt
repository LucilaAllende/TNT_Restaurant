package com.example.appempleado.eliminarPlatos

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
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
import com.example.appempleado.listaPedidos.Pedido
import com.example.appempleado.listaPedidos.l
import com.example.appempleado.listaPedidos.r
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.content_item_m.view.*
import kotlinx.android.synthetic.main.fragment_eliminar_platos.*
import kotlinx.android.synthetic.main.fragment_lista_pedidos.*
import java.util.*
import kotlin.collections.ArrayList

var r1 : RecyclerView? = null
var l1 : ArrayList<Plato>? = null

class EliminarPlatosFragment : Fragment() {
    private var vista: View? = null
    private val lista_platos = ArrayList<Plato>()
    private lateinit var slideshowViewModel: EliminarPlatosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        slideshowViewModel =
            ViewModelProvider(this).get(EliminarPlatosViewModel::class.java)
        vista = inflater.inflate(R.layout.fragment_eliminar_platos, container, false)
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
        rvEliminarPlatos.layoutManager = LinearLayoutManager(this.context)
        rvEliminarPlatos.adapter = AdapterPlato(lista_platos)
        r1 = rvEliminarPlatos
        val swipeController = SwipeController()
        val itemTouchhelper = ItemTouchHelper(swipeController)
        itemTouchhelper.attachToRecyclerView(rvEliminarPlatos)
        l1 = lista_platos
    }


    private fun deboAgregarPlato(p: Plato):Boolean{
        var agregar = true
        for (c in lista_platos) {
            if (c.idPlato == p.idPlato &&
                c.nombrePlato == p.nombrePlato &&
                c.categoria == p.categoria ) {
                agregar = false
            }
        }
        return agregar
    }

    private fun verificarPedidos() {
        FirebaseDatabase.getInstance().reference.child("platoDia")
            .addChildEventListener(object : ChildEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    println("cancelado")
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    println("movido")
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    println("agregado")
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    println("removido")
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                     if (p0.childrenCount > 0) {
                        val pd = p0.getValue(PlatoDia::class.java)!!
                        val plato = Plato()
                        plato.idPlato = p0.key.toString()
                        plato.nombrePlato = pd.nombre
                        plato.categoria = "md"
                        plato.imagen = pd.imagenUrl
                        if (deboAgregarPlato(plato)) {
                            lista_platos.add(plato)
                            if (rvEliminarPlatos != null) {
                                rvEliminarPlatos.adapter!!.notifyDataSetChanged()
                            }
                        }

                    } else {
                        if (rvEliminarPlatos != null) {
                            rvEliminarPlatos.adapter!!.notifyDataSetChanged()
                        }
                        mostrarSnackbar("No hay pedidos aun.")
                    }
                }
            })

        //menumate
        FirebaseDatabase.getInstance().reference.child("desayunoMerienda")
            .addChildEventListener(object : ChildEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    println("cancelado")
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    println("movido")
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    println("agregado")
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    println("removido")
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    if (p0.childrenCount > 0) {
                        val pd = p0.getValue(MenuMate::class.java)!!
                        val plato = Plato()
                        plato.idPlato = p0.key.toString()
                        plato.nombrePlato = pd.nombre
                        plato.categoria = "mm"
                        plato.imagen = pd.imagenUrl
                        if (deboAgregarPlato(plato)) {
                            lista_platos.add(plato)
                            if (rvEliminarPlatos != null) {
                                rvEliminarPlatos.adapter!!.notifyDataSetChanged()
                            }
                        }

                    } else {
                        if (rvEliminarPlatos != null) {
                            rvEliminarPlatos.adapter!!.notifyDataSetChanged()
                        }
                        mostrarSnackbar("No hay pedidos aun.")
                    }
                }
            })


        //menu semanal
        FirebaseDatabase.getInstance().reference.child("vianda")
            .addChildEventListener(object : ChildEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    println("cancelado")
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    println("movido")
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    println("agregado")
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    println("removido")
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    if (p0.childrenCount > 0) {
                        val pd = p0.getValue(Vianda::class.java)!!
                        val plato = Plato()
                        plato.idPlato = p0.key.toString()
                        plato.nombrePlato = pd.nombre
                        plato.categoria = "ms"
                        plato.imagen = pd.imagenUrl
                        if (deboAgregarPlato(plato)) {
                            lista_platos.add(plato)
                            if (rvEliminarPlatos != null) {
                                rvEliminarPlatos.adapter!!.notifyDataSetChanged()
                            }
                        }

                    } else {
                        if (rvEliminarPlatos != null) {
                            rvEliminarPlatos.adapter!!.notifyDataSetChanged()
                        }
                        mostrarSnackbar("No hay pedidos aun.")
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


fun eliminarPlato(plato: Plato){
    val database = FirebaseDatabase.getInstance()
    var tabla_plato = "desayunoMerienda"

    if (plato.categoria == "md"){
        tabla_plato = "platoDia"
    }
    if (plato.categoria == "ms"){
        tabla_plato = "vianda"
    }
    println(".................")
    println(plato)
    println()
    println(".................")
    database.reference.child(tabla_plato+'/'+ plato.idPlato).removeValue()
}

class SwipeController : ItemTouchHelper.Callback() {

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
        var plato: Plato? = null
        if ((p1 == ItemTouchHelper.RIGHT) or(p1 == ItemTouchHelper.LEFT)) {
            plato = l1?.get(p0.adapterPosition)
            if (plato != null) {
                eliminarPlato(plato)
                l1?.removeAt(p0.adapterPosition)
                r1?.adapter?.notifyDataSetChanged()
            }
        }
        r1?.adapter?.notifyItemChanged(p0.getAdapterPosition())
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
        val itemView = viewHolder.itemView
        var newDx:Float = dX;
        super.onChildDraw(c, recyclerView, viewHolder, newDx, dY, actionState, isCurrentlyActive);
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

data class PlatoDia (var id:String="", var nombre: String ="", var ingredientes: String="", var imagenUrl: String="", var categoria: String="", var precio:String="")
data class MenuMate (var id:String="",var nombre: String="", var ingredientes: String="", var imagenUrl: String="", var sabor: String="", var precio:String="")
data class Vianda (var id:String="",var nombre: String="", var ingredientes: String="", var imagenUrl: String="", var dia: String="", var precio: String="")

