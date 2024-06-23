package ar.edu.unlam.mobile.scaffold.data.local.entity


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "step_counter")
data class StepCounterEntity @RequiresApi(Build.VERSION_CODES.O) constructor(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val date: LocalDate = LocalDate.now(), // Valor predeterminado para la fecha
    val steps: Int,
    val goal: Int,
    val activityTime: Long, // Tiempo activo en milisegundos
    val activityDistance: Double, // Distancia recorrida en kilómetros
    val activityCalories: Int // Calorías quemadas
)
