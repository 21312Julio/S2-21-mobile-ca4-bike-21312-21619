package com.example.weather_application

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class MapsForBikeParis : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private val TAG = MapsForBike::class.java.simpleName
    private val REQUEST_LOCATION_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.maps_screen_paris)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        backgroundTask().execute()
    }

    private fun setMapStyle(googleMap: GoogleMap) {
        map = googleMap

        try {
            val success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this,
                            R.raw.map_style
                    )
            )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }

    private fun bitmapDescriptorFromVector(context: Context, @DrawableRes vectorDrawableResourceId: Int): BitmapDescriptor? {
        val background = ContextCompat.getDrawable(context, R.drawable.ic_baseline_directions_bike_24)
        background!!.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)
        val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)
        vectorDrawable!!.setBounds(40, 20, vectorDrawable.intrinsicWidth + 40, vectorDrawable.intrinsicHeight + 20)
        val bitmap = Bitmap.createBitmap(background.intrinsicWidth, background.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        background.draw(canvas)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return
            }
            map.isMyLocationEnabled = true
        }
        else {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
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

        setMapStyle(map)

        // Add a marker in Lyon,France and move the camera
        val lyon = LatLng(45.75, 4.85)
        val zoomLevel = 15f
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(lyon, zoomLevel))

        enableMyLocation()
    }

    inner class backgroundTask(): AsyncTask<Void, Void, List<Stations>>() {

        val apiKey = "e7a1f66f33e297827e7ec779b6cee6dd00ae76fb"
        val city = "lyon"
        val baseUrl = "https://api.jcdecaux.com/vls/v1/stations?contract=${city}&apiKey=${apiKey}"
        val client = OkHttpClient()

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Void?): List<Stations> {
            val request = Request.Builder().url(baseUrl).build()

            client.newCall(request).execute().use {
                if (!it.isSuccessful) throw IOException("Unexpected Error $request")

                val body = it.body?.string()
                val gson = GsonBuilder().create()
                val type = object : TypeToken<List<Stations>>(){}.type
                val elements: List<Stations> = gson.fromJson(body, type)

                return elements
            }
        }

        override fun onPostExecute(result: List<Stations>?) {
            super.onPostExecute(result)
            result?.forEach {
                val latitude = it.position.lat
                val longitude = it.position.lng
                val name = it.name
                val status = it.status
                val available = it.available_bike_stands
                val availableBikes = it.available_bikes
                val latLng = LatLng(latitude, longitude)
                val title = "${name}: ${status}"
                val snippet = "Available Stands: ${available} | " +
                        "Available Bikes: ${availableBikes}"
                map.addMarker(MarkerOptions().position(latLng).title(title).snippet(snippet)).setIcon(
                        bitmapDescriptorFromVector(applicationContext, R.drawable.ic_baseline_directions_bike_24)
                )
            }
        }

    }
}

