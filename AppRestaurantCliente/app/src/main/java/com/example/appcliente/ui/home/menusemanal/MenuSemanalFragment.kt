package com.example.appcliente.ui.home.menusemanal

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appcliente.R
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class MenuSemanalFragment : Fragment() {

    var vista: View? = null
    val viandas= ArrayList<Vianda>()
    var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_menu_semanal, container, false)
        return vista
    }

    @SuppressLint("ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView = activity?.findViewById(R.id.recyclearS)
        if (recyclerView != null) {
            recyclerView!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        //llenarMenu(viandas)
        recyclerView?.adapter= AdapterMenuSemanal(viandas){
                item -> Toast.makeText(context, "Desde el fragment Vianda", Toast.LENGTH_SHORT).show()
            //val action = MenuSemanalFragmentDirections.actionNavMenuSemanalToNavDetallesVianda("hola")

            val options = navOptions {
                anim {
                    enter = R.anim.slide_in_right
                    exit = R.anim.slide_out_left
                    popEnter = R.anim.slide_in_left
                    popExit = R.anim.slide_out_right
                }
            }

            val bundle = bundleOf("nombre" to item.nombre,"ingredientes" to item.ingredientes)
            findNavController().navigate(R.id.nav_detalles_vianda, bundle, options)
        }
        verificarPlato()
    }

    private fun deboAgregarPlato(vianda: Vianda): Boolean {
        var encontrado = true
        for (c in viandas) {
            if (c.id == vianda.id &&
                c.imagenUrl == vianda.imagenUrl &&
                c.ingredientes == vianda.ingredientes &&
                c.nombre == vianda.nombre &&
                c.precio == vianda.precio &&
                c.dia == vianda.dia) {
                encontrado = false
            }
        }
        return encontrado
    }

    private fun verificarPlato(){
        FirebaseDatabase.getInstance().reference.child("vianda").addChildEventListener(object :
            ChildEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                println("error trayendo datos de la base")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val vianda = p0.getValue(Vianda::class.java)
                vianda!!.id = p0.key.toString()
                if (deboAgregarPlato(vianda)) {
                    viandas.add(vianda)
                    recyclerView?.adapter?.notifyDataSetChanged()
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }
}
