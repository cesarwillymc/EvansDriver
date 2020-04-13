package com.evans.technologies.conductor.data.remote.main

import com.evans.technologies.conductor.data.local.entities.StatusTrip
import com.evans.technologies.conductor.data.remote.request.DriverToUser
import com.evans.technologies.conductor.model.chats
import com.evans.technologies.conductor.model.config
import com.evans.technologies.conductor.model.infoDriver
import com.mapbox.geojson.Point
import com.summit.roomexample.base.Resource
import kotlinx.coroutines.flow.Flow

interface RepositoryMain {
    suspend fun getVersion():Flow<Resource<Int>>
    suspend fun getSettingsAccount():Flow<Resource<config>>
    suspend fun getChats(): Flow<Resource<List<chats>>>
    suspend fun changeSwitch(statusSwitch:Boolean): Resource<Boolean>
    suspend fun getStatusTrip(): Resource<infoDriver>
    suspend fun getRouteMapbox(origin: Point, destination: Point):Resource<String>
    suspend fun acceptTrip()
    suspend fun putStatusTrip(statusTrip: StatusTrip)
    suspend fun conexionDriverToUser(driverToUser: DriverToUser)
}