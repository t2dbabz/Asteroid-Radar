package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception

class AsteroidRepository(private val database: AsteroidsDatabase) {

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroid()) {
        it.asDomainModel()
    }


    suspend fun refreshAsteroid() {
        withContext(Dispatchers.IO) {
            try {
                val networkResponse = NasaApi.retrofitService.getAsteroidProperties()
                if (networkResponse.isSuccessful) {
                    val resultList = networkResponse.body()!!
                    val asteroidList = parseAsteroidsJsonResult(JSONObject(resultList))
                    database.asteroidDao.insertAll(*asteroidList.asDatabaseModel())
                }
            } catch (e: Exception) {

            }
        }
    }
}