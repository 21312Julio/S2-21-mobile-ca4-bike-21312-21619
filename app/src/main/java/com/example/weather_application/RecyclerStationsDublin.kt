package com.example.weather_application

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.favorites_screen.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class RecyclerStationsDublin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorites_screen)

        backgroundTask().execute()

        bt_back4.setOnClickListener {
            finish()
        }

        button_toFavoritesList.setOnClickListener {
            val intent = Intent(this, Favorites::class.java)
            startActivity(intent)
        }
    }

    inner class backgroundTask(): AsyncTask<Void, Void, List<Stations>>() {

        val apiKey = "e7a1f66f33e297827e7ec779b6cee6dd00ae76fb"
        val city = "dublin"
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
            val adapter = GroupAdapter<ViewHolder>()
            result?.forEach {
                if (it != null) {
                    adapter.add(StationItem(it))
                }
            }

            recyclerView_stations.adapter = adapter
        }

    }
}


