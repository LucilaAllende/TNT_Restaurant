package com.example.appcliente.ui.home.menudia

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appcliente.R
import com.example.appcliente.ui.interfaces.IComunicaFragments
import com.google.firebase.database.*


class MenuDiaFragment : Fragment() {

    var vista: View? = null
    val menuDia = ArrayList<PlatoDia>()
    var actividad: Activity? = null
    var interfaceComunicaFragments: IComunicaFragments? = null

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
        val recyclerView: RecyclerView? = activity?.findViewById(R.id.recyclear)

        if (recyclerView != null) {
            recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        llenarMenu(menuDia)
        recyclerView?.adapter = AdapterMenuDia(menuDia)

/*        recyclerView?.setOnClickListener(View.OnClickListener { view ->
            Toast.makeText(
                context, "Seleccion: " +
                        recyclerView?.getChildAdapterPosition(view)?.let {
                            series.get(it)
                                .name
                        }, Toast.LENGTH_SHORT
            ).show()
            interfaceComunicaFragments?.enviarMenuDia(
                recyclerView?.getChildAdapterPosition(
                    view
                )?.let {
                    series.get(
                        it
                    )
                }
            )
        })*/

        //TODO: Esto trae instancias de "Platos" de la base de datos
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

                val plato = p0.getValue(PlatoDia::class.java)
                if (plato != null) {
                    //TODO: Esta linea de abajo no se si esta bien. Buscar como es la forma de agregar dinamicamente elementos a un recyclerView!
                    menuDia.add(
                        PlatoDia(plato.nombre,
                           plato.ingredientes,
                           R.drawable.platovengano1,
                           plato.categoria,
                           plato.precio

                        )
                    )
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })


    }



    //Esto se va a borrar cuando trabajemos directamente con la BD
    private fun llenarMenu(series: ArrayList<PlatoDia>) {
        series.add(
            PlatoDia(
                "Hamburguesas de lenteja y brocoli",
                "½ cabeza de brócoli\n" +
                        "200 gr de lentejas cocidas\n" +
                        "1 cebolla\n" +
                        "2 ajos\n" +
                        "½ pimiento rojo",
                R.drawable.platovengano1,
                "Vegano",
                "$14.50"
            )
        )
        series.add(
            PlatoDia(
                "Espaguetis de calabacín con pesto rojo",
                "2 calabacines\n" +
                        "6 tomates secos\n" +
                        "1 cebolleta\n" +
                        "40 g de avellanas\n" +
                        "1 diente de ajo\n" +
                        "1 ramita de tomillo\n" +
                        "50 g de queso parmesano rallado\n" +
                        "Aceite de oliva\n" +
                        "Sal",
                R.drawable.platovegetariano1,
                "Vegetariano",
                "$10.50"
            )
        )
        series.add(
            PlatoDia(
                "Milanesa con puré de papa",
                "4 filetes de ternera\n" +
                        "3 huevos\n" +
                        "3 patatas\n" +
                        "1 diente de ajo\n" +
                        "150 ml de salsa de tomate\n" +
                        "150 gr de queso mozzarella\n" +
                        "1 tomate\n" +
                        "harina\n" +
                        "pan rallado\n" +
                        "4 aceitunas negras (sin hueso)\n" +
                        "aceite de oliva virgen extra\n" +
                        "sal\n" +
                        "pimienta\n" +
                        "orégano\n" +
                        "perejil",
                R.drawable.platocarnico1,
                "Carnico",
                "$12.50"
            )
        )
    }

/*    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            actividad = context
            interfaceComunicaFragments = actividad as IComunicaFragments?
        }
    }*/
}


data class Plato(
    var descripcion: String="",
    var imageUrl:String="",
    var nombre: String = "",
    var precio: String=""
)