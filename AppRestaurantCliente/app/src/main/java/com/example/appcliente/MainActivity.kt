package com.example.appcliente

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.appcliente.geofence.MapsActivity
import com.example.appcliente.ui.EXTRA_MESSAGE
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() { //, OnMapReadyCallback

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout:DrawerLayout

    var lonPublica = 0.0

    private val TAG = "MainActivity"

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreateAcPr: ESTOY CREANDO MAIN ACTIVITY")

        try{
            val database = FirebaseDatabase.getInstance()
            database.setPersistenceEnabled(true)
            val pedido = database.getReference("Pedido")
            pedido.keepSynced(true)
            val historial = database.getReference("Historial")
            historial.keepSynced(true)
        }
        catch (e: Exception){
            println("error")
        }

        setContentView(R.layout.activity_main)

        //val mapFragment = supportFragmentManager.findFragmentById(R.id.fragmentoMapa) as SupportMapFragment
        //mapFragment.getMapAsync(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val tolBar: Toolbar? = findViewById<View>(R.id.toolbar) as? Toolbar
        tolBar?.visibility = View.VISIBLE

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_seguimiento, R.id.nav_pedido, R.id.nav_historial), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //val intent2 = Intent(this, MapsActivity::class.java)
        //startActivity(intent2)

        val message = (intent.getStringExtra(EXTRA_MESSAGE))?.toInt()
        if (message == 1){
            findNavController(R.id.nav_host_fragment).navigate(R.id.fragmentInicio)
        }
    }

    /*override fun onMapReady(googleMap: GoogleMap) {
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(0.0, 0.0))
                .title("Marker")
        )
    }*/

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut()
                findNavController(R.id.nav_host_fragment).navigate(R.id.fragmentInicio)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
        //finish()
    }
}
