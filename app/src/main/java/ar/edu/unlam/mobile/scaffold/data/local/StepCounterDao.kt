package ar.edu.unlam.mobile.scaffold.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ar.edu.unlam.mobile.scaffold.data.local.entity.StepCounterEntity

@Dao
interface StepCounterDao {
    @Insert
    suspend fun insert(stepCounter: StepCounterEntity)

    @Update
    suspend fun update(stepCounter: StepCounterEntity)

    @Query("SELECT * FROM step_counter ORDER BY id DESC LIMIT 1")
    suspend fun getLatestStepCounter(): StepCounterEntity?

    @Query("SELECT SUM(activityTime) FROM step_counter")
    suspend fun getTotalActivityTime(): Long

    @Query("SELECT SUM(activityDistance) FROM step_counter")
    suspend fun getTotalActivityDistance(): Double

    @Query("SELECT SUM(activityCalories) FROM step_counter")
    suspend fun getTotalActivityCalories(): Int
}
