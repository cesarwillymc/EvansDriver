package com.evans.technologies.conductor.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.evans.technologies.conductor.common.constants.Constants

@Entity(tableName = Constants.NAME_TABLE_PHOTOS_CAR,
        foreignKeys = arrayOf(
            ForeignKey(
                entity = Car::class,
                childColumns = arrayOf("idCar"),
                parentColumns = arrayOf("id"),
                onDelete = CASCADE
            )
        )
)
data class PhotosCar (
    @PrimaryKey
    var id:Int=0,
    var licencia:String,
    var soar:String,
    var reverse:String,
    var pront:String,
    val idCar:Int
)