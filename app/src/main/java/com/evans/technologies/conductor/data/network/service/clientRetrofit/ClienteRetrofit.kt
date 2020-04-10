package com.evans.technologies.conductor.data.network.service.auth

import com.evans.technologies.conductor.common.constants.Constants.BASE_URL_API
import com.evans.technologies.conductor.data.network.service.authInterceptor.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ClienteRetrofit private constructor() {
    private val retrofit: Retrofit
    val api: ApiClienteRetrofit
        get() = retrofit.create(ApiClienteRetrofit::class.java)

    companion object {
        private val BASE_URL: String = BASE_URL_API
        private var mInstance: ClienteRetrofit? = null
        @JvmStatic
        @get:Synchronized
        val instance: ClienteRetrofit?
            get() {
                if (mInstance == null) {
                    mInstance = ClienteRetrofit()
                }
                return mInstance
            }
        fun getClienteRetrofit(): ApiClienteRetrofit {
            return ClienteRetrofit.instance!!.api
        }
    }

    init {
        val okHttpClienteBuilder=OkHttpClient.Builder()
        okHttpClienteBuilder.addInterceptor(AuthInterceptor())
        val cliente:OkHttpClient= okHttpClienteBuilder.build()
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(cliente)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}