package com.example.appcliente.ui.home.menus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val recyclerView: RecyclerView? = activity?.findViewById(R.id.recyclear)
        if (recyclerView != null) {
            recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        val series= ArrayList<Menu>()
        series.add(Menu("Atypical", 3, R.drawable.serie1))
        series.add(Menu("You", 2, R.drawable.serie2))
        series.add(Menu("Stranger Things", 3, R.drawable.serie3))
        series.add(Menu("Riverdale", 3, R.drawable.serie4))
        recyclerView?.adapter=AdapterMenu(series)
    }
}
