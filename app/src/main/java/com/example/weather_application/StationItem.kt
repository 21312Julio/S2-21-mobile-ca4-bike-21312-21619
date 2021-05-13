package com.example.weather_application

import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.station_newrow.view.*

class StationItem(val station: Stations): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        val name = station.name
        val status = station.status
        val available = station.available_bike_stands
        val availableBikes = station.available_bikes
        val title = "${name}: ${status}"
        val snippet = "Stands: ${available} | " +
                "Bikes: ${availableBikes}"
        viewHolder.itemView.textViewNameStation.text = title
        viewHolder.itemView.textViewAvailable.text = snippet
    }

    override fun getLayout(): Int {
        return R.layout.station_newrow
    }

}