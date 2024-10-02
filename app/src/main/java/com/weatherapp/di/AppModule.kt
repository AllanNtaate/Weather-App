package com.weatherapp.di

import android.content.Context
import com.weatherapp.data.network.local.SharedPreferencesManager
import com.weatherapp.data.network.service.WeatherApiService
import com.weatherapp.domain.utilities.Const
import com.weatherapp.data.network.repo.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * AppModule provides application-level dependencies for the weather app.
 *
 * This object is annotated with @Module and @InstallIn to indicate that it's a Hilt module
 * responsible for providing dependencies to the SingletonComponent, making these dependencies
 * available throughout the entire application lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides the WeatherApiService instance.
     *
     * This method uses Retrofit to build the API service for making network requests.
     *
     * @return an instance of WeatherApiService, configured to interact with the weather API.
     */
    @Provides
    @Singleton
    fun provideWeatherApiService(): WeatherApiService {

        val builder = OkHttpClient().newBuilder()
        builder.readTimeout(10, TimeUnit.SECONDS)
        builder.connectTimeout(5, TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
        builder.addInterceptor(interceptor)
        val client = builder.build()

        return Retrofit.Builder()
            .baseUrl(Const.BASE_URL)  // Use the base URL from the Const object
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(WeatherApiService::class.java)
    }

    /**
     * Provides the WeatherRepository instance.
     *
     * This repository will act as a single source of truth for weather data by abstracting
     * the API service. It is injected wherever the app needs to access weather data.
     *
     * @param apiService the WeatherApiService instance to interact with the API.
     * @return an instance of WeatherRepository.
     */
    @Provides
    @Singleton
    fun provideWeatherRepository(apiService: WeatherApiService): WeatherRepository {
        return WeatherRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesManager(@ApplicationContext context: Context): SharedPreferencesManager {
        return SharedPreferencesManager(context)
    }
}
