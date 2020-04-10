package com.evans.technologies.conductor.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.evans.technologies.conductor.data.remote.main.RepositoryMain
import com.summit.roomexample.base.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

class MainViewModel(private val repositoryMain: RepositoryMain):ViewModel() {

    val versionCode= liveData (Dispatchers.IO){
        Log.d("versionC","entra")
        emit(Resource.Loading())
        try{
            repositoryMain.getVersion().collect {
                emit(it)
            }
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }
    val configAccount= liveData (Dispatchers.IO){
        emit(Resource.Loading())
        try {
            repositoryMain.getSettingsAccount().collect {
                emit(it)
            }
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }
    val chats= liveData (Dispatchers.IO){
        emit(Resource.Loading())
        try {
            repositoryMain.getChats().collect {
                emit(it)
            }
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }
    fun changeStatusSwitch(status:Boolean)= liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
           val response= repositoryMain.changeSwitch(status)
            emit(response)
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }
   fun dettatc(){

   }
}