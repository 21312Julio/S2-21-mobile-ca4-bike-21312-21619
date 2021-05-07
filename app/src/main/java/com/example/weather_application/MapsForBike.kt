package com.example.weather_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.concurrent.ArrayBlockingQueue

class MapsForBike() : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.maps_screen)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Add a marker in Dublin and move the camera
        val dublin = LatLng(53.3497645, -6.2602732)
        val zoomLevel = 15f
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(dublin, zoomLevel))

        // Trying to add Marker onto the map by using a forEach loop in my requestHandler list response
        // NOT WORKING *******************
        val stations = fetchJson {
            it.forEach {
                val latitude = it.position.lat
                val longitude = it.position.lng
                val latLng = LatLng(latitude, longitude)
                map.addMarker(MarkerOptions().position(latLng))
            }
        }

    }


}
