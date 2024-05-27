package com.example.prtracker.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.prtracker.data.room.converters.DateConverter
import com.example.prtracker.data.room.models.Lifts
import com.example.prtracker.data.room.models.PR
import com.example.prtracker.data.room.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@TypeConverters(value = [DateConverter::class])
@Database(
    entities = [User::class, Lifts::class, PR::class],
    version = 1,
    exportSchema = false
)

abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun liftsDao(): LiftsDao
    abstract fun prDao(): PRDao

    private class UserDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.userDao())
                }
            }
        }

        suspend fun populateDatabase(userDao: UserDao) {
            // Delete all content here.
            userDao.deleteAll()

            // Add sample users.
            var user = User(id = 0, first_name = "MICHELLE", last_name = "KWAN", gender = "Female")
            userDao.insert(user)
            user = User(id = 0, first_name = "CURTIS", last_name = "KROETSCH", gender = "Male")
            userDao.insert(user)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                )
                    .addCallback(UserDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}