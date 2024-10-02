package com.weatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.weatherapp.data.network.local.SharedPreferencesManager
import com.weatherapp.data.network.model.Clouds
import com.weatherapp.data.network.model.Coord
import com.weatherapp.data.network.model.Main
import com.weatherapp.data.network.model.Sys
import com.weatherapp.data.network.model.Weather
import com.weatherapp.data.network.model.WeatherResponse
import com.weatherapp.data.network.model.Wind
import com.weatherapp.data.network.repo.WeatherRepository
import com.weatherapp.presentation.viewmodel.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: WeatherViewModel
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        weatherRepository = mock(WeatherRepository::class.java)
        sharedPreferencesManager = mock(SharedPreferencesManager::class.java)
        viewModel = WeatherViewModel(weatherRepository,sharedPreferencesManager)
        Dispatchers.setMain(testDispatcher)
    }

    // Dummy data for new york city weather

    private val newYorkWeatherResponse = WeatherResponse(
        coord = Coord(lon = -74.0060, lat = 40.7128),
        weather = listOf(
            Weather(
                id = 800,
                main = "Clear",
                description = "clear sky",
                icon = "01d"
            )
        ),
        base = "stations",
        main = Main(
            temp = 25.3,
            feels_like = 24.0,
            temp_min = 22.0,
            temp_max = 27.0,
            pressure = 1013,
            humidity = 60
        ),
        visibility = 10000,
        wind = Wind(
            speed = 5.0,
            deg = 240
        ),
        clouds = Clouds(
            all = 1
        ),
        dt = 1633072800,
        sys = Sys(
            type = 1,
            id = 4610,
            country = "US",
            sunrise = 1633072800,
            sunset = 1633114800
        ),
        timezone = -14400,
        id = 5128581,
        name = "New York",
        cod = 200,
        message = null
    )

    @Test
    fun `getWeatherByCity should update weatherResponse on success`() = runTest {
        // Arrange
        val mockWeatherResponse = newYorkWeatherResponse
        `when`(weatherRepository.getWeatherByCity("newYork")).thenReturn(mockWeatherResponse)

        // Act
        viewModel.getWeatherByCity("newYork")
        testDispatcher.scheduler.advanceUntilIdle() // For coroutine execution

        // Assert
        assertEquals(mockWeatherResponse, viewModel.weatherResponse.value)
        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun `getWeatherByCity should update errorMessage on failure`() = runTest {
        // Arrange
        `when`(weatherRepository.getWeatherByCity("newYork")).thenThrow(RuntimeException("Network Error"))

        // Act
        viewModel.getWeatherByCity("newYork")
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals("Failed to fetch weather data.", viewModel.errorMessage.value)
        assertNull(viewModel.weatherResponse.value)
    }

    @Test
    fun `getWeatherForCurrentLocation should update weatherResponse on success`() = runTest {
        // Arrange
        val mockWeatherResponse = newYorkWeatherResponse
        `when`(weatherRepository.getWeatherByCoordinates(51.5074, -0.1278)).thenReturn(mockWeatherResponse)

        // Act
        viewModel.getWeatherForCurrentLocation(51.5074, -0.1278)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(mockWeatherResponse, viewModel.weatherResponse.value)
        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun `getWeatherForCurrentLocation should update errorMessage on failure`() = runTest {
        // Arrange
        `when`(weatherRepository.getWeatherByCoordinates(51.5074, -0.1278)).thenThrow(RuntimeException("Location Error"))

        // Act
        viewModel.getWeatherForCurrentLocation(51.5074, -0.1278)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals("Failed to fetch weather data for location.", viewModel.errorMessage.value)
        assertNull(viewModel.weatherResponse.value)
    }
}
