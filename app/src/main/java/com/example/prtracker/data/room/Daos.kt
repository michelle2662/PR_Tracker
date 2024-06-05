package com.example.prtracker.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.prtracker.data.room.models.LiftWithPR
import com.example.prtracker.data.room.models.Lifts
import com.example.prtracker.data.room.models.PR
import com.example.prtracker.data.room.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM user")
    suspend fun deleteAll()

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<User>>
}

@Dao
interface LiftsDao{
    @Insert
    suspend fun insert(lifts: Lifts)

    @Update
    suspend fun update(lifts: Lifts)

    @Delete
    suspend fun delete(lifts: Lifts)

    @Query("SELECT * FROM lifts")
    fun getAllLifts(): Flow<List<Lifts>>
    @Query("SELECT * FROM lifts WHERE lift = :name LIMIT 1")
    suspend fun findLiftByName(name: String): Lifts?
}

@Dao
interface PRDao {

    @Query("SELECT * FROM prs")
    fun getAllPRs(): Flow<List<PR>>

    @Query("SELECT * FROM prs WHERE lifts_id = :liftId")
    fun getPRsForLift(liftId: Int): Flow<List<PR>>

    @Query("SELECT * FROM prs WHERE user_id = :userId")
     fun getPRsForUser(userId:Int):Flow<List<PR>>

     @Query("SELECT * FROM prs WHERE user_id = :userId and lifts_id = :liftId")
     fun getPRForUserAndLift(userId:Int, liftId:Int): Flow<PR?>

    @Transaction
    @Query("""
        SELECT * FROM prs 
        WHERE user_id = :userId AND (lifts_id, weight) IN (
            SELECT lifts_id, MAX(weight) 
            FROM prs 
            WHERE user_id = :userId 
            GROUP BY lifts_id
        )
    """)
    fun getUserPRsWithLifts(userId: Int): Flow<List<LiftWithPR>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPR(pr: PR)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePR(pr:PR)

    @Delete
    suspend fun deletePR(pr: PR)
}