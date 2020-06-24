package com.example.appcliente.ui.home.menudia

import android.annotation.SuppressLint
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
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions

class MenuDiaFragment : Fragment() {

    var vista: View? = null
    val menuDia = ArrayList<PlatoDia>()
    var recyclerView: RecyclerView? = null

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
                item -> Toast.makeText(context, "Desde el fragment",Toast.LENGTH_SHORT).show()
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
                var platoDia = p0.getValue(PlatoDia::class.java)
                platoDia!!.id = p0.key.toString()
                if (deboAgregarPlato(platoDia!!)) {
                    menuDia.add(platoDia!!)
                    recyclerView?.getAdapter()?.notifyDataSetChanged();
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }

    private fun llenarMenu(series: ArrayList<PlatoDia>) {
        println(":::::::::::::::::::::::")
        println("LLENANDO")
        println(":::::::::::::::::::::::")
        series.add(
            PlatoDia(
                "md 1",
                "Hamburguesas de lenteja y brocoli",
                "½ cabeza de brócoli\n" +
                        "200 gr de lentejas cocidas\n" +
                        "1 cebolla\n" +
                        "2 ajos\n" +
                        "½ pimiento rojo",
                "R.drawable.platovengano1",
                "Vegano",
                "$14.50"
            )
        )
        series.add(
            PlatoDia(
                "md 2",
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
                "R.drawable.platovegetariano1",
                "Vegetariano",
                "$10.50"
            )
        )
        series.add(
            PlatoDia(
                    "md 3",
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
                "R.drawable.platocarnico1",
                "Carnico",
                "$12.50"
            )
        )
    }
}
