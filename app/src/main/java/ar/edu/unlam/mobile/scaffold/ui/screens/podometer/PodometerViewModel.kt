package ar.edu.unlam.mobile.scaffold.ui.screens.podometer

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.text.SimpleDateFormat
import java.util.*

data class StepCounter(
    val steps: Int,
    val goal: Int
)

data class StepHistory(
    val date: String,
    val steps: Int,
    val activeTime: String,
    val distance: Double,
    val calories: Int
)

class PodometerViewModel(application: Application) : AndroidViewModel(application), SensorEventListener {

    private val _stepCounter = MutableLiveData(StepCounter(0, 10000)) // Default goal 10000
    val stepCounter: LiveData<StepCounter> get() = _stepCounter

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> get() = _progress

    private val _activityTime = MutableLiveData(0L)
    val activityTime: LiveData<Long> get() = _activityTime

    private val _activityCalories = MutableLiveData(0)
    val activityCalories: LiveData<Int> get() = _activityCalories

    private val _activityDistance = MutableLiveData(0.0)
    val activityDistance: LiveData<Double> get() = _activityDistance

    private val _stepHistory = MutableLiveData<List<StepHistory>>(emptyList())
    val stepHistory: LiveData<List<StepHistory>> get() = _stepHistory

    private val sensorManager: SensorManager = application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    private var initialStepCount: Int? = null
    private val sharedPreferences: SharedPreferences = application.getSharedPreferences("StepCounterPrefs", Context.MODE_PRIVATE)

    init {
        // Load last step count from SharedPreferences on app start
        val lastStepCount = sharedPreferences.getInt("lastStepCount", 0)
        initialStepCount = lastStepCount
        updateSteps(0) // Update UI with the last registered value
        stepSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }

        // Load history from SharedPreferences
        loadHistory()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                if (initialStepCount == null) {
                    initialStepCount = it.values[0].toInt()
                }
                val steps = it.values[0].toInt() - (initialStepCount ?: 0)
                updateSteps(steps)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // We don't need to handle accuracy changes
    }

    private fun updateSteps(steps: Int) {
        val goal = _stepCounter.value?.goal ?: 10000
        _stepCounter.value = StepCounter(steps, goal)
        _progress.value = (steps * 100) / goal
        // Save the last step count to SharedPreferences
        sharedPreferences.edit().putInt("lastStepCount", steps).apply()

        // Simulate updating activity time and calories
        _activityTime.value = steps.toLong() // Simulation: 1 step = 1 second of activity
        _activityCalories.value = (steps / 20) // Simulation: 1 step = 0.05 calories
        _activityDistance.value = calculateActivityDistance() // Calculate distance

        // Save daily data to history
        saveDailyData()
    }

    private fun calculateTotalCaloriesBurned(): Int {
        return _activityCalories.value ?: 0
    }

    private fun calculateActivityDistance(): Double {
        return (_stepCounter.value?.steps ?: 0) * 0.0008 // Simulation
    }

    private fun saveDailyData() {
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate = dateFormatter.format(Date())
        val steps = _stepCounter.value?.steps ?: 0
        val activeTime = formatActiveTime(_activityTime.value ?: 0L)
        val distance = calculateActivityDistance()
        val calories = _activityCalories.value ?: 0

        val history = _stepHistory.value.orEmpty().toMutableList()
        val existingDataIndex = history.indexOfFirst { it.date == currentDate }

        if (existingDataIndex != -1) {
            history[existingDataIndex] = StepHistory(currentDate, steps, activeTime, distance, calories)
        } else {
            history.add(StepHistory(currentDate, steps, activeTime, distance, calories))
        }

        _stepHistory.value = history
        saveHistoryToPreferences(history)
    }

    private fun saveHistoryToPreferences(history: List<StepHistory>) {
        val editor = sharedPreferences.edit()
        editor.putInt("history_size", history.size)
        history.forEachIndexed { index, item ->
            editor.putString("history_$index", "${item.date},${item.steps},${item.activeTime},${item.distance},${item.calories}")
        }
        editor.apply()
    }

    private fun loadHistory() {
        val historySize = sharedPreferences.getInt("history_size", 0)
        val history = mutableListOf<StepHistory>()
        for (i in 0 until historySize) {
            sharedPreferences.getString("history_$i", null)?.let { data ->
                val parts = data.split(",")
                if (parts.size == 5) {
                    val date = parts[0]
                    val steps = parts[1].toInt()
                    val activeTime = parts[2]
                    val distance = parts[3].toDouble()
                    val calories = parts[4].toInt()
                    history.add(StepHistory(date, steps, activeTime, distance, calories))
                }
            }
        }
        _stepHistory.value = history
    }

    fun filterHistoryByDate(date: String) {
        if (date.isBlank()) {
            // Si la fecha proporcionada está vacía, mostramos todo el historial
            _stepHistory.value = loadFullHistoryFromPreferences()
        } else {
            // Filtrar el historial por la fecha proporcionada
            val filteredHistory = _stepHistory.value.orEmpty().filter { it.date == date }
            _stepHistory.value = filteredHistory
        }
    }

    private fun loadFullHistoryFromPreferences(): List<StepHistory> {
        val historySize = sharedPreferences.getInt("history_size", 0)
        val history = mutableListOf<StepHistory>()
        for (i in 0 until historySize) {
            sharedPreferences.getString("history_$i", null)?.let { data ->
                val parts = data.split(",")
                if (parts.size == 5) {
                    val date = parts[0]
                    val steps = parts[1].toInt()
                    val activeTime = parts[2]
                    val distance = parts[3].toDouble()
                    val calories = parts[4].toInt()
                    history.add(StepHistory(date, steps, activeTime, distance, calories))
                }
            }
        }
        return history
    }

    private fun formatActiveTime(activeTime: Long): String {
        val minutes = activeTime / 60
        val seconds = activeTime % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun handlePermissionDenied(context: Context) {
        Toast.makeText(context, "Permiso de reconocimiento de actividad denegado.", Toast.LENGTH_LONG).show()
    }

    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(this)
    }
}
