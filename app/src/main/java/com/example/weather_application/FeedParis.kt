package com.example.weather_application

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.feed_screen.imageButton_logOff
import kotlinx.android.synthetic.main.feed_screen.imageView_weatherIcon
import kotlinx.android.synthetic.main.feed_screen.mainContainerFeed
import kotlinx.android.synthetic.main.feed_screen.progressBar_feed
import kotlinx.android.synthetic.main.feed_screen.textView_address
import kotlinx.android.synthetic.main.feed_screen.textView_description
import kotlinx.android.synthetic.main.feed_screen.textView_errorText
import kotlinx.android.synthetic.main.feed_screen.textView_maxTemperature
import kotlinx.android.synthetic.main.feed_screen.textView_minTemperature
import kotlinx.android.synthetic.main.feed_screen.textView_temperature
import kotlinx.android.synthetic.main.feed_screen_paris.*
import kotlinx.android.synthetic.main.feed_screen_paris.buttonEditProfile
import kotlinx.android.synthetic.main.feed_screen_paris.button_favorites
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

class FeedParis : AppCompatActivity() {

    // Desired city to get weather information from and API Key for permission
    val CITY: String = "lyon,fr"
    val API: String = "8ad632697414be0ea06cd0357fb775ba"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.feed_screen_paris)

        // Fill in weather information
        WeatherTask().execute()

        // Initialize find a bike MAPS intent
        button_findBikeNearYou2.setOnClickListener {
            val intentMaps = Intent(this, MapsForBikeParis::class.java)
            startActivity(intentMaps)
        }

        // Sign off and load splash page
        imageButton_logOff.setOnClickListener {
            userSignOut()
        }

        // Change to dublin weather information
        button_toDublin.setOnClickListener {
            finish()
            val intentDublin = Intent(this, Feed::class.java)
            startActivity(intentDublin)
        }

        // Open Profile Edit Page
        buttonEditProfile.setOnClickListener {
            val intentProfile = Intent(this, Profile::class.java)
            intentProfile.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intentProfile)
        }

        // Open List of Stations Page
        button_favorites.setOnClickListener {
            val intentFavorites = Intent(this, RecyclerStationsLyon::class.java)
            startActivity(intentFavorites)
        }
    }

    // Sign out of current user session and return to splash page
    private fun userSignOut() {
        FirebaseAuth.getInstance().signOut()
        val intentSplash = Intent(this, Splash::class.java)
        intentSplash.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intentSplash)
    }

    // Weather data retrieval class
    inner class WeatherTask() : AsyncTask<String, Void, String>() {

        // Only set XML visibility bar while tasks are executing in the background
        override fun onPreExecute() {
            progressBar_feed.visibility = View.VISIBLE
            mainContainerFeed.visibility = View.GONE
            textView_errorText.visibility = View.GONE
        }

        // Retrieving API data and formatting
        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(
                    Charsets.UTF_8
                )
            }catch (e: Exception){
                response = null
            }
            return response
        }

        // After retrieving the data access the JSON Object elements
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                // Accessing single elements and arrays inside JSON with given string names
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                val address = jsonObj.getString("name")+","+sys.getString("country")
                val temp = main.getInt("temp")
                val weatherDescription = weather.getString("description")
                val tempMin = "Min Temp: "+main.getString("temp_min")+"°C"
                val tempMax = "Max Temp: "+main.getString("temp_max")+"°C"

                // Setting textViews with retrieved information
                textView_address.text = address
                textView_temperature.text = "$temp°C"
                textView_description.text = weatherDescription.capitalize()
                textView_minTemperature.text = tempMin
                textView_maxTemperature.text = tempMax

                // Change weather icon base on returned weatherDescription on the API JSON
                if (weatherDescription == "clear sky") {
                    imageView_weatherIcon.setImageResource(R.drawable.sunny)
                }
                else if (weatherDescription == "few clouds" || weatherDescription == "broken clouds") {
                    imageView_weatherIcon.setImageResource(R.drawable.fewclouds)
                }
                else if (weatherDescription == "shower rain" || weatherDescription == "rain" || weatherDescription == "light rain") {
                    imageView_weatherIcon.setImageResource(R.drawable.showerain)
                }
                else if (weatherDescription == "snow") {
                    imageView_weatherIcon.setImageResource(R.drawable.snow)
                }
                else if (weatherDescription == "scattered clouds") {
                    imageView_weatherIcon.setImageResource(R.drawable.scatteredclouds)
                }
                else if (weatherDescription == "thunderstorm") {
                    imageView_weatherIcon.setImageResource(R.drawable.thunderstorm)
                }

                progressBar_feed.visibility = View.GONE
                mainContainerFeed.visibility = View.VISIBLE
            } catch (e: Exception) {
                progressBar_feed.visibility = View.GONE
                textView_errorText.visibility = View.VISIBLE
            }
        }

    }

}
