package com.example.weather_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.screen_favorites.*

class Favorites : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_favorites)

        bt_back6.setOnClickListener {
            finish()
        }

        val adapter = GroupAdapter<ViewHolder>()

        val name = intent.getStringExtra("name")
        val address = intent.getStringExtra("name")
        val status = intent.getStringExtra("status")
        val bikeStands = intent.getIntExtra("available", 0)
        val bikes = intent.getIntExtra("availableBikes", 0)
        val position = Position(0.0, 0.0)


        val station = Stations(name.toString(), address.toString(), position, 0, bikeStands, bikes, status.toString())

        adapter.add(StationItem(station))
        recyclerView_favorites.adapter = adapter

    }
}