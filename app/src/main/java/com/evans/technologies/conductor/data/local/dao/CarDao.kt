package com.evans.technologies.conductor.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.evans.technologies.conductor.common.constants.Constants
import com.evans.technologies.conductor.data.local.entities.Car
import com.evans.technologies.conductor.data.local.entities.PhotosCar

@Dao
interface CarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCar(car: Car)
    @Update
    suspend fun updateCar(car: Car)
//    @Query("UPDATE ${Constants.NAME_TABLE_CAR} SET photosCar =:photosCar WHERE id=:id   ")
//    suspend fun updatePhotosCar(id:Int,photosCar: PhotosCar)
    @Query("SELECT * FROM ${Constants.NAME_TABLE_CAR} WHERE ${Constants.NAME_TABLE_CAR}.id=:id")
    fun selectCarById(id: Int):LiveData<Car>

}