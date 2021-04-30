package com.rak12.gymapp.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {
    @Insert
    fun insert(cuisineEntity: CuisineEntity)

    @Delete
    fun delete(cuisineEntity: CuisineEntity)

    @Query("SELECT * FROM CUISINE")
    fun getall(): List<CuisineEntity>

    @Query("SELECT * FROM CUISINE WHERE ID=:id")
    fun getbyid(id: Int): CuisineEntity



}