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

/**
 * A simple [Fragment] subclass.
 */
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
                item -> Toast.makeText(context, "Desde el fragment", Toast.LENGTH_SHORT).show()
            val action = MenuSemanalFragmentDirections.actionNavMenuSemanalToNavDetallesVianda("hola")

            val options = navOptions {
                anim {
                    enter = R.anim.slide_in_right
                    exit = R.anim.slide_out_left
                    popEnter = R.anim.slide_in_left
                    popExit = R.anim.slide_out_right
                }
            }

            val bundle = bundleOf("name" to item.nombre)
            findNavController().navigate(R.id.nav_detalles_dia, bundle, options)
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
                if (deboAgregarPlato(vianda!!)) {
                    viandas.add(vianda!!)
                    recyclerView?.getAdapter()?.notifyDataSetChanged();
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }
    //Esto se va a borrar cuando trabajemos directamente con la BD
    private fun llenarMenu(viandas: ArrayList<Vianda>) {
        viandas.add(
            Vianda(
                "id 1",
                "Gratinado de puerros con jamón y queso",
                "Puerro en conserva\n" +
                        "Queso Edam en lonchas\n" +
                        "Jamón de York en lonchas\n" +
                        "Salsa bechamel\n" +
                        "Queso rallado para gratinar",
                "R.drawable.vianda1",
                "Lunes",
                "$45.20"
            )
        )
        viandas.add(
            Vianda(
                "id 2",
                "Sándwich de ensalada de huevo",
                "Huevos\n" +
                        "Cebolla\n" +
                        "Mostaza\n" +
                        "Creme Fraiche\n" +
                        "Mayonesa\n" +
                        "Pan de molde sin corteza\n",
                "R.drawable.vianda2",
                "Martes",
                "$45.20"
            )
        )
        viandas.add(
            Vianda(
                "id 3",
                "Tarta de espinaca",
                "Tapas de tarta\n" +
                        "Espinaca\n" +
                        "Cebolla\n" +
                        "Ajo\n" +
                        "Huevo\n" +
                        "Aceitunas verdes\n" +
                        "Aceite de oliva\n" +
                        "Queso parmesano\n" +
                        "Sal \n" +
                        "Pimienta\n" +
                        "Nuez moscada\n",
                "R.drawable.vianda3",
                "Miercoles",
                "$45.20"
            )
        )
        viandas.add(
            Vianda(
                "id 4",
                "Pizza",
                "Tapas de tarta\n" +
                        "Harina\n" +
                        "Levadura\n" +
                        "Sal\n" +
                        "Aceite\n" +
                        "Salsa de tomate\n" +
                        "Mozzarella rallada\n" +
                        "Jamón cocido.\n" +
                        "Aceitunas negra \n" +
                        "Orégano seco \n",
                "R.drawable.vianda4",
                "Jueves",
                "$45.20"
            )
        )
        viandas.add(
            Vianda(
                "id 5",
                "Canelones de pollo",
                "Tapas de tarta\n" +
                        "Pechuga de pollo\n" +
                        "Cebolla\n" +
                        "Ajo\n" +
                        "Apio\n" +
                        "Huevo\n" +
                        "Perejil\n" +
                        "Queso rallado\n" +
                        "Sal \n" +
                        "Pimienta\n" +
                        "Aceite de oliva virgen extra\n",
                "R.drawable.vianda5",
                "Viernes",
                "$45.20"
            )
        )
    }

}
