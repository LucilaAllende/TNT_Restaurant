package com.example.appcliente.ui.home.menumate

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
class MenuMateFragment : Fragment() {

    var vista: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_menu_mate, container, false)
        return vista
    }

    private fun verDetalles() {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
        Toast.makeText(context, "Que onda", Toast.LENGTH_LONG).show()
    }

    // TODO : Metodo que muestra el mensaje
    fun showDialog() {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage("The message here")
        dialogBuilder.setPositiveButton("Done",
            DialogInterface.OnClickListener { dialog, whichButton -> })
        val b = dialogBuilder.create()
        b.show()
    }

    @SuppressLint("ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val recyclerView: RecyclerView? = activity?.findViewById(R.id.recyclearMate)

        if (recyclerView != null) {
            recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        val paraMate= ArrayList<MenuMate>()
        llenarMenu(paraMate)

        recyclerView?.adapter= AdapterMenuMate(paraMate)

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
                val desayunoMerienda = p0.getValue(MenuMate::class.java)
                if (desayunoMerienda != null) {
                    println(desayunoMerienda)
                    paraMate.add(
                        MenuMate(
                            desayunoMerienda.nombre,
                            desayunoMerienda.ingredientes,
                            R.drawable.mate2,
                            desayunoMerienda.sabor,
                            desayunoMerienda.precio
                        )
                    )
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
                "Tortas fritas",
                "Harina\n" +
                        "Grasa\n" +
                        "Agua\n" +
                        "Sal\n" +
                        "Aceite",
                R.drawable.mate1,
                "Salado",
                "20.50"
            )
        )
        paraMate.add(
            MenuMate(
                "Churros rellenos de dulce de leche",
                "Harina\n" +
                        "Azucar\n" +
                        "Agua\n" +
                        "Sal\n" +
                        "Aceite\n+" +
                        "Dulce de Leche",
                R.drawable.mate2,
                "Agridulce",
                "20.50"
            )
        )
        paraMate.add(
            MenuMate(
                "Tarta de manzana",
                "Harina\n" +
                        "Azucar\n" +
                        "Leche\n" +
                        "Manzana\n" +
                        "Huevo\n+" +
                        "Mermelada de melocotón",
                R.drawable.mate3,
                "Dulce",
                "20.50"
            )
        )
    }


}
