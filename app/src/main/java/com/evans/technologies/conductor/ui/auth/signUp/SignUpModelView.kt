package com.evans.technologies.conductor.ui.auth.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.evans.technologies.conductor.data.local.entities.Usuario
import com.evans.technologies.conductor.data.remote.auth.signup.RepositorySignUp
import com.evans.technologies.conductor.model.LoginResponse
import com.summit.roomexample.base.Resource
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SignUpModelView(private val repositorySignUp: RepositorySignUp): ViewModel(), CoroutineScope {
    private val job= Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job
    fun SignUp(loginResponse: LoginResponse): LiveData<Resource<Unit>> = liveData(Dispatchers.IO){
        emit(Resource.Loading())
        try{
            async {
                repositorySignUp.SignUpAuth(loginResponse)
            }.await()
            emit(Resource.Success(Unit))
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }
    fun dettachJob(){
        coroutineContext.cancel()
    }
}