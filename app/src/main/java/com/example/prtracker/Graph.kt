package com.example.prtracker

import android.content.Context
import android.service.autofill.UserData
import com.example.prtracker.data.room.UserDatabase
import com.example.prtracker.ui.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

object Graph {
    private val applicationScope = CoroutineScope(SupervisorJob())

    lateinit var db: UserDatabase
        private set

    val repository by lazy {
        Repository(
            userDao = db.userDao(),
            liftsDao = db.liftsDao(),
            prDao = db.prDao()
        )
    }

    fun provide(context: Context) {
        db = UserDatabase.getDatabase(context, applicationScope)
    }
}