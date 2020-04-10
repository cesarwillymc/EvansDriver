package com.evans.technologies.conductor.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.evans.technologies.conductor.common.constants.Constants
import com.evans.technologies.conductor.data.local.entities.Car
import com.evans.technologies.conductor.data.local.entities.Usuario
import org.jetbrains.anko.db.ConflictClause

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuario(usuario: Usuario)
    @Update
    suspend fun updateUsuario(usuario: Usuario)
    @Query("DELETE FROM ${Constants.NAME_TABLE_USER}")
    suspend fun deleteUsuario()
    @Query("SELECT * FROM ${Constants.NAME_TABLE_USER} WHERE 'id' LIKE :id" )
    fun selectUsuario(id:String):LiveData<Usuario>
//    @Query("SELECT * FROM ${Constants.NAME_TABLE_CAR} INNER JOIN ${Constants.NAME_TABLE_USER} ON ${Constants.NAME_TABLE_CAR}.usuarioUser=${Constants.NAME_TABLE_USER}.id")
//    suspend fun selectAllCars():LiveData<List<Car>>
}