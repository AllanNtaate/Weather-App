package com.weatherapp.data.network.repo

import com.weatherapp.data.network.model.WeatherResponse
import com.weatherapp.data.network.service.WeatherApiService
import com.weatherapp.domain.utilities.Const.Companion.API_KEY
import javax.inject.Inject

/**
 * WeatherRepository handles the data operations related to weather information.
 *
 * This repository acts as an intermediary between the data source (WeatherApiService)
 * and the rest of the application. It provides methods to fetch weather data by city name
 * or by geographic coordinates, using the API service.
 *
 * @param apiService The WeatherApiService used for making network requests.
 */
class WeatherRepository @Inject constructor(private val apiService: WeatherApiService) {

    /**
     * Fetches weather data for a given city name.
     *
     * This method interacts with the WeatherApiService to retrieve weather information
     * based on the provided city name. The API key is included in the request for authentication.
     *
     * @param cityName the name of the city to fetch weather data for.
     * @return WeatherResponse containing the weather data for the specified city.
     */
    suspend fun getWeatherByCity(cityName: String): WeatherResponse {
        return apiService.getWeatherByCity(cityName, API_KEY)
    }

    /**
     * Fetches weather data based on geographic coordinates (latitude and longitude).
     *
     * This method interacts with the WeatherApiService to retrieve weather information
     * based on the provided latitude and longitude. The API key is included in the request
     * for authentication.
     *
     * @param lat the latitude of the location.
     * @param lon the longitude of the location.
     * @return WeatherResponse containing the weather data for the specified coordinates.
     */
    suspend fun getWeatherByCoordinates(lat: Double, lon: Double): WeatherResponse {
        return apiService.getWeatherByCoordinates(lat, lon, API_KEY)
    }
}
