package com.summit.summitfood.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.evans.technologies.conductor.common.constants.Constants
import com.evans.technologies.conductor.common.shared.SharedPreferencsManager.Companion.setSomeStringValue
import com.evans.technologies.conductor.data.remote.auth.signin.RepositoryAuth
import com.summit.roomexample.base.Resource
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

class SignInViewModel(val repo: RepositoryAuth):ViewModel(),CoroutineScope {
    private val job= Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    fun SignIn(email:String,pass:String):LiveData<Resource<Unit>> = liveData(Dispatchers.IO) {
            emit(Resource.Loading())
            try {
                val time=measureTimeMillis {
                    async {
                        try{
                            Log.d("signin","entro async 1")
                            repo.SignInAuth(email, pass)
                            val dato=repo.getTokenFirebase()
                            Log.d("signin",dato)

                            setSomeStringValue(Constants.PREF_TOKEN_FIREBASE,dato)
                        }catch (e:Exception){
                            Log.d("signin","entra try cath")
                            emit(Resource.Failure(e))

                            this.cancel()
                        }
                    }.await()
                    async {
                        try{
                            Log.d("signin","entro async 2")
                            repo.getInformationUserService()
                            setSomeStringValue(Constants.PREF_PROFILE_URL,"https://images.app.goo.gl/c31fXu2Zb6KvufoHA")
                        }catch (e:Exception){
                            Log.d("signin","$e entra try cath 2")
                            emit(Resource.Failure(e))
                            this.cancel()
                        }
                    }.await()
                    async {
                        try{
                            Log.d("signin","entro async 3")
                            repo.getInformationUserDriversService()
                            repo.getPhotoProfile()
                            repo.getPhotosCar()
                        }catch (e:Exception){
                            Log.d("signin","$e entra try cath3 ")
                            emit(Resource.Failure(e))
                            this.cancel()
                        }
                    }.await()
                }
                Log.d("signin","Termino exitosamente en ${time.toString()}")
                emit(Resource.Success(Unit))
            }catch (e:Exception){
               // emit(Resource.Failure(e))
            }
    }
    fun dettachJob(){
        coroutineContext.cancel()
    }

    //Token se cre junto con el inicio de sesion
    // y todol lo demas asincronamente
}