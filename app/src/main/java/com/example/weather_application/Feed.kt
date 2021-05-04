package com.example.weather_application

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.feed_screen.*
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

class Feed : AppCompatActivity() {

    val CITY: String = "dublin,ie"
    val API: String = "44b71d1183d492c052c10a6f7ebf27dd"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feed_screen)

        WeatherTask().execute()
        verifyUserIsLoggedIn()


        imageButton_logOff.setOnClickListener {
            userSignOut()
        }

        buttonEditProfile.setOnClickListener {
            val intentProfile = Intent(this, Profile::class.java)
            startActivity(intentProfile)
        }

        button_ToParis.setOnClickListener {
            finish()
            val intentParis = Intent(this, FeedParis::class.java)
            intentParis.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intentParis)
        }
    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intentSplash = Intent(null, Splash::class.java)
            intentSplash.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intentSplash)
        }
    }

    private fun userSignOut() {
        FirebaseAuth.getInstance().signOut()
        val intentSplash = Intent(this, Splash::class.java)
        intentSplash.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intentSplash)
    }

    inner class WeatherTask() : AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            progressBar_feed.visibility = View.VISIBLE
            mainContainerFeed.visibility = View.GONE
            textView_errorText.visibility = View.GONE
        }

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

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                val address = jsonObj.getString("name")+","+sys.getString("country")
                val temp = main.getInt("temp")
                val weatherDescription = weather.getString("description")
                val tempMin = "Min Temp: "+main.getString("temp_min")+"°C"
                val tempMax = "Max Temp: "+main.getString("temp_max")+"°C"

                textView_address.text = address
                textView_temperature.text = "$temp°C"
                textView_description.text = weatherDescription.capitalize()
                textView_minTemperature.text = tempMin
                textView_maxTemperature.text = tempMax

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
