package com.evans.technologies.conductor.data.local.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.evans.technologies.conductor.app.MyApp
import com.evans.technologies.conductor.app.MyApp.Companion.getContextApp
import com.evans.technologies.conductor.common.constants.Constants
import com.evans.technologies.conductor.data.local.dao.CarDao
import com.evans.technologies.conductor.data.local.dao.PhotosCarDao
import com.evans.technologies.conductor.data.local.dao.UsuarioDao
import com.evans.technologies.conductor.data.local.entities.Car
import com.evans.technologies.conductor.data.local.entities.PhotosCar
import com.evans.technologies.conductor.data.local.entities.Usuario

@Database(entities = [Usuario::class, Car::class,PhotosCar::class],version = 1)
abstract class AppDB:RoomDatabase() {
    abstract val carDao:CarDao
    abstract val usuarioDao:UsuarioDao
    abstract val photosCar:PhotosCarDao
    companion object{
        private var INSTANCE:AppDB?=null
        fun getInstanceDB(): AppDB? {
            if(INSTANCE==null){
                INSTANCE= synchronized(AppDB::class){
                    Room.databaseBuilder(getContextApp(),AppDB::class.java,Constants.NAME_DATABASE)
                        .build()
                }
            }
            return INSTANCE
        }
    }
}