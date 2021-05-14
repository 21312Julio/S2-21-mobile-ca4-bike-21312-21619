package com.example.weather_application

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.station_newrow.view.*

class StationItem(val station: Stations): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        val name = station.name
        val status = station.status
        val available: Int = station.available_bike_stands
        val availableBikes: Int = station.available_bikes
        val latitude: Double = station.position.lat
        val longitude: Double = station.position.lng
        val title = "${name}: ${status}"
        val snippet = "Stands: ${available} | " +
                "Bikes: ${availableBikes}"
        viewHolder.itemView.textViewNameStation.text = title
        viewHolder.itemView.textViewAvailable.text = snippet
        viewHolder.itemView.button2.setOnClickListener {
            val intent = Intent(viewHolder.itemView.context, StationDetailsPage::class.java)
                intent.putExtra("name", name)
                intent.putExtra("status", status)
                intent.putExtra("available", available)
                intent.putExtra("availableBikes", availableBikes)
                intent.putExtra("latitude", latitude)
                intent.putExtra("longitude", longitude)
                intent.putExtra("title", title)
                intent.putExtra("snippet", snippet)
            viewHolder.itemView.context.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.station_newrow
    }

}