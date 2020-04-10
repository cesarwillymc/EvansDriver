package com.evans.technologies.conductor.data.network.service.authInterceptor

import android.content.SharedPreferences
import com.evans.technologies.conductor.common.constants.Constants
import com.evans.technologies.conductor.common.shared.SharedPreferencsManager
import com.evans.technologies.conductor.common.shared.SharedPreferencsManager.Companion.getSomeStringValue
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
            val token = getSomeStringValue(Constants.PREF_TOKEN)
            val request:Request = chain.request().newBuilder().addHeader("Authorization",token).build()
            return chain.proceed(request)
    }
}