package com.example.appcliente.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.appcliente.FragmentPortada
import com.example.appcliente.MainActivity
import com.example.appcliente.R
import com.example.appcliente.ui.pedido.Pedido
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_track.*

const val EXTRA_MESSAGE = "com.example.appcliente.MESSAGE"

class TrackActivity : AppCompatActivity() {
    var view_order_placed: View? = null
    var view_order_confirmed: View? = null
    var view_order_processed: View? = null
    var view_order_pickup: View? = null
    var con_divider: View? = null
    var ready_divider: View? = null
    var placed_divider: View? = null
    var img_orderconfirmed: ImageView? = null
    var orderprocessed: ImageView? = null
    var orderpickup: ImageView? = null
    var textorderpickup: TextView? = null
    var text_confirmed: TextView? = null
    var textorderprocessed: TextView? = null
    var horaPedido: TextView? = null
    var cantPedidos : TextView? = null

    lateinit var pedidoIdSeguimiento : String

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)
        view_order_placed = findViewById(R.id.view_order_placed)
        //view_order_confirmed = findViewById(R.id.view_order_confirmed)
        view_order_processed = findViewById(R.id.view_order_processed)
        view_order_pickup = findViewById(R.id.view_order_pickup)
        //placed_divider = findViewById(R.id.placed_divider)
        con_divider = findViewById(R.id.con_divider)
        ready_divider = findViewById(R.id.ready_divider)
        textorderpickup = findViewById(R.id.textorderpickup)
        //text_confirmed = findViewById(R.id.text_confirmed)
        textorderprocessed = findViewById(R.id.textorderprocessed)
        //img_orderconfirmed = findViewById(R.id.img_orderconfirmed)
        orderprocessed = findViewById(R.id.orderprocessed)
        orderpickup = findViewById(R.id.orderpickup)
        horaPedido = findViewById(R.id.textViewSeguimientoHoraPedido2)
        val intent = intent
        val orderStatus = intent.getStringExtra("orderStatus")
        pedidoIdSeguimiento = intent.getStringExtra("pedidoIdSeguimiento")
        val h = intent.getStringExtra("horaPedidoSeguimiento")
        val extra = h.split(" ")
        verificarPedidos()
        getOrderStatus(orderStatus)
        println("DEVOLVER $extra")
        horaPedido!!.text = extra[3]
        cantPedidos = findViewById(R.id.textViewNumeroOrdenSeguimiento)
        //cantPedidos!!.text = "#"+ intent.getStringExtra("cantPedidosSeguimiento")
        title = "Detalle del Pedido"
        //appBar.setBackgroundColor(R.color.primaryColor)
        //toolbar.setBackgroundColor(R.color.primaryColor)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut()
                /* TODO Como navegar de una actividad a un fragmento
                val fragmento: Fragment = FragmentPortada()
                supportFragmentManager.beginTransaction().replace(R.id.activity_track, fragmento)
                    .commit()*/
                val message = 1
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra(EXTRA_MESSAGE, message)
                }
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getOrderStatus(orderStatus: String) {
        println("REFRESANDOOs")
        if (orderStatus == "0") {
            val alfa = 0.5.toFloat()
            setStatus(alfa)
        } else if (orderStatus == "1") {
            val alfa = 0.5.toFloat()
            setStatus1(alfa)
        } else if (orderStatus == "2") {
            val alfa = 0.5.toFloat()
            setStatus2(alfa)
        }
    }


    private fun setStatus(alfa: Float) {
        val myf = 0.5.toFloat()

        //ORDEN 1
        view_order_placed!!.background = resources.getDrawable(R.drawable.shape_status_completed)
        orderplaced!!.alpha = 1.toFloat()
        textorderplaced!!.alpha = 1.toFloat()

        //ORDEN 2
        view_order_processed!!.alpha = alfa
        view_order_processed!!.background = resources.getDrawable(R.drawable.shape_status_current) //punto
        con_divider!!.alpha = alfa
        con_divider!!.background = resources.getDrawable(R.drawable.shape_status_current)
        orderprocessed!!.alpha = alfa
        textorderprocessed!!.alpha = myf

        //ORDEN 3
        view_order_pickup!!.alpha = alfa
        view_order_pickup!!.background = resources.getDrawable(R.drawable.shape_status_current)
        ready_divider!!.alpha = alfa
        ready_divider!!.background = resources.getDrawable(R.drawable.shape_status_current)
        orderpickup!!.alpha = alfa
        textorderpickup!!.alpha = myf
    }


     private fun setStatus1(alfa: Float) {
        val myf = 0.5.toFloat()
         //ORDEN 1
         view_order_placed!!.alpha = alfa
         view_order_placed!!.background = resources.getDrawable(R.drawable.shape_status_current) //punto
         con_divider!!.alpha = alfa
         con_divider!!.background = resources.getDrawable(R.drawable.shape_status_current)
         orderplaced!!.alpha = alfa
         textorderplaced!!.alpha = myf

         //ORDEN 2
         view_order_processed!!.background = resources.getDrawable(R.drawable.shape_status_completed)
         orderprocessed!!.alpha = 1.toFloat()
         textorderprocessed!!.alpha = 1.toFloat()

         //ORDEN 3
         view_order_pickup!!.alpha = alfa
         view_order_pickup!!.background = resources.getDrawable(R.drawable.shape_status_current)
         ready_divider!!.alpha = alfa
         ready_divider!!.background = resources.getDrawable(R.drawable.shape_status_current)
         orderpickup!!.alpha = alfa
         textorderpickup!!.alpha = myf
    }

    private fun setStatus2(alfa: Float) {
        val myf = 0.5.toFloat()
        //ORDEN 1
        view_order_placed!!.alpha = alfa
        view_order_placed!!.background = resources.getDrawable(R.drawable.shape_status_current) //punto
        con_divider!!.alpha = alfa
        con_divider!!.background = resources.getDrawable(R.drawable.shape_status_current)
        orderplaced!!.alpha = alfa
        textorderplaced!!.alpha = myf

        //ORDEN 2
        view_order_processed!!.alpha = alfa
        view_order_processed!!.background = resources.getDrawable(R.drawable.shape_status_current) //punto
        con_divider!!.alpha = alfa
        con_divider!!.background = resources.getDrawable(R.drawable.shape_status_current)
        orderprocessed!!.alpha = alfa
        textorderprocessed!!.alpha = myf

        //ORDEN 3
        view_order_pickup!!.background = resources.getDrawable(R.drawable.shape_status_completed)
        ready_divider!!.alpha = alfa
        ready_divider!!.background = resources.getDrawable(R.drawable.shape_status_current)
        orderpickup!!.alpha = 1.toFloat()
        textorderpickup!!.alpha = 1.toFloat()

    }



    private fun verificarPedidos(){
        FirebaseDatabase.getInstance().reference.child("Pedido")
            .orderByChild("clienteId")
            .equalTo(FirebaseAuth.getInstance().currentUser?.uid)
            .addChildEventListener(object : ChildEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    println("cancelado")
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    println("movido")
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    println("pass")
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    println("borrando")
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    if (p0.childrenCount > 0) {
                        if (pedidoIdSeguimiento == p0.key.toString()) {
                            val p = p0.getValue(Pedido::class.java)!!
                            var orderStatus = "0"

                            if (p.estado == "EN PREPARACION") { //menuDia
                                orderStatus = "1"
                            } else if (p.estado == "PEDIDO ENVIADO") { //menuMate
                                orderStatus = "2"
                            }
                            getOrderStatus(orderStatus)
                        }
                    }
                }
            })
        }

}
