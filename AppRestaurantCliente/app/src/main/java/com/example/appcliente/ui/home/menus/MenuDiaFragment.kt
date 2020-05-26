package com.example.appcliente.ui.home.menus

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appcliente.R
import com.example.appcliente.ui.home.AdapterMenu
import com.example.appcliente.ui.home.Menu


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
        val textview:TextView? = activity?.findViewById(R.id.txtTitle)
        textview?.setBackgroundColor(R.color.accentColor)
        val series= ArrayList<Menu>()
        series.add(Menu("Atypical", 3, R.drawable.serie1, "Vegano"))
        series.add(Menu("You", 2, R.drawable.serie2, "Carnico"))
        series.add(Menu("Stranger Things", 3, R.drawable.serie3, "Vegetariano"))
        series.add(Menu("Riverdale", 3, R.drawable.serie4, "Vegano"))
        recyclerView?.adapter=AdapterMenu(series)
    }
}
