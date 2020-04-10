package  com.evans.technologies.conductor.data.network.Interactor

import android.os.AsyncTask
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.firebase.firestore.CollectionReference

abstract class NetworkBoundResourceFirebase<ResultType, RequestType> @MainThread internal constructor(clase:Any) {
    private val result =
        MediatorLiveData<ResourceBound<ResultType>>()
    private var clase:Any = clase
    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        result.addSource(
            dbSource
        ) { newData: ResultType ->
            result.setValue(
                ResourceBound.loading(newData)
            )
        }
        createCallFirebase().addSnapshotListener { documentSnapshot, firestoreException ->
           if (firestoreException!=null){
               onFetchFailed()
               result.removeSource(dbSource)
               result.addSource(
                   dbSource
               ) { newData: ResultType ->
                   result.setValue(
                       ResourceBound.error(
                           firestoreException.message,
                           newData
                       )
                   )
               }
           }else{
               result.removeSource(dbSource)
               var datos:ArrayList<Any>?=null
               documentSnapshot!!.documents.forEach {
                   val int =it.toObject(clase::class.java)
                   datos!!.add(int!!)
               }
               saveResultAndReInit(datos as RequestType)
           }
        }
    }

    @MainThread
    private fun saveResultAndReInit(response: RequestType?) {
        object : AsyncTask<Void?, Void?, Void?>() {


            override fun onPostExecute(aVoid: Void?) {
                result.addSource(
                    loadFromDb()
                ) { newData: ResultType ->
                    result.setValue(ResourceBound.success(newData))
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
    protected abstract fun createCallFirebase(): CollectionReference


    @MainThread
    protected fun onFetchFailed() {
    }

    val asLiveData: LiveData<ResourceBound<ResultType>>
        get() = result
//<Any>
    init {
        result.setValue(ResourceBound.loading(null))
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data: ResultType ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(
                    dbSource
                ) { newData: ResultType ->
                    result.setValue(ResourceBound.success(newData))
                }
            }
        }
    }
}