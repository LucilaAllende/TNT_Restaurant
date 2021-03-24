package com.example.appcliente.ui.seguimiento

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appcliente.R
import com.example.appcliente.ui.pedido.Pedido

class SeguimientoAdapter(val resultado: ArrayList<Pedido>) : RecyclerView.Adapter<SeguimientoViewHolder>() {

    override fun getItemCount(): Int {
        return resultado.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeguimientoViewHolder {
        return SeguimientoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.content_item_seguimiento, parent, false))
    }


    override fun onBindViewHolder(holder: SeguimientoViewHolder, position: Int) {
        holder.bind(resultado.get(position), resultado.size)
    }
}