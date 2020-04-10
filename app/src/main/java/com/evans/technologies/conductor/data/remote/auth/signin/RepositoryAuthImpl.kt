package com.evans.technologies.conductor.data.remote.auth.signin

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.evans.technologies.conductor.app.MyApp
import com.evans.technologies.conductor.common.constants.Constants
import com.evans.technologies.conductor.common.constants.Constants.PREF_ID_USER
import com.evans.technologies.conductor.common.constants.Constants.PREF_PROFILE_URL
import com.evans.technologies.conductor.common.shared.SharedPreferencsManager.Companion.getSomeStringValue
import com.evans.technologies.conductor.common.shared.SharedPreferencsManager.Companion.setSomeStringValue
import com.evans.technologies.conductor.data.local.db.AppDB.Companion.getInstanceDB
import com.evans.technologies.conductor.data.network.service.auth.ApiAuth
import com.evans.technologies.conductor.data.network.service.auth.ApiClienteRetrofit
import com.evans.technologies.conductor.data.network.service.auth.ClienteRetrofit
import com.evans.technologies.conductor.data.network.service.auth.ClienteRetrofit.Companion.getClienteRetrofit
import com.evans.technologies.conductor.data.network.service.auth.RetrofitClient
import com.evans.technologies.conductor.data.network.service.auth.RetrofitClient.Companion.getRetrofitClient
import com.evans.technologies.conductor.model.Driver
import com.evans.technologies.conductor.model.LoginResponse
import com.evans.technologies.conductor.model.RegistroInicioSesion
import com.evans.technologies.conductor.model.infoDriver
import com.evans.technologies.conductor.utils.*
import com.google.firebase.iid.FirebaseInstanceId
import com.summit.roomexample.base.Resource
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RepositoryAuthImpl:
    RepositoryAuth {
    private var apiAuth= getRetrofitClient()
    private var apiAuthClient= getClienteRetrofit()
    private var usuarioDao= getInstanceDB()!!.usuarioDao
    private var carDao= getInstanceDB()!!.carDao
    private var photosCarDao= getInstanceDB()!!.photosCar
    override suspend fun SignInAuth(email:String,pass:String):Unit = suspendCancellableCoroutine {
           apiAuth.loginUser(email,pass).enqueue(object : Callback<LoginResponse>{
               override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                   Log.d("signin", t.message +" on failure")
                   it.resumeWithException(Exception("Revise su conexion de red"))
               }
               override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                   if (response.isSuccessful){
                       try {
                           Log.d("signin",response.body()!!.driver.token )
                           Log.d("signin",  response.body()!!.driver.id)

                           setSomeStringValue(Constants.PREF_TOKEN,response.body()!!.driver.token)
                           setSomeStringValue(Constants.PREF_ID_USER,response.body()!!.driver.id)
                           it.resume(Unit)
                       }catch (e:Exception){
                           Log.d("signin", "exception "+e.message)
                           it.resumeWithException(e)
                       }

                   }else{
                       Log.d("signin", response.message() +" on else")
                       it.resumeWithException(Exception(response.message() + " " + response.code()))

                   }
               }

           })

    }


    override suspend fun getInformationUserDriversService(): Unit= suspendCancellableCoroutine {
        apiAuthClient.getInfoDriver(getSomeStringValue(Constants.PREF_ID_USER)!!).enqueue(object :Callback<infoDriver>{
            override fun onFailure(call: Call<infoDriver>, t: Throwable) {
                it.resumeWithException(Exception("Revise su conexion de red"))
            }

            override fun onResponse(call: Call<infoDriver>, response: Response<infoDriver>) {
                if (response.isSuccessful && response.body()!=null){
                    it.resume(Unit)
                }else{
                    Log.d("signin", "Photos ${response.code()}")
                    it.resume(Unit)
                }

            }

        })
    }

    override suspend fun getPhotosCar(): Unit= suspendCancellableCoroutine{
        apiAuthClient.getDataInicioSesion(getSomeStringValue(Constants.PREF_ID_USER)!!).enqueue(object : Callback<RegistroInicioSesion>{
            override fun onFailure(call: Call<RegistroInicioSesion>, t: Throwable) {
                it.resumeWithException(Exception("Revise su conexion de red"))
            }
            override fun onResponse(
                call: Call<RegistroInicioSesion>,
                response: Response<RegistroInicioSesion>
            ) {
                if (response.isSuccessful && response.body()!=null){
                    it.resume(Unit)
                }else{
                    Log.d("signin", "Photos ${response.code()}")
                    it.resume(Unit)
                }

            }
        })
    }

    override suspend fun getTokenFirebase(): String= suspendCancellableCoroutine{
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {continuation->
            it.resume( continuation.result!!.token)
        }.addOnFailureListener {e->
            it.resumeWithException(e)
        }
    }

    override suspend fun setTokenFirebase():Unit= suspendCancellableCoroutine {
        apiAuthClient.tokenFCM(getSomeStringValue(PREF_ID_USER)!!
            ,getSomeStringValue(Constants.PREF_TOKEN)!!,getSomeStringValue(Constants.PREF_TOKEN_FIREBASE)!!)
            .enqueue(object : Callback<Driver>{
                override fun onFailure(call: Call<Driver>, t: Throwable) {
                    it.resumeWithException(Exception("Revise su conexion de red"))
                }
                override fun onResponse(call: Call<Driver>, response: Response<Driver>) {
                    if (response.isSuccessful){
                        it.resume(Unit)
                    }else{
                        it.resumeWithException(Exception("Se presentaron problemas al intentar logearse"))
                    }
                }

            })
    }

    override suspend fun getPhotoProfile():Unit = suspendCancellableCoroutine {
       // BASE_URL_AMAZON_IMG+
        Glide.with(MyApp.getContextApp())
            .asBitmap()
            .load(getSomeStringValue(PREF_PROFILE_URL))
            .into(object : SimpleTarget<Bitmap>(100, 100) {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                   runBlocking {
                        try {
                            //getSomeStringValue(PREF_ID_USER)!!
                            saveToInternalStorage(resource,"guardado" )
                            it.resume(Unit)
                        }catch (e:Exception){
                            it.resume(Unit)
                           // it.resumeWithException((e))
                        }
                   }
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    it.resume(Unit)
                    // it.resumeWithException(Exception("Revise su conexion a la red, no se puedo descargar la imagen"))
                }
            })
    }
/*

        return object :NetworkBoundResourceRetrofit<Usuario,LoginResponse >(){
            override fun saveCallResult(item: LoginResponse) {
               // usuarioDao.insertUsuario(null)
            }

            override fun loadFromDb(): LiveData<Usuario> {
                return usuarioDao.selectUsuario(getSomeStringValue(PREF_ID_USER)!!)
            }

            override fun createCallRetrofit(): Call<LoginResponse> {
                return
            }
        }.asLiveData
 */
    override suspend fun getInformationUserService(): Unit= suspendCancellableCoroutine {
        apiAuthClient.getUserInfo(getSomeStringValue(PREF_ID_USER)!!).enqueue(object : Callback<LoginResponse>{
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                it.resumeWithException(Exception("Revise su conexion de red"))
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body()!=null){
                    it.resume(Unit)
                }else{
                    Log.d("signin", "Photos ${response.code()}")
                    it.resumeWithException(Exception("Error ${response.message()} ${response.code()}"))
                }

            }

        })

    }
}