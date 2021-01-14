package com.inmedia.radio.base

import com.inmedia.radio.utils.SingleLiveEvent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

abstract class BaseViewModel : ViewModel(), LifecycleObserver{

    val navControllerLiveEvent = SingleLiveEvent<NavigationModel>()
    val toastLiveEvent = SingleLiveEvent<ToastModel>()
    private val asyncJobs = mutableListOf<Job>()

    fun addObserver(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    fun removeObserver(lifecycle: Lifecycle) {
        lifecycle.removeObserver(this)
    }

    protected fun launchAsync(block: suspend CoroutineScope.() -> Unit) {
        val job: Job = GlobalScope.launch(Dispatchers.Main) { block() }
        asyncJobs.add(job)
        job.invokeOnCompletion { asyncJobs.remove(job) }
    }

    protected fun launchAsyncOnError(
        block: suspend CoroutineScope.() -> Unit,
        onError:(() -> Unit)? = null
    ) {
        val job: Job = GlobalScope.launch(Dispatchers.Main) {
            try {
                block()
            } catch (e: Exception) {
                onError?.invoke()
            }
        }
        asyncJobs.add(job)
        job.invokeOnCompletion { asyncJobs.remove(job) }
    }

    private fun cancelAllAsync() {
        val asyncJobsSize = asyncJobs.size

        if (asyncJobsSize > 0) {
            for (i in asyncJobsSize - 1 downTo 0) {
                asyncJobs[i].cancel()
            }
        }
    }

    fun backPressed(){
        navControllerLiveEvent.value = NavigationModel(popBack = true)
    }

    override fun onCleared() {
        super.onCleared()
        cancelAllAsync()
    }
}