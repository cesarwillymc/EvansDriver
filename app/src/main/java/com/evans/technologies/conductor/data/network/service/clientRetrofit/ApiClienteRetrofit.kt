@file:Suppress("unused")

package com.evans.technologies.conductor.data.network.service.auth


import com.evans.technologies.conductor.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiClienteRetrofit {
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
        @Field("name") name: String,
        @Field("surname") surname: String,
        @Field("numDocument") dni: String,
        @Field("email") email: String,
        @Field("cellphone") celphone: String,
        @Field("city") city: String,
        @Field("password") password: String,
        @Field("passwordconfirm") passwordconfirm: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("driver/signin")
    fun loginUser(
            @Field("email") email:String,
            @Field("password") password:String
    ): Call<LoginResponse>


    @GET
    fun getPath(
            @Url  url:String
    ):Call<String>
    @FormUrlEncoded
    @PUT("driver/updateValues/{id}/")
    fun tokenFCM(
        @Path("id") id:String,
        @Field("accessToken") accessToken:String,
        @Field("fcmToken") fcmtoken:String
    ):Call<Driver>
    @GET("driver/getInformation/{id}/")
    fun getUserInfo(
        @Path("id") id:String
    ):Call<LoginResponse>
    @FormUrlEncoded
    @PUT("driver/updateStatus/{id}/")
    fun switchStatus(
        @Path("id") id:String,
        @Field("statusSwitch") statusSwitch:Boolean
    ):Call<Driver>
    @FormUrlEncoded
    @PUT("driver/updateCoordinates/{id}/")
    fun updateCoordenadas(
        @Header("authorization") token:String,
        @Path("id") id:String,
        @Field("latitude") latitude:Double,
        @Field("longitude") longitude:Double
    ):Call<Driver>
    @FormUrlEncoded
    @POST("driver/sendNotification/{id}/")
    fun goTOnotification(
        @Header("authorization") token:String,
        @Field("id") id:String,
        @Field("tripId") tripId:String,
        @Field("driverId") driverId:String,
        @Field("title") title:String,
        @Field("message") message:String
    ): Call<Driver>
    @FormUrlEncoded
    @POST("driver/sendNotification/{driverId} ")
    fun driverTOuser(
        @Path("driverId") driverId:String,
        @Field("userId") userId:String,
        @Field("title") title:String,
        @Field("message") message:String,
        @Field("response") response:String,
        @Field("chatId") chatId:String,
        @Field("tripId") tripId:String
    ): Call<Driver>
    @FormUrlEncoded
    @PUT("driver/aceptedNotification/{Driverid} ")
    fun acceptNotification(
        @Path("Driverid") Driverid:String,
        @Field("tripId") tripId:String
    ): Call<Driver>


    @FormUrlEncoded
    @PUT("statusTrip/{tripId} ")
    fun puStatusTrip(
        @Path("tripId") Driverid:String,
        @Field("tripCancell") tripCancell:Boolean,
        @Field("tripAccepted") tripAccepted:Boolean,
        @Field("tripInitiated") tripInitiated:Boolean,
        @Field("tripFinalized") tripFinalized:Boolean

    ): Call<Driver>

    @GET("notifications/getInformationDriver/{driverId} ")
    fun getInfoDriver(
        @Path("driverId") driverId:String
    ): Call<infoDriver>
    @GET("getStatusTrip/{driverId} ")
    fun getStatusTrip(
        @Path("driverId") driverId:String
    ): Call<infoDriver>
    @FormUrlEncoded
    @POST("car/createCar/{Driverid}/")
    fun enviarDatosAdicionales(
        @Path("Driverid") Driverid:String,
        @Field("brandCar") brandCar:String,
        @Field("modelCar") modelCar:String,
        @Field("colorCar") colorCar:String,
        @Field("yearCar") yearCar:String,
        @Field("licenseCar") licenseCar:String,
        @Field("licenseCategory") licenseCategory:String
    ): Call<Driver>

    @GET("v/driver")
    fun optenerVersionAndroid(): Call<infoDriver>
    @Multipart
    @PUT("upload/img/driver/{driverId}")
    fun guardarImagenes(
        @Path("driverId")driverId:String,
        @Part profile: MultipartBody.Part,
        @Part("profile") name: RequestBody
    ): Call<Driver>

    //Registro de Inicio de Sesion

    @GET("driver/documentStatus/{driverId} ")
    fun getDataInicioSesion(
        @Path("driverId") driverId:String
    ): Call<RegistroInicioSesion>
    @Multipart
    @PUT("upload/img/driver/document/policeRecordCert/{driverId}")
    fun gdd_policeRecordCert(
        @Path("driverId")driverId:String,
        @Part policeRecordCert: MultipartBody.Part,
        @Part("policeRecordCert") name: RequestBody
    ): Call<Driver>
    @Multipart
    @PUT("upload/img/driver/document/criminalRecodCert/{driverId}")
    fun gdd_criminalRecordCert(
        @Path("driverId")driverId:String,
        @Part criminalRecordCert: MultipartBody.Part,
        @Part("criminalRecodCert") name: RequestBody
    ): Call<Driver>
    @Multipart
    @PUT("upload/img/driver/document/driverLicense/{driverId}")
    fun gdd_driverLicense(
        @Path("driverId")driverId:String,
        @Part driverLicense: MultipartBody.Part,
        @Part("driverLicense") name: RequestBody
    ): Call<Driver>
    @Multipart
    @PUT("upload/img/driver/car/SOAT/{driverId}")
    fun gdd_soat(
        @Path("driverId")driverId:String,
        @Part soat: MultipartBody.Part,
        @Part("SOAT") name: RequestBody
    ): Call<Driver>
    @Multipart
    @PUT("upload/img/driver/car/propertyCardForward/{driverId}")
    fun gdd_propertyCardForward(
        @Path("driverId")driverId:String,
        @Part propertyCardForward: MultipartBody.Part,
        @Part("propertyCardForward") name: RequestBody
    ): Call<Driver>
    @Multipart
    @PUT("upload/img/driver/car/propertyCardBack/{driverId}")
    fun gdd_propertyCardBack(
        @Path("driverId")driverId:String,
        @Part propertyCardBack: MultipartBody.Part,
        @Part("propertyCardBack") name: RequestBody
    ): Call<Driver>
    @FormUrlEncoded
    @POST("driver/sendNotification/{driverId} ")
    fun enviar_tipo_socio(
        @Path("driverId") driverId:String,
        @Field("tipo_socio") tipo_socio:Int
    ): Call<Driver>
    @GET("other/driver/active/{driverId}")
    fun callAcountActivate(
        @Path("driverId") driverId:String
    ):Call<Driver>

    @Multipart
    @PUT("upload/img/driver/pago/{idpago}")
    fun insert_voucher(
        @Path("idpago")idpago:String,
        @Part propertyCardBack: MultipartBody.Part,
        @Part("baucherImg") name: RequestBody
    ): Call<Driver>
    @FormUrlEncoded
    @PUT("driver/registerPayment/{driverId}")
    fun send_data_voucher(
        @Path("driverId")driverId:String,
        @Field("fecha") fecha:String,
        @Field("monto") monto:String,
        @Field("serie") serie:String
    ): Call<Driver>
    @GET("driver/baucherStatus/{driverId}")
    fun tiene_voucher(
        @Path("driverId") driverId:String
    ):Call<Driver>
    @GET("getDriverMsgNotificationFindByID/{driverId}")
    fun getAllNotifys(
        @Path("driverId") driverId:String
    ):Call<infoDriver>
    @FormUrlEncoded
    @PUT("UpdateMessageStatusById")
    fun changeStatusNotify(
        @Field("MessageID")MessageID:String,
        @Field("state") state:Boolean
    ): Call<infoDriver>

    @DELETE("setDeleteOneMessageByID/{MessageID}")
    fun deleteNotify(
        @Path("MessageID")driverId:String
    ):Call<infoDriver>
}