package com.example.prtracker.ui.repository

import com.example.prtracker.data.room.LiftsDao
import com.example.prtracker.data.room.PRDao
import com.example.prtracker.data.room.UserDao
import com.example.prtracker.data.room.models.Lifts
import com.example.prtracker.data.room.models.PR
import com.example.prtracker.data.room.models.User
import kotlinx.coroutines.flow.Flow

class Repository(
    private val userDao: UserDao,
    private val liftsDao: LiftsDao,
    private val prDao: PRDao
) {
    fun getUsers(): Flow<List<User>> = userDao.getAllUsers()

    fun getUserPR(userId: Int): Flow<List<PR>> = prDao.getPRsForUser(userId)

    fun getPRForUserAndLift(userId: Int, liftId: Int): Flow<PR?> = prDao.getPRForUserAndLift(userId, liftId)

    fun getPRsForLift(liftId: Int): Flow<List<PR>> = prDao.getPRsForLift(liftId)

    suspend fun insertUser(user: User) {
        userDao.insert(user)
    }

    suspend fun insertLift(lift: Lifts) {
        liftsDao.insert(lift)
    }

    suspend fun deleteLift(lift: Lifts) {
        liftsDao.delete(lift)
    }

    suspend fun updateLift(lift: Lifts) {
        liftsDao.update(lift)
    }

    suspend fun insertPR(pr: PR) {
        prDao.insertPR(pr)
    }

    suspend fun updatePR(pr: PR) {
        prDao.updatePR(pr)
    }

    suspend fun findLiftByName(name: String): Lifts? {
        return liftsDao.findLiftByName(name)
    }
}