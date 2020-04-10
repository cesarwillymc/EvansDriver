package com.evans.technologies.conductor.data.network.Interactor

import android.os.AsyncTask
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class NetworkBoundResourceRetrofit<ResultType, RequestType> @MainThread internal constructor() {
    private val result =
        MediatorLiveData<ResourceBound<ResultType>>()

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        result.addSource(
            dbSource
        ) { newData: ResultType ->
            result.postValue(
                ResourceBound.loading(newData)
            )
        }
        createCallRetrofit().enqueue(object : Callback<RequestType> {
            override fun onResponse(
                call: Call<RequestType>,
                response: Response<RequestType>
            ) {
                result.removeSource(dbSource)
                saveResultAndReInit(response.body())
            }

            override fun onFailure(
                call: Call<RequestType>,
                t: Throwable
            ) {
                onFetchFailed()
                result.removeSource(dbSource)
                result.addSource(
                    dbSource
                ) { newData: ResultType ->
                    result.postValue(ResourceBound.error(
                        t.message,
                        newData
                    ))

                }
            }
        })
    }

    @MainThread
    private fun saveResultAndReInit(response: RequestType?) {
        object : AsyncTask<Void?, Void?, Void?>() {


            override fun onPostExecute(aVoid: Void?) {
                result.addSource(
                    loadFromDb()
                ) { newData: ResultType ->
                    result.postValue(ResourceBound.success(newData))
                }
            }

            override fun doInBackground(vararg params: Void?): Void? {
                saveCallResult(response!!)
                return null
            }
        }.execute()
    }

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected fun shouldFetch(data: ResultType?): Boolean {
        return true
    }

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCallRetrofit(): Call<RequestType>

    @MainThread
    protected fun onFetchFailed() {
    }

    val asLiveData: LiveData<ResourceBound<ResultType>>
        get() = result
//<Any>
    init {
        result.postValue(ResourceBound.loading(null))
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data: ResultType ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(
                    dbSource
                ) { newData: ResultType ->
                    result.postValue(ResourceBound.success(newData))
                }
            }
        }
    }
}