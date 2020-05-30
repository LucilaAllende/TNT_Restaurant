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

        val viandas= ArrayList<MenuSemanal>()
        llenarMenu(viandas)

        recyclerView?.adapter=
            AdapterMenuSemanal(viandas)
    }

    private fun llenarMenu(viandas: ArrayList<MenuSemanal>) {
        viandas.add(
            MenuSemanal(
                "Gratinado de puerros con jamón y queso",
                "Puerro en conserva\n" +
                        "Queso Edam en lonchas\n" +
                        "Jamón de York en lonchas\n" +
                        "Salsa bechamel\n" +
                        "Queso rallado para gratinar",
                R.drawable.vianda1,
                "Lunes"
            )
        )
        viandas.add(
            MenuSemanal(
                "Sándwich de ensalada de huevo",
                "Huevos\n" +
                        "Cebolla\n" +
                        "Mostaza\n" +
                        "Creme Fraiche\n" +
                        "Mayonesa\n" +
                        "Pan de molde sin corteza\n",
                R.drawable.vianda2,
                "Martes"
            )
        )
        viandas.add(
            MenuSemanal(
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
                "Miercoles"
            )
        )
        viandas.add(
            MenuSemanal(
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
                "Jueves"
            )
        )
        viandas.add(
            MenuSemanal(
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
                "Viernes"
            )
        )
    }

}
