package com.example.appcliente.ui.pedido

import com.example.appcliente.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.appcliente.databinding.FragmentPedidoBinding
import com.example.appcliente.ui.cuenta.CuentaViewModel
import com.example.appcliente.ui.historial.HistorialViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.item_pedido.view.*


class PedidoFragment : Fragment() {

    private var _binding: FragmentPedidoBinding? = null
    private val binding get() = _binding!!
    var lista_pedidos: ArrayList<Pedido> = ArrayList()
    private lateinit var galleryViewModel: HistorialViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProviders.of(this).get(HistorialViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_historial, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        activity?.findViewById<TabLayout>(R.id.tabs)?.removeAllTabs() // remove all the tabs from the action bar and deselect the current tab
        return root
    }






    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //verificarPedidos()
        //rvPedido.layoutManager = LinearLayoutManager(this.context)
        //rvPedido.adapter = ResultadoAdapter(lista_pedidos)
        //binding.layoutPedido.visibility = View.INVISIBLE
        println(":..................")
        println("ENTRANDO AL PEDIDO CAPOO")
        println(":..................")
        val snackBar = Snackbar.make(
            requireActivity().findViewById(R.id.content),
            "Look at me, I'm a fancy snackbar", Snackbar.LENGTH_LONG
        )
        snackBar.show()
        println(":..................")
        println("saliendo")
        println(":..................")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun verificarPedidos(){
        FirebaseDatabase.getInstance().reference.child("Pedido")
            .orderByChild("clienteId")
            .equalTo(FirebaseAuth.getInstance().currentUser?.uid)
            .addValueEventListener(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.childrenCount > 0){
                        for (ds in p0.children) {
                            val pedido = ds.getValue(com.example.appcliente.ui.home.menudia.Pedido::class.java)!!

                        }
                    }

                }
            })
    }
}




class PedidoAdapter(val resultado: ArrayList<Pedido>) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return resultado.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pedido, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(resultado.get(position))
    }
}




class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val r= view.cardViewPedido.txtNombreViandaPedido
    fun bind(p: Pedido) {
        r.text = p.nombrePlato
    }
}


data class Pedido(
    var clienteId: String = "",
    var direccionEnvio: String = "",
    var estado:String = "",
    var platoId:String = "",
    var timestamp:String = "",
    var nombrePlato: String ="",
    var precioPlato:String=""
)
