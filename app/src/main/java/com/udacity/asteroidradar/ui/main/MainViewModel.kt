package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.util.Constants
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(application: Application) : AndroidViewModel(application) {
    enum class NasaApiStatus {LOADING, DONE, ERROR}
    private val database = AsteroidsDatabase.getInstance(application)
    private val asteroidRepository = AsteroidRepository(database)

    private val startDate = getNextSevenDaysFormattedDates()[0]
    private val endDate = getNextSevenDaysFormattedDates()[6]
    private val weekEnd = getNextSevenDaysFormattedDates()[4]


    var asteroids: LiveData<List<Asteroid>> = asteroidRepository.asteroids

    private val _status = MutableLiveData<NasaApiStatus>()
    val status : LiveData<NasaApiStatus>
        get() = _status


    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfTheDay : LiveData<PictureOfDay>
    get() = _pictureOfTheDay


    init {
        getRefreshAsteroid()
    }

    private fun getRefreshAsteroid() {
        viewModelScope.launch{
            try {
                _status.value = NasaApiStatus.LOADING
                asteroidRepository.refreshAsteroid(startDate, endDate, Constants.API_KEY)
                getPictureOfTheDay()
                _status.value = NasaApiStatus.DONE

            }catch (e : Exception) {
                _status.value = NasaApiStatus.ERROR
            }
        }
    }

    fun getWeekAsteroid(): LiveData<List<Asteroid>> {
         val weekAsteroid = Transformations.map(asteroidRepository.getWeekAsteroids(startDate, weekEnd)){
            it.asDomainModel()
        }
        return weekAsteroid
    }

    fun getTodayAsteroids(): LiveData<List<Asteroid>> {
        val todayAsteroids = Transformations.map(asteroidRepository.getTodayAsteroids(startDate)){
            it.asDomainModel()
        }
        return todayAsteroids
    }

    private fun getPictureOfTheDay() {
        viewModelScope.launch {
            val picture = asteroidRepository.getPictureOfTheDay(Constants.API_KEY)
            picture.let {
                _pictureOfTheDay.value = picture
            }
        }
    }
}