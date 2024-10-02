package com.weatherapp.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weatherapp.data.network.local.SharedPreferencesManager
import com.weatherapp.data.network.model.WeatherResponse
import com.weatherapp.data.network.repo.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    var weatherResponse = mutableStateOf<WeatherResponse?>(null)
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    init {
        // Load the last searched city
        loadLastSearchedCity()
    }

    private fun loadLastSearchedCity() {
        sharedPreferencesManager.getLastSearchedCity()?.let {
            getWeatherByCity(it)
        }
    }

    fun getWeatherByCity(cityName: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                weatherResponse.value = repository.getWeatherByCity(cityName)
                errorMessage.value = null // Clear any previous error message on success
                sharedPreferencesManager.saveLastSearchedCity(cityName) // Save the last searched city
            } catch (e: Exception) {
                errorMessage.value = "Failed to fetch weather data." // Set error message on failure
            } finally {
                isLoading.value = false // Always set loading to false after the operation
            }
        }
    }

    fun getWeatherForCurrentLocation(lat: Double, lon: Double) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                weatherResponse.value = repository.getWeatherByCoordinates(lat, lon)
                errorMessage.value = null // Clear any previous error message on success
            } catch (e: Exception) {
                errorMessage.value = "Failed to fetch weather data for location." // Set error message on failure
            } finally {
                isLoading.value = false // Always set loading to false after the operation
            }
        }
    }
}
