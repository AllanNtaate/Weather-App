package com.weatherapp.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.weatherapp.presentation.viewmodel.WeatherViewModel
import com.weatherapp.presentation.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels()

    private val requestLocationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                fetchCurrentLocationAndGetWeather()
            } else {
                // Handle permission denied case
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme(darkTheme = true) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    WeatherScreen(
                        weatherViewModel = weatherViewModel,
                        requestLocationPermissionLauncher = { permission ->
                            requestLocationPermissionLauncher.launch(permission)
                        },
                        context = this
                    )
                }
            }
        }
    }

    private fun fetchCurrentLocationAndGetWeather() {
        val lat = 12.34  // Example latitude
        val lon = 56.78  // Example longitude
        weatherViewModel.getWeatherForCurrentLocation(lat, lon)
    }
}
