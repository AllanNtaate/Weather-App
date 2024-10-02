package com.weatherapp.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * WeatherApp is the base class for maintaining global application state.
 *
 * This class extends the Android Application class and serves as the entry
 * point for the application. It is also used to set up Hilt for dependency injection.
 *
 * The @HiltAndroidApp annotation triggers Hilt's code generation, including a base
 * class for the application that serves as the application-level dependency container.
 * This allows Hilt to automatically manage dependency injection for the entire application.
 */
@HiltAndroidApp
class WeatherApp : Application() {
    // Application-level setup can be done here if needed
}
