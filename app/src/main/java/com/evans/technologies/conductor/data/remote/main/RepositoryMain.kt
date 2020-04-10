package com.evans.technologies.conductor.data.remote.main

import com.evans.technologies.conductor.model.chats
import com.evans.technologies.conductor.model.config
import com.summit.roomexample.base.Resource
import kotlinx.coroutines.flow.Flow

interface RepositoryMain {
    suspend fun getVersion():Flow<Resource<Int>>
    suspend fun getSettingsAccount():Flow<Resource<config>>
    suspend fun getChats(): Flow<Resource<List<chats>>>
    suspend fun changeSwitch(statusSwitch:Boolean): Resource<Boolean>
}