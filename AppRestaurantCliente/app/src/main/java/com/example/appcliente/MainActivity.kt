package com.example.appcliente

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.appcliente.ui.home.menudia.DetallesPlatoFragment
import com.example.appcliente.ui.home.menudia.MenuDiaFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

//, IComunicaFragments agregar para que implemente esta interface
class MainActivity() : AppCompatActivity(){

    private lateinit var appBarConfiguration: AppBarConfiguration

    var menuFragment: MenuDiaFragment? = null
    var detalleFragment: DetallesPlatoFragment ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_cuenta, R.id.nav_pedido, R.id.nav_historial), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

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
        return super.onOptionsItemSelected(item);
    }

/*    override fun enviarMenuDia(menuDia: PlatoDia){
        detalleFragment = this.supportFragmentManager
            .findFragmentById(R.id.nav_detalles_plato) as DetallesPlatoFragment?
        if (detalleFragment != null && findViewById<View?>(R.id.activity_main) == null) {
            detalleFragment?.asignarInformacion(menuDia)
        } else {
            detalleFragment = DetallesPlatoFragment()
            val bundleEnvio = Bundle()
            bundleEnvio.putSerializable("objeto", menuDia)
            detalleFragment!!.arguments = bundleEnvio

            //cargamos el fragment en el Activity
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main, detalleFragment!!).addToBackStack(null).commit()
        }
    }*/
}
