package com.evans.technologies.conductor.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.evans.technologies.conductor.common.constants.Constants

@Entity(tableName = Constants.NAME_TABLE_CAR,
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Usuario::class,
            childColumns = arrayOf("usuarioUser"),
            parentColumns = arrayOf("id"),
            onDelete = CASCADE
        )
    )
)
data class Car (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var marca:String,
    var modelo:String,
    var color:String,
    var año:String,
    var placa:String,
    var categoria:String,
    var usuarioUser: String,
    var select:Boolean=true
)