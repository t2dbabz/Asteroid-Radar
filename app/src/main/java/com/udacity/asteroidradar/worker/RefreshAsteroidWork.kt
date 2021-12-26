package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.util.Constants
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*

class RefreshAsteroidWork(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshAsteroidsWork"
    }
    override suspend fun doWork(): Result {
        val database = AsteroidsDatabase.getInstance(applicationContext)
        val repository = AsteroidRepository(database)

        val startDate = getNextSevenDaysFormattedDates()[0]
        val endDate = getNextSevenDaysFormattedDates()[5]

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val previousDayTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val previousDayDate = dateFormat.format(previousDayTime)


        return try {
            repository.refreshAsteroid(startDate, endDate, Constants.API_KEY)
            repository.deletePreviousDayAsteroid(previousDayDate)
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }


    }

}