package com.example.appcliente.ui.home.menumate

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appcliente.R


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

    @SuppressLint("ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val recyclerView: RecyclerView? = activity?.findViewById(R.id.recyclearMate)

        if (recyclerView != null) {
            recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        val paraMate= ArrayList<MenuMate>()
        llenarMenu(paraMate)

        recyclerView?.adapter=
            AdapterMenuMate(paraMate)
    }

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
                "Salado"
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
                "Agridulce"
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
                "Dulce"
            )
        )
    }


}
