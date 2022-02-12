package com.kitmak.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.kitmak.repo.TruckRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UpdateRouteWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted parameters: WorkerParameters,
    val repo: TruckRepo,
) :
    CoroutineWorker(context, parameters) {

    companion object {
        const val Progress = "Progress"
    }

    override suspend fun doWork(): Result {
        var page = 0
        while (fetchAndSave(page)) {
            page++
        }

        return Result.success()
    }

    suspend fun fetchAndSave(page: Int): Boolean {
        val routes = repo.fetchTruckRoute(page)
        if (routes.isEmpty()) {
            return false
        }
        repo.saveTruckRoute(routes)
        val update = workDataOf(Progress to page)
        setProgress(update)
        return true
    }
}