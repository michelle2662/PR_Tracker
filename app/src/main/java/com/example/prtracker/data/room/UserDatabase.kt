package com.example.prtracker.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.prtracker.data.room.converters.DateConverter
import com.example.prtracker.data.room.models.Lifts
import com.example.prtracker.data.room.models.PR
import com.example.prtracker.data.room.models.User

@TypeConverters(value = [DateConverter::class])
@Database(
    entities = [User::class, Lifts::class, PR::class],
    version = 1,
    exportSchema = false
)

abstract class UserDatabase:RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun liftsDao(): LiftsDao
    abstract fun prDao(): PRDao

    companion object{
        @Volatile
        var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    UserDatabase::class.java,
                    "user_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }


}