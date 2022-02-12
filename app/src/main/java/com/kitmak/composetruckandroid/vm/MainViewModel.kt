package com.kitmak.composetruckandroid.vm

import android.content.Context
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.await
import com.kitmak.repo.TruckResult
import com.kitmak.network.remote.responses.TruckLocation
import com.kitmak.network.remote.responses.TruckRoute
import com.kitmak.network.remote.responses.toDomain
import com.kitmak.repo.TruckRepo
import com.kitmak.workers.UpdateRouteWorker
import com.kitmak.workers.UpdateRouteWorker.Companion.Progress
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val repo: TruckRepo
) : ViewModel() {
    init {
        val getRouteWorkRequest = OneTimeWorkRequest.from(UpdateRouteWorker::class.java)
        val id = getRouteWorkRequest.id


        WorkManager.getInstance(context)
            .enqueue(getRouteWorkRequest)

        viewModelScope.launch {
            val info: WorkInfo = WorkManager.getInstance(context)
                .getWorkInfoById(id)
                .await()
            Log.d(
                "Progress", "${info.progress.getInt(Progress, 0)}"
            )
        }

    }

    fun latestRoutes(): Flow<List<TruckRoute>> {
        return repo.latestTruckRoute()
    }

    suspend fun findRouteById(id: String): List<TruckRoute> {
        return repo.findRouteById(id).map { it.toDomain() }
    }

    suspend fun fetchTruckLocation(): Flow<TruckResult<List<TruckLocation>>> {
        return repo.fetchTruckLocation()
    }

}