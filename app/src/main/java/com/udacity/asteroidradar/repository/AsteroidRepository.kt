package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.api.NasaApi

class AsteroidRepository {

    suspend fun getAsteroidProperties() = NasaApi.retrofitService.getAsteroidProperties()
}