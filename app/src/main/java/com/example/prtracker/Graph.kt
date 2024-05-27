package com.example.prtracker

import android.content.Context
import android.service.autofill.UserData
import com.example.prtracker.data.room.UserDatabase
import com.example.prtracker.ui.repository.Repository

object Graph {
    lateinit var db:UserDatabase
        private set

    val respository by lazy {
        Repository(
            userDao = db.userDao(),
            liftsDao = db.liftsDao(),
            prDao =  db.prDao()
        )
    }

    fun provide(context: Context){
        db = UserDatabase.getDatabase(context)
    }
}