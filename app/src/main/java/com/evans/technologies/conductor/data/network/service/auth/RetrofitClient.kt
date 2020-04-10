package com.evans.technologies.conductor.data.network.service.auth

import com.evans.technologies.conductor.common.constants.Constants.BASE_URL_API
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {
    private val retrofit: Retrofit
    val api: ApiAuth
        get() = retrofit.create(ApiAuth::class.java)

    companion object {
        private val BASE_URL: String = BASE_URL_API
        private var mInstance: RetrofitClient? = null
        @JvmStatic
        @get:Synchronized
        val instance: RetrofitClient?
            get() {
                if (mInstance == null) {
                    mInstance = RetrofitClient()
                }
                return mInstance
            }
        fun getRetrofitClient(): ApiAuth {
            return RetrofitClient.instance!!.api
        }
    }

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}