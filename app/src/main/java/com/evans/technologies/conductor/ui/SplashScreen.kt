package com.evans.technologies.conductor.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import androidx.core.app.ActivityCompat
import com.evans.technologies.conductor.R
import com.evans.technologies.conductor.ui.auth.signIn.view.LoginActivity
import com.evans.technologies.conductor.ui.main.view.MainActivity
import com.evans.technologies.conductor.utils.getUserEmail
import com.evans.technologies.conductor.utils.getUserPassword

class SplashScreen : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler().postDelayed({
            initSesion()
        }, 1000)





    }
    fun initSesion(){
        var valor="nulo"

        ActivityCompat.requestPermissions(
            this,
            arrayOf(

                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            30
        )
        // var dataDriver = getSharedPreferences("datadriver", Context.MODE_PRIVATE)
        //  setdataNotification_noti(dataDriver,"nulo")
        //{latitudeOrigen=-15.839086694816755, startAddress=sadsad, userId=5e1c067278f2bb00170b8232, response=requestDriver, longitudeOrigen=-70.01764614355434, longitudeDestino=-70.0222960003151, viajeId=5e277e8475a2db00175fc7d5, travelRate=20, destinationAddress=sdsadsadsadsadsad, latitudeDestino=-15.83869473760434}

        intent.extras?.let {
            Log.e("NotificationReceiver",it.getString("data")+" splash")
            try{
                Log.e("NotificationReceiver",it.getString("data")+" splash")
                valor=it.getString("data")!!

            }catch(e:Exception){
                //Log.e("NotificationReceiver",e.message)
            }
            // if(it.getString("data")!=null&&it.getString("data").toString().contains("null")){

            //}

        }

        /* intent.extras?.let {

            if (it.get("response")?.equals(SEND_NOTIFICATION_REQUEST_DRIVER)!!){

                valor="{"+"latitudeOrigen"+"="+it.get("latitudeOrigen")+", "+
                        "startAddress"+"="+it.get("startAddress")+", "+
                        "userId"+"="+it.get("userId")+", "+
                        "response"+"="+it.get("response")+", "+
                        "longitudeOrigen"+"="+it.get("longitudeOrigen")+", "+
                        "longitudeDestino"+"="+it.get("longitudeDestino")+", "+
                        "viajeId"+"="+it.get("viajeId")+", "+
                        "travelRate"+"="+it.get("travelRate")+", "+
                        "destinationAddress"+"="+it.get("destinationAddress")+", "+
                        "latitudeDestino"+"="+it.get("latitudeDestino")+"}"
            }else if (it.get("response")!=null){

                valor="{"+"userId"+"="+it.get("userId")+", "+
                        "response"+"="+it.get("response")+"}"
            }
            /* if (it.containsKey())
             for (key in it.keySet()) {
                 Switch(key){

                 }
                 value = value + "Key: $key Value: ${intent.extras?.get(key)}"+"\n"
             }-*/

         }*/
        if (!valor.contains("nulo"))
            Log.d("MainActivity22",valor )
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE)

        val intentLogin = Intent(this, LoginActivity::class.java)
        val intentMain = Intent(this, MainActivity::class.java)

        if(!TextUtils.isEmpty(getUserEmail(prefs)) &&
            !TextUtils.isEmpty(getUserPassword(prefs))){
            if (valor.contains("nulo")){

                startActivity(intentMain)
                finish()
            }
            else{
                if (valor.contains("{")){
                    //  setdataNotification_noti(dataDriver,valor)
                }

                startActivity(intentMain)
                finish()
            }
        }else{
            startActivity(intentLogin)
            finish()
        }

    }
}
