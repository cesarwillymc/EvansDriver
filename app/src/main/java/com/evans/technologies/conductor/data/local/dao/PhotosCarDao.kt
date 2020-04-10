package com.evans.technologies.conductor.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.evans.technologies.conductor.common.constants.Constants
import com.evans.technologies.conductor.data.local.entities.PhotosCar

@Dao
interface PhotosCarDao {
    @Query("SELECT * FROM ${Constants.NAME_TABLE_PHOTOS_CAR} WHERE ${Constants.NAME_TABLE_PHOTOS_CAR}.id =:id")
    fun selectphotosCarById(id: Int): LiveData<PhotosCar>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotosCar(photosCar: PhotosCar)
    @Update
    suspend fun updatePhotosCar(photosCar: PhotosCar)
}