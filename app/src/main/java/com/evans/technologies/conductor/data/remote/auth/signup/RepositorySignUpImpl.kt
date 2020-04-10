package com.evans.technologies.conductor.data.remote.auth.signup

import com.evans.technologies.conductor.data.network.service.auth.ClienteRetrofit
import com.evans.technologies.conductor.data.network.service.auth.RetrofitClient
import com.evans.technologies.conductor.model.LoginResponse
import com.evans.technologies.conductor.model.RegisterResponse
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RepositorySignUpImpl:RepositorySignUp {
    private var apiAuth= RetrofitClient.getRetrofitClient()
    private var apiAuthClient= ClienteRetrofit.getClienteRetrofit()
    override suspend fun SignUpAuth(model: LoginResponse):Unit= suspendCancellableCoroutine {
        apiAuth.createUser(model).enqueue(object :Callback<RegisterResponse>{
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                it.resumeWithException(Exception("Verifique su conexion a internet"))
            }
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {

                if (response.isSuccessful){
                    it.resume(Unit)
                }else{
                    it.resumeWithException(Exception(  response.body()!!.msg))
                }
            }

        })
    }
}