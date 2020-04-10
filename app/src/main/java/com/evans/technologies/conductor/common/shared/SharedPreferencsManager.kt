package com.evans.technologies.conductor.common.shared

import android.content.Context
import android.content.SharedPreferences
import com.evans.technologies.conductor.app.MyApp
import com.evans.technologies.conductor.common.constants.Constants.APP_SETTINGS_FILE

class SharedPreferencsManager {
    companion object{
        private fun getSharedPreferencs():SharedPreferences= MyApp.getContextApp().getSharedPreferences(APP_SETTINGS_FILE,Context.MODE_PRIVATE)

        fun setSomeStringValue(key:String,value:String){
            val editor = getSharedPreferencs().edit()
            editor.putString(key,value)
            editor.apply()
        }
        fun setSomeBooleanValue(key:String,value:Boolean){
            val editor = getSharedPreferencs().edit()
            editor.putBoolean(key,value)
            editor.apply()
        }
        fun setSomeIntValue(key:String,value:Int){
            val editor = getSharedPreferencs().edit()
            editor.putInt(key,value)
            editor.apply()
        }
        fun getSomeStringValue(key:String): String? =getSharedPreferencs().getString(key,"nulo")
        fun getSomeBooleanValue(key:String): Boolean? =getSharedPreferencs().getBoolean(key,false)
        fun getSomeIntValue(key:String): Int? =getSharedPreferencs().getInt(key,0)
        fun clearAllManagerShared(){
            getSharedPreferencs().edit().clear().apply()
        }
        fun clearItemManagerShared(key:String){
            getSharedPreferencs().edit().remove(key).apply()
        }
    }


}