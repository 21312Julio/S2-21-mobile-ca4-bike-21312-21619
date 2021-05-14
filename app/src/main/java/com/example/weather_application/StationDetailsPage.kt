package com.example.weather_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.stationdetails_screen.*

class StationDetailsPage : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stationdetails_screen)

        bt_back5.setOnClickListener {
            finish()
        }

        textView_stationName.text = intent.getStringExtra("name")
        textView_stationsStatus.text = intent.getStringExtra("status")
        textView_bikeStands.text = "AVAILABLE BIKE STANDS: " + intent.getIntExtra("available",0)
        textView_bikes.text = "AVAILABLE BIKES: " + intent.getIntExtra("availableBikes",0)
        textView_latitude.text = "LATITUDE: " + intent.getDoubleExtra("latitude",0.0)
        textView_longitude.text = "LONGITUDE: " + intent.getDoubleExtra("longitude",0.0)
    }

}





