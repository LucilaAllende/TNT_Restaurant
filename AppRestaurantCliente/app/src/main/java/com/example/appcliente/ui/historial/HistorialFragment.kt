package com.example.appcliente.ui.historial

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.*
import androidx.room.Query
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_historial.*
import kotlinx.android.synthetic.main.item_pedido.view.*
import kotlinx.coroutines.CoroutineScope
import com.example.appcliente.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.launch


class HistorialFragment : Fragment() {

    private lateinit var slideshowViewModel: HistorialViewModel
    var vista: View? = null
    private var viewPager: ViewPager? = null
    var lista_pedidos: ArrayList<Pedido> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
            ViewModelProviders.of(this).get(HistorialViewModel::class.java)
        vista = inflater.inflate(R.layout.fragment_historial, container, false)
        activity?.findViewById<TabLayout>(R.id.tabs)
            ?.removeAllTabs() // remove all the tabs from the action bar and deselect the current tab
        verificarPedidos()
        return vista

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rvPedido.layoutManager = LinearLayoutManager(this.context)
        rvPedido.adapter = PedidoAdapter(lista_pedidos)

    }

    private fun verificarPedidos() {
        FirebaseDatabase.getInstance().reference.child("Historial")
            .orderByChild("clienteId")
            .equalTo(FirebaseAuth.getInstance().currentUser?.uid)
            .addChildEventListener(object : ChildEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    println("cancelado")
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    println("movido")
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    /*
                    if (p0.childrenCount > 0) {
                        val pedido = p0.getValue(Pedido::class.java)!!
                        pedido.id = p0.key.toString()
                        lista_pedidos.add(pedido)
                        if (rvPedido != null) {
                            rvPedido.adapter!!.notifyDataSetChanged()
                        }
                    } else {
                        if (rvPedido != null) {
                            rvPedido.adapter!!.notifyDataSetChanged()
                        }
                        mostrarSnackbar("No hay pedidos aun.")
                    }
                    */
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    if (p0.childrenCount > 0) {
                        val pedido = p0.getValue(Pedido::class.java)!!
                        pedido.id = p0.key.toString()
                        lista_pedidos.add(pedido)
                        if (rvPedido != null) {
                            rvPedido.adapter!!.notifyDataSetChanged()
                        }
                    } else {
                        if (rvPedido != null) {
                            rvPedido.adapter!!.notifyDataSetChanged()
                        }
                        mostrarSnackbar("No hay pedidos aun.")
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot) {

                    if (rvPedido != null) {
                        val pedido = p0.getValue(Pedido::class.java)!!
                        pedido.id = p0.key.toString()
                        val pos = lista_pedidos.indexOf(pedido)
                        lista_pedidos.remove(pedido)
                        if (rvPedido != null) {
                            rvPedido.adapter?.notifyItemRemoved(pos)
                            rvPedido.adapter?.notifyDataSetChanged()
                            if (lista_pedidos.size == 0) {
                                mostrarSnackbar("No hay pedidos aun.")
                            }
                        }
                    }
                }
            })
    }

    fun mostrarSnackbar(mensaje: String) {
        val snackBar = activity?.findViewById<TabLayout>(R.id.tabs)?.let {
            Snackbar.make(
                it,
                mensaje, Snackbar.LENGTH_LONG
            )
        }
        if (snackBar != null) {
            snackBar.show()
        }
    }


}

    private fun eliminarPedido(idPedido: String) {
        FirebaseDatabase.getInstance().reference.child("Historial/" + idPedido).removeValue()
    }



    class PedidoAdapter(val resultado: ArrayList<Pedido>) : RecyclerView.Adapter<ViewHolder>() {

        override fun getItemCount(): Int {
            return resultado.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var vh = ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_pedido,
                    parent,
                    false
                )
            )
            return vh
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(resultado.get(position))
        }
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre = view.cardViewPedido.txtNombreViandaPedido
        //val txtPrecio = view.cardViewPedido.txtPrecioPedido
        val color = view.bannerColor
        val platoId = view.textViewPlatoIdPedido
        val btnNotificar = view.btnNotificarPedido
        //val btnEliminarPedido = view.btnEliminarPedido

        fun bind(p: Pedido) {
            if (p.tipo == "md") { //menuDia
                color.setBackgroundColor(Color.parseColor("#F87652")) //menu dia
            } else if (p.tipo == "mm") { //menuMate
                color.setBackgroundColor(Color.parseColor("#C3223A"))
            } else {
                color.setBackgroundColor(Color.parseColor("#757575"))
            }

            if (p.notificarme){
                btnNotificar.text = "Eliminar Suscripcion"
                btnNotificar.setTextColor(Color.parseColor("#ff0000"))
            }
            else{
                btnNotificar.text = "Suscribirme"
                btnNotificar.setTextColor(Color.parseColor("#484848"))
            }

            color.text = p.timestamp
            txtNombre.text = p.nombrePlato
            //txtPrecio.text = p.precioPlato
            platoId.text = p.platoId
            btnNotificar.setOnClickListener {
                if (btnNotificar.text.toString().contains("Suscribirme", ignoreCase = true)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(p.nombrePlato.replace(" ","_"))
                        .addOnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                println("error ")
                            }
                            btnNotificar.text = "Eliminar Suscripcion"
                            btnNotificar.setTextColor(Color.parseColor("#ff0000"))
                            FirebaseDatabase.getInstance().reference.child("Historial").child(p.id).child("notificarme").setValue(true)
                        }
                }
                else{
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(p.nombrePlato.replace(" ","_"))
                        .addOnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                println("error ")
                            }
                            btnNotificar.text = "Suscribirme"
                            btnNotificar.setTextColor(Color.parseColor("#484848"))
                            FirebaseDatabase.getInstance().reference.child("Historial").child(p.id).child("notificarme").setValue(false)
                        }
                }
            }
        }
    }




    data class Pedido(
        var id: String = "",
        var clienteId: String = "",
        var direccionEnvio: String = "",
        var estado: String = "",
        var platoId: String = "",
        var timestamp: String = "",
        var nombrePlato: String = "",
        var precioPlato: String = "",
        var tipo: String = "",
        var notificarme: Boolean = false
    )

    //ROOM para notificaciones
    @Entity(tableName = "notificaciones")
    data class Notificacion(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,

        @ColumnInfo(name = "platoId")
        var platoId: String,

        @ColumnInfo(name = "usuarioId")
        var usuarioId: String,

        @ColumnInfo(name = "nombrePlato")
        var nombrePlato: String


    )

    //Creamos el DAO
    @Dao
    interface notificacionDAO {
        @Query("SELECT * from notificaciones ORDER BY id ASC")
        suspend fun getPlatos(): List<Notificacion>

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insert(notificacion: Notificacion)

        @Query("select * from notificaciones where nombrePlato = :nombre_plato and usuarioId = :usuario_id")
        fun getNotificacion(nombre_plato: String, usuario_id: String): LiveData<List<Notificacion>>

        @Query("delete from notificaciones where nombrePlato = :nombre_plato and usuarioId = :usuario_id")
        suspend fun borrarNotificacion(nombre_plato: String, usuario_id: String)

        @Query("DELETE FROM notificaciones")
        suspend fun deleteAll()

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertar(notificacion: Notificacion)

        @Query("select * from notificaciones")
        fun getAll(): LiveData<List<Notificacion>>
    }

    //Creamos la BD
    @Database(entities = arrayOf(Notificacion::class), version = 1)
    abstract class NotificacionesRoomDatabase : RoomDatabase() {
        abstract fun notificacionDao(): notificacionDAO

        companion object {
            @Volatile
            private var INSTANCIA: NotificacionesRoomDatabase? = null

            fun obtenerDatabase(context: Context): NotificacionesRoomDatabase {
                val instanciaTemporal = INSTANCIA
                if (instanciaTemporal != null) {
                    return instanciaTemporal
                }
                synchronized(this) {
                    val instancia = Room.databaseBuilder(
                        context.applicationContext,
                        NotificacionesRoomDatabase::class.java,
                        "notificaciones_database"
                    )
                        //.addCallback(PartidoDatabaseCallback(viewModelScope))
                        .build()
                    INSTANCIA = instancia
                    return instancia
                }
            }


            private class PartidoDatabaseCallback(private val scope: CoroutineScope) :
                RoomDatabase.Callback() {
                /* importante: destacar el scope como parametro */

                /**
                 * Lo que hacemos en esta clase es sobreescribir el método onOpen
                 * para cargar la base de datos.
                 *
                 */
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    INSTANCIA?.let { database ->
                        scope.launch {
                            cargarBaseDeDatos(database.notificacionDao())
                        }
                    }
                }

                suspend fun cargarBaseDeDatos(notificacionDAO: notificacionDAO) {

                    notificacionDAO.insert(
                        Notificacion(
                            1,
                            "idunplato",
                            "UsuarioID",
                            "carneConPapas"
                        )
                    )

                }
            }

        }
    }

data class Usuario(
    var apellido: String ="",
    var email: String = "",
    var nombre: String ="",
    var localidad: String = "",
    var calle: String = ""
)

