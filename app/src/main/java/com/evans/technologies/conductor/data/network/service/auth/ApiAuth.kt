@file:Suppress("unused")

package com.evans.technologies.conductor.data.network.service.auth


import com.evans.technologies.conductor.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiAuth {
    @FormUrlEncoded
    @POST("driver/forgot")
    fun sendCorreo_recuperar(
        @Field("email") email: String
    ): Call<Driver>
    @FormUrlEncoded
    @POST("driver/reset/confirm")
    fun sendCodigo_recuperar(
        @Field("id") id:String,
        @Field("token") token: String
    ): Call<Driver>
    @FormUrlEncoded
    @POST("driver/reset/change/{id}")
    fun sendContraseña_recuperar(
        @Path("id") id:String,
        @Field("token") token: String,
        @Field("password") password: String
    ): Call<Driver>
    ///
    @FormUrlEncoded
    @POST("driver/signup")
    fun createUser(
        @Body model:LoginResponse
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("driver/signin")
    fun loginUser(
            @Field("email") email:String,
            @Field("password") password:String
    ): Call<LoginResponse>

    @GET("notifications/getInformationDriver/{driverId} ")
    fun getInfoDriver(
        @Path("driverId") driverId:String
    ): Call<infoDriver>

    @GET("v/driver")
    fun optenerVersionAndroid(): Call<infoDriver>


}