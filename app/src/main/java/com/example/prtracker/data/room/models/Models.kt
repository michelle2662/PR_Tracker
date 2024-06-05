package com.example.prtracker.data.room.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDate
import java.util.Date

@Entity(tableName = "user")
data class User(

    @ColumnInfo(name = "user_id")
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val first_name:String,
    val last_name:String,
    val gender:String
)

@Entity(tableName= "lifts")
data class Lifts(
    @ColumnInfo(name = "lifts_id")
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    var lift:String,
)

@Entity(tableName = "prs",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Lifts::class,
            parentColumns = ["lifts_id"],
            childColumns = ["lifts_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PR(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pr_id")
    val id: Int = 0,
    @ColumnInfo(name = "user_id")
    val userId: Int,
    @ColumnInfo(name = "lifts_id")
    val liftId: Int,
    val weight: Double,
    val repetitions: Int,
    val date: Date
)

data class LiftWithPR(
    @Embedded val pr: PR,
    @Relation(
        parentColumn = "lifts_id",
        entityColumn = "lifts_id"
    )
    val lift: Lifts
)