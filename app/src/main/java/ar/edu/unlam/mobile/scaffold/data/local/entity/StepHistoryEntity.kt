package ar.edu.unlam.mobile.scaffold.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "step_history")
data class StepHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val steps: Int,
    val activeTime: Long,
    val distance: Double,
    val calories: Int
)
