package com.rak12.gymapp.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [CuisineEntity::class], version = 1)

abstract class Database : RoomDatabase() {
    abstract fun cusdao(): Dao

}


