package com.example.appcliente.ui.home.menusemanal

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val recyclerView: RecyclerView? = activity?.findViewById(R.id.recyclearS)

        if (recyclerView != null) {
            recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        val viandas= ArrayList<Vianda>()
        llenarMenu(viandas)

        recyclerView?.adapter= AdapterMenuSemanal(viandas)

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
                if (vianda != null) {
                    println(vianda)
                    viandas.add(
                        Vianda(
                            vianda.name,
                            vianda.ingredientes,
                            R.drawable.vianda1,
                            vianda.dia,
                            vianda.precio
                        )
                    )
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
                "Gratinado de puerros con jamón y queso",
                "Puerro en conserva\n" +
                        "Queso Edam en lonchas\n" +
                        "Jamón de York en lonchas\n" +
                        "Salsa bechamel\n" +
                        "Queso rallado para gratinar",
                R.drawable.vianda1,
                "Lunes",
                "$45.20"
            )
        )
        viandas.add(
            Vianda(
                "Sándwich de ensalada de huevo",
                "Huevos\n" +
                        "Cebolla\n" +
                        "Mostaza\n" +
                        "Creme Fraiche\n" +
                        "Mayonesa\n" +
                        "Pan de molde sin corteza\n",
                R.drawable.vianda2,
                "Martes",
                "$45.20"
            )
        )
        viandas.add(
            Vianda(
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
                R.drawable.vianda3,
                "Miercoles",
                "$45.20"
            )
        )
        viandas.add(
            Vianda(
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
                R.drawable.vianda4,
                "Jueves",
                "$45.20"
            )
        )
        viandas.add(
            Vianda(
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
                R.drawable.vianda5,
                "Viernes",
                "$45.20"
            )
        )
    }

}
