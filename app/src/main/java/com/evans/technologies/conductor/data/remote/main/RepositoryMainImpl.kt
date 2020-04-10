package com.evans.technologies.conductor.data.remote.main

import com.evans.technologies.conductor.common.constants.Constants.PREF_ID_USER
import com.evans.technologies.conductor.common.shared.SharedPreferencsManager.Companion.getSomeStringValue
import com.evans.technologies.conductor.data.network.service.auth.ClienteRetrofit
import com.evans.technologies.conductor.model.Driver
import com.evans.technologies.conductor.model.chats
import com.evans.technologies.conductor.model.config
import com.google.firebase.firestore.FirebaseFirestore
import com.summit.roomexample.base.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RepositoryMainImpl:RepositoryMain {
    private var apiAuthClient= ClienteRetrofit.getClienteRetrofit()
    override suspend fun getVersion(): Flow<Resource<Int>> = callbackFlow {
        val suscription=FirebaseFirestore.getInstance().collection("").document()
            .addSnapshotListener { documentSnapshot, exception ->
                if (exception!=null||!documentSnapshot!!.exists()){
                   channel.close(exception!!.cause)
                }else{
                    val versionCode=documentSnapshot.getLong("")!!.toInt()
                    offer(Resource.Success(versionCode))
                }
            }
        awaitClose{suscription.remove()}
    }

    override suspend fun getSettingsAccount(): Flow<Resource<config>> = callbackFlow {
        val suscription=FirebaseFirestore.getInstance().collection("settingDriver")
            .document(getSomeStringValue(PREF_ID_USER)!!)
            .addSnapshotListener { documentSnapshot, exception ->
                if (exception!=null||!documentSnapshot!!.exists()){
                    channel.close(exception!!.cause)
                }else{
                    val configAccount=documentSnapshot.toObject(config::class.java)
                    offer(Resource.Success(configAccount!!))
                }
            }
        awaitClose { suscription.remove() }
    }

    override suspend fun getChats(): Flow<Resource<List<chats>>> = callbackFlow {
        val suscription=FirebaseFirestore.getInstance().collection("chatsFirebase")
            .document(getSomeStringValue(PREF_ID_USER)!!).addSnapshotListener { documentSnapshot, exception ->
                if (exception!=null||!documentSnapshot!!.exists()){
                    channel.close(exception!!.cause)
                }else{
                    val chats=documentSnapshot.get("chats") as List<chats>
                    offer(Resource.Success(chats))
                }
            }
        awaitClose { suscription.remove() }
    }

    override suspend fun changeSwitch(statusSwitch:Boolean): Resource<Boolean> = suspendCancellableCoroutine {
        apiAuthClient.switchStatus(getSomeStringValue(PREF_ID_USER)!!,statusSwitch).enqueue(object : Callback<Driver>{
            override fun onFailure(call: Call<Driver>, t: Throwable) {
                it.resumeWithException(Exception("Error de conexion, revise su internet"))
            }

            override fun onResponse(call: Call<Driver>, response: Response<Driver>) {
                if (response.isSuccessful){
                    it.resume(Resource.Success(statusSwitch))
                }else{
                    it.resumeWithException(Exception("Error de conexion, revise su internet"))
                }
            }

        })
    }
}