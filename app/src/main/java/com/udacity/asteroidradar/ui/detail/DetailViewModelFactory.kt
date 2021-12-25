package com.udacity.asteroidradar.ui.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.domain.Asteroid
import java.lang.IllegalArgumentException

class DetailViewModelFactory(
    private val asteroid: Asteroid,
    private val app: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(asteroid, app) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }

}