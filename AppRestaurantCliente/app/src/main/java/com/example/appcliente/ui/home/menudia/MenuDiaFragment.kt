package com.example.appcliente.ui.home.menudia

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appcliente.R
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions

class MenuDiaFragment : Fragment() {

    var vista: View? = null
    val menuDia = ArrayList<PlatoDia>()
    var recyclerView: RecyclerView? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        vista= inflater.inflate(R.layout.fragment_menu_dia, container, false)
        return vista
    }

    @SuppressLint("ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView = activity?.findViewById(R.id.recyclear)

        if (recyclerView != null) {
            recyclerView!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        recyclerView?.adapter = AdapterMenuDia(menuDia){
                item -> Toast.makeText(context, "Desde el fragment Menu dia",Toast.LENGTH_SHORT).show()
                //val action = MenuDiaFragmentDirections.actionNavMenuDiaToNavDetallesDia("hola", "ingredientes")

                val options = navOptions {
                    anim {
                        enter = R.anim.slide_in_right
                        exit = R.anim.slide_out_left
                        popEnter = R.anim.slide_in_left
                        popExit = R.anim.slide_out_right
                    }
                }

                val bundle = bundleOf("nombre" to item.nombre,"ingredientes" to item.ingredientes)
                findNavController().navigate(R.id.nav_detalles_dia, bundle, options)
        }
        verificarPlato()
    }

    private fun deboAgregarPlato(platoDia: PlatoDia): Boolean {
        var encontrado = true
        for (c in menuDia) {
            if (c.id == platoDia.id &&
                c.imagenUrl == platoDia.imagenUrl &&
                c.ingredientes == platoDia.ingredientes &&
                c.nombre == platoDia.nombre &&
                c.precio == platoDia.precio &&
                c.categoria == platoDia.categoria) {
                encontrado = false
            }
        }
        return encontrado
    }

    private fun verificarPlato(){
        FirebaseDatabase.getInstance().reference.child("platoDia").addChildEventListener(object :
            ChildEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                println("error trayendo datos de la base")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val platoDia = p0.getValue(PlatoDia::class.java)
                platoDia!!.id = p0.key.toString()
                if (deboAgregarPlato(platoDia)) {
                    menuDia.add(platoDia)
                    recyclerView?.adapter?.notifyDataSetChanged()
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }
}
