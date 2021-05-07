package com.example.weather_application

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException

// Fetching the JSON data from the JCDecaux API and trying to return with resultHandler
fun fetchJson(resultHandler: (List<Stations>) -> Unit) {
    val apiKey = "e7a1f66f33e297827e7ec779b6cee6dd00ae76fb"
    val city = "dublin"
    val baseUrl = "https://api.jcdecaux.com/vls/v1/stations?contract=${city}&apiKey=${apiKey}"

    // Building OKHTTP request
    val request = Request.Builder().url(baseUrl).build()

    val client = OkHttpClient()
    // Using client to return information in the https website
    client.newCall(request).enqueue(object: Callback{
        override fun onFailure(call: Call, e: IOException) {
        }

        override fun onResponse(call: Call, response: Response) {
            // JSON Values Returned!
            Log.d("JSON", "Data OK")
            // Returning the JSON in String
            val body = response.body?.string()
            // Parsing the JSON to a List of Station objects by using GSON and TypeToken
            val gson = GsonBuilder().create()
            val type = object : TypeToken<List<Stations>>(){}.type
            val elements: List<Stations> = gson.fromJson(body, type)
            resultHandler(elements)
        }
    })

}

// Stations class with variables types of data provided by JSON String
class Stations(val name: String, val address: String, val position: Position,
                val bike_stands: Int, val available_bike_stands: Int, val available_bikes: Int, val status: String, )
class Position(val lat: Double, val lng: Double)
