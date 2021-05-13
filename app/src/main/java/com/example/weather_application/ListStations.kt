package com.example.weather_application

// Stations class with variables types of data provided by JSON String
class Stations(val name: String, val address: String, val position: Position,
               val bike_stands: Int, val available_bike_stands: Int, val available_bikes: Int, val status: String, )
class Position(val lat: Double, val lng: Double)
