package com.evans.technologies.conductor.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.evans.technologies.conductor.common.constants.Constants

@Entity(tableName = Constants.NAME_TABLE_USER)
data class Usuario(
    @PrimaryKey
    var id:String,
    var name:String,
    var lastName:String,
    var dni:String
)