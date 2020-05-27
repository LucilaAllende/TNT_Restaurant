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


class MenuDiaFragment : Fragment() {

    var vista: View? = null

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

        val series= ArrayList<MenuDia>()
        series.add(
            MenuDia(
                "Hamburguesas de lenteja y brocoli",
                "½ cabeza de brócoli\n" +
                        "200 gr de lentejas cocidas\n" +
                        "1 cebolla\n" +
                        "2 ajos\n" +
                        "½ pimiento rojo",
                R.drawable.platovengano1,
                "Vegano"
            )
        )
        series.add(
            MenuDia(
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
                "Vegetariano"
            )
        )
        series.add(
            MenuDia(
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
                "Carnico"
            )
        )
        recyclerView?.adapter=
            AdapterMenuDia(series)
    }
}
