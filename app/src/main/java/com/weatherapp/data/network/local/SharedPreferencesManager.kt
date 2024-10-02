package com.weatherapp.data.network.local

import android.content.Context

class SharedPreferencesManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("weather_app", Context.MODE_PRIVATE)

    fun saveLastSearchedCity(cityName: String) {
        with(sharedPreferences.edit()) {
            putString("last_city", cityName)
            apply()
        }
    }

    fun getLastSearchedCity(): String? {
        return sharedPreferences.getString("last_city", null)
    }
}
