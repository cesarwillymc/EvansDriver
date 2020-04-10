package com.evans.technologies.conductor.data.remote.auth.signin

import androidx.lifecycle.LiveData
import com.evans.technologies.conductor.data.local.entities.Car
import com.evans.technologies.conductor.data.local.entities.PhotosCar
import com.evans.technologies.conductor.data.local.entities.Usuario
import com.evans.technologies.conductor.data.network.Interactor.ResourceBound
import com.evans.technologies.conductor.model.Driver
import com.evans.technologies.conductor.model.LoginResponse
import com.evans.technologies.conductor.model.RegistroInicioSesion
import com.evans.technologies.conductor.model.infoDriver
import com.summit.roomexample.base.Resource
import kotlinx.coroutines.Deferred

interface RepositoryAuth {
    suspend fun SignInAuth(email:String,pass:String)
    suspend fun getInformationUserDriversService()
    suspend fun getInformationUserService()
    suspend fun getPhotosCar()
    suspend fun getTokenFirebase(): String
    suspend fun setTokenFirebase()
    suspend fun getPhotoProfile()
}