package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.NasaApi
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(application: Application) : AndroidViewModel(application) {



    init {

    }

    private fun getResult() {
      viewModelScope.launch {
          try {






          } catch (e: Exception) {

          }
      }
    }
}