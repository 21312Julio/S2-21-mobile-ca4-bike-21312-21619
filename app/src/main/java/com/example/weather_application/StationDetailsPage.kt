package com.example.weather_application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.stationdetails_screen.*

class StationDetailsPage : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stationdetails_screen)

        // Back to Station List
        bt_back5.setOnClickListener {
            finish()
        }

        // Add station to favorites Recycler View
        // NOT WORKING
        button3_addToFav.setOnClickListener {
            val intent = Intent(this, Favorites::class.java)
            intent.putExtra("name", textView_stationName.text)
            intent.putExtra("status", textView_stationsStatus.text)
            intent.putExtra("available", textView_bikeStands.text)
            intent.putExtra("availableBikes", textView_bikes.text)
            startActivity(intent)
        }

        // Assigning parameters from StationItem to textViews
        textView_stationName.text = intent.getStringExtra("name")
        textView_stationsStatus.text = intent.getStringExtra("status")
        textView_bikeStands.text = "AVAILABLE BIKE STANDS: " + intent.getIntExtra("available",0)
        textView_bikes.text = "AVAILABLE BIKES: " + intent.getIntExtra("availableBikes",0)
        textView_latitude.text = "LATITUDE: " + intent.getDoubleExtra("latitude",0.0)
        textView_longitude.text = "LONGITUDE: " + intent.getDoubleExtra("longitude",0.0)
    }

}





