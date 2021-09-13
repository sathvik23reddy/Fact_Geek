package com.example.factgeek

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Fact::class), version=1, exportSchema = false)
abstract class FactDatabase:RoomDatabase() {
    abstract fun getFactDao():FactDao

    companion object{
        @Volatile
        private var INSTANCE:FactDatabase ?= null

        fun getDatabase(context: Context): FactDatabase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FactDatabase::class.java,
                    "fact_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}