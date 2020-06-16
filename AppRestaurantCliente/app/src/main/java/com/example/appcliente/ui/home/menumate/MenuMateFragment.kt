package com.example.appcliente.ui.home.menumate

import com.example.appcliente.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase


/**
 * A simple [Fragment] subclass.
 */
val paraMate= ArrayList<MenuMate>()

class MenuMateFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var vista: View? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_menu_mate, container, false)
        verificarPlato()
        return vista
    }

    @SuppressLint("ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView = activity?.findViewById(R.id.recyclearMate)

        if (recyclerView != null) {
            recyclerView!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }


        //llenarMenu(paraMate)

        recyclerView?.adapter= AdapterMenuMate(paraMate)


    }

    private fun deboAgregarPlato(merienda: MenuMate): Boolean {
        var encontrado = true
        for (c in paraMate) {
            if (c.id == merienda.id &&
                c.imagenUrl == merienda.imagenUrl &&
                c.ingredientes == merienda.ingredientes &&
                c.nombre == merienda.nombre &&
                c.precio == merienda.precio &&
                c.sabor == merienda.sabor) {
                encontrado = false
            }
        }
        return encontrado
    }

    private fun verificarPlato(){
        FirebaseDatabase.getInstance().reference.child("desayunoMerienda").addChildEventListener(object :
            ChildEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                println("error trayendo datos de la base")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                if(p0.childrenCount > 0){
                    var merienda = p0.getValue(MenuMate::class.java)!!
                    merienda.id = p0.key.toString()
                    if (deboAgregarPlato(merienda)){
                        paraMate.add(merienda)
                        if (recyclerView != null) {
                            recyclerView!!.adapter!!.notifyDataSetChanged()
                        }
                    }
                }
                else{
                    if (recyclerView != null){
                        recyclerView!!.adapter!!.notifyDataSetChanged()
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }

    //Esto se va a borrar cuando trabajemos directamente con la BD
    private fun llenarMenu(paraMate: ArrayList<MenuMate>) {
        paraMate.add(
            MenuMate(
                "id1",
                "Tortas fritas",
                "Harina\n" +
                        "Grasa\n" +
                        "Agua\n" +
                        "Sal\n" +
                        "Aceite",
                "R.drawable.mate1",
                "Salado",
                "20.50"
            )
        )
        paraMate.add(
            MenuMate(
                "id2",
                "Churros rellenos de dulce de leche",
                "Harina\n" +
                        "Azucar\n" +
                        "Agua\n" +
                        "Sal\n" +
                        "Aceite\n+" +
                        "Dulce de Leche",
                "R.drawable.mate2",
                "Agridulce",
                "20.50"
            )
        )
        paraMate.add(
            MenuMate(
                "id3",
                "Tarta de manzana",
                "Harina\n" +
                        "Azucar\n" +
                        "Leche\n" +
                        "Manzana\n" +
                        "Huevo\n+" +
                        "Mermelada de melocotón",
                "R.drawable.mate3",
                "Dulce",
                "20.50"
            )
        )
    }


}
