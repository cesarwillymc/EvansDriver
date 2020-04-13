package com.evans.technologies.conductor.data.remote.main

import android.provider.Settings.Global.getString
import android.util.Log
import com.evans.technologies.conductor.R
import com.evans.technologies.conductor.app.MyApp.Companion.getContextApp
import com.evans.technologies.conductor.common.constants.Constants
import com.evans.technologies.conductor.common.constants.Constants.ACCESS_TOKEN_MAPBOX
import com.evans.technologies.conductor.common.constants.Constants.PREF_ID_USER
import com.evans.technologies.conductor.common.shared.SharedPreferencsManager.Companion.getSomeStringValue
import com.evans.technologies.conductor.common.shared.SharedPreferencsTemp.Companion.getTempStringValue
import com.evans.technologies.conductor.data.local.entities.StatusTrip
import com.evans.technologies.conductor.data.network.service.auth.ClienteRetrofit
import com.evans.technologies.conductor.data.remote.request.DriverToUser
import com.evans.technologies.conductor.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.geojson.Point
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import com.summit.roomexample.base.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

    override suspend fun getStatusTrip(): Resource<infoDriver> = suspendCancellableCoroutine {

            val statusTrip= apiAuthClient.getStatusTrip(getSomeStringValue(Constants.PREF_ID_USER)!!)
            statusTrip.enqueue( object:Callback<infoDriver> {
                override fun onFailure(call: Call<infoDriver>, t: Throwable) {
                   it.resumeWithException(Exception("error en la señal"))
                }

                override fun onResponse(call: Call<infoDriver>, response: Response<infoDriver>) {
                    if (response.isSuccessful){
                        it.resume(Resource.Success(response.body()!!))
                    }else{
                        it.resumeWithException(Exception("error al traer datos"))
                    }
                }

            })


    }

    override suspend fun getRouteMapbox(origin:Point,destination:Point): Resource<String> = suspendCancellableCoroutine {
        NavigationRoute.builder(getContextApp())
                    .accessToken(ACCESS_TOKEN_MAPBOX)
                    .origin(origin)
                    .destination(destination)
                    .build()
                    .getRoute(object :Callback<DirectionsResponse> {
                        override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                            it.resumeWithException(Exception("Revise su conexion, no pudo acceder a mapbox"))
                        }
                        override fun onResponse(
                            call: Call<DirectionsResponse>,
                            response: Response<DirectionsResponse>
                        ) {
                                if ((response.body() == null) || (response.body()!!.routes().size<1 )) {
                                    it.resumeWithException(Exception("el cuerpo es nulo o no se encontro una ruta"))
                                }else{
                                    it.resume(Resource.Success(response.body()!!.toJson()))
                                }
                        }
                    });
    }
//Esto es una sola funcion
    override suspend fun acceptTrip() {
        apiAuthClient.acceptNotification(getSomeStringValue(PREF_ID_USER)!!,getTempStringValue(PREF_ID_USER)!!)
            .enqueue(object :Callback<Driver> {
                override fun onFailure(call: Call<Driver>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResponse(call: Call<Driver>, response: Response<Driver>) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            })
    }

    override suspend fun putStatusTrip(statusTrip: StatusTrip) {
        apiAuthClient.puStatusTrip(getTempStringValue(PREF_ID_USER)!!,statusTrip).enqueue(object :Callback<Driver> {
            override fun onFailure(call: Call<Driver>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<Driver>, response: Response<Driver>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    override suspend fun conexionDriverToUser(driverToUser: DriverToUser) {
        apiAuthClient.driverTOuser(getSomeStringValue(PREF_ID_USER)!!,driverToUser)
        .enqueue(object :Callback<Driver> {
            override fun onFailure(call: Call<Driver>, t: Throwable) {

            }

            override fun onResponse(call: Call<Driver>, response: Response<Driver>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


        })
    }
    //Esto es una sola funcion
}