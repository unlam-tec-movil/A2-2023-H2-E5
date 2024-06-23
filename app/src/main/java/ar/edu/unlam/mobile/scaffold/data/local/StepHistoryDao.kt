package ar.edu.unlam.mobile.scaffold.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ar.edu.unlam.mobile.scaffold.data.local.entity.StepHistoryEntity

@Dao
interface StepHistoryDao {
    @Insert
    suspend fun insert(stepHistory: StepHistoryEntity)

    @Query("SELECT * FROM step_history WHERE date = :selectedDate")
    fun getHistoryByDate(selectedDate: String): LiveData<List<StepHistoryEntity>>

    @Query("SELECT * FROM step_history")
    fun getAllHistory(): LiveData<List<StepHistoryEntity>>
}
