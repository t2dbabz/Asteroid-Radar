package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface AsteroidApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroidProperties(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String
    ): String
}

interface PictureOfDayApiService {
    @GET("planetary/apod")
    suspend fun getImageOfTheDay(
        @Query("api_key") apiKey: String
    ): PictureOfDay
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofitAsteroid = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL)
    .build()

object AsteroidApi {
    val retrofitService: AsteroidApiService by lazy {
        retrofitAsteroid.create(AsteroidApiService::class.java)
    }
}

private val retrofitPictureOfDay = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()

object PictureOfDayApi {
    val retrofitService: PictureOfDayApiService by lazy {
        retrofitPictureOfDay.create(PictureOfDayApiService::class.java)
    }
}