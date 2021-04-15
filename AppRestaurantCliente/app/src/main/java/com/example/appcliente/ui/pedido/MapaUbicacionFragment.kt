package com.example.appcliente.ui.pedido

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.appcliente.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class MapaUbicacionFragment : SupportMapFragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private var FINE_LOCATION_ACCESS_REQUEST_CODE: Int = 10001

    private lateinit var mMap: GoogleMap
    private var googleApiClient: GoogleApiClient? = null
    private lateinit var database: DatabaseReference
    var vista: View? = null

    //Para guardar la Longitud y la Latitud
    var longitude = 0.0
    var latitude = 0.0

    var lat1 = ""
    var long2 = ""

    var ubi: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)

        getMapAsync(this)

        val ola = rootView?.findViewById<View>(R.id.btnConfirmarUbicacion)
        ola?.setOnClickListener {
            Toast.makeText(context, "Siiiiii :)", Toast.LENGTH_LONG).show()
        println("BOTON")}

        googleApiClient = GoogleApiClient.Builder(this.context as Activity)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        return rootView
    }

    override fun onMapReady(map: GoogleMap) {
        val trelew = LatLng(-43.254998, -65.3174879) //Mirador Cementerio

        mMap = map
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(trelew, 13.25f))

    }

    private fun enableUserLocation() {
        if (ContextCompat.checkSelfPermission(
                this.context as Activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            //Solicitar permiso
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this.context as Activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                //Necesitamos mostrarle al usuario un cuadro de diálogo para mostrar por qué se necesita el permiso y luego pedir el permiso ...
                ActivityCompat.requestPermissions(
                    this.context as Activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    FINE_LOCATION_ACCESS_REQUEST_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    this.context as Activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    FINE_LOCATION_ACCESS_REQUEST_CODE
                )
            }
        }

        val location: Location? = LocationServices.FusedLocationApi.getLastLocation(googleApiClient!!)
        if (location != null) {
            //Obtenemos la Longitut/Latitud
            longitude = location.longitude
            latitude = location.latitude

            lat1 = latitude.toString()
            long2 = longitude.toString()

            moveMap()
        }

        val database = FirebaseDatabase.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
        val formatedDate = formatter.format(date)

        val ltlg = mapOf(
            "clienteId" to user?.uid,
            "latitudd" to lat1 ,
            "longitudd" to long2,
            "timestamp" to formatedDate.toString()
        )

        val ltlgReference: DatabaseReference =
            database.reference.child("ltlg").push()

        ltlgReference.setValue(ltlg)

    }


    //Funcion para mover el mapa
    private fun moveMap() {
        //String para mostrar la Latitud y Longitud
        val msg = "Latitud: $latitude Longitud: $longitude"
        ubi = msg

        //Usamos un LatLng Object para guardar las Coordenadas
        val latLng = LatLng(latitude, longitude)

        //Añadimos un marker para el mapa
        mMap.addMarker(
            MarkerOptions()
                .position(latLng) //Posicion
                .draggable(true) //Para que se pueda mover
                .title("Esta es tu ubicacion")
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                mMap.isMyLocationEnabled = true
            }
        }
    }

    override fun onConnected(p0: Bundle?) {
        enableUserLocation()

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onStart() {
        super.onStart()
        googleApiClient?.connect()
    }

    override fun onStop() {
        googleApiClient!!.disconnect()
        super.onStop()
    }

}
