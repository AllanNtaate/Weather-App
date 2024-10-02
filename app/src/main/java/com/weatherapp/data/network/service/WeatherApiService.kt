package com.weatherapp.data.network.service

import com.weatherapp.data.network.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * WeatherApiService defines the network API endpoints for fetching weather data.
 *
 * This interface uses Retrofit annotations to map HTTP requests to function calls.
 * The suspend functions indicate that these requests are asynchronous and can be called
 * from Kotlin Coroutines.
 */
interface WeatherApiService {

    /**
     * Fetch weather data by city name.
     *
     * This function performs a GET request to retrieve weather data for a specific city.
     * The city name is passed as a query parameter, and the API key is required for authentication.
     *
     * @param cityName the name of the city to fetch the weather for.
     * @param apiKey the API key for authenticating the request.
     * @return WeatherResponse containing the weather data for the specified city.
     */
    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String
    ): WeatherResponse

    /**
     * Fetch weather data by geographic coordinates (latitude and longitude).
     *
     * This function performs a GET request to retrieve weather data based on the specified
     * latitude and longitude coordinates. The API key is required for authentication.
     *
     * @param lat the latitude of the location.
     * @param lon the longitude of the location.
     * @param apiKey the API key for authenticating the request.
     * @return WeatherResponse containing the weather data for the specified coordinates.
     */
    @GET("weather")
    suspend fun getWeatherByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String
    ): WeatherResponse
}
