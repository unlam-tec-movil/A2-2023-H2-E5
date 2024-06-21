package ar.edu.unlam.mobile.scaffold.ui.screens.podometer

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class StepCounter(
    val steps: Int,
    val goal: Int
)

class PodometerViewModel(application: Application) : AndroidViewModel(application), SensorEventListener {

    private val _stepCounter = MutableLiveData(StepCounter(0, 10000)) // Default goal 10000
    val stepCounter: LiveData<StepCounter> get() = _stepCounter

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> get() = _progress

    private val _activityTime = MutableLiveData(0)
    val activityTime: LiveData<Int> get() = _activityTime

    private val _activityCalories = MutableLiveData(0)
    val activityCalories: LiveData<Int> get() = _activityCalories

    val totalCaloriesBurned: Int
        get() = calculateTotalCaloriesBurned()

    val activityDistance: Double
        get() = calculateActivityDistance()

    private val sensorManager: SensorManager = application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    private var initialStepCount: Int? = null
    private val sharedPreferences: SharedPreferences = application.getSharedPreferences("StepCounterPrefs", Context.MODE_PRIVATE)

    init {
        // Cargar el último valor registrado desde SharedPreferences al iniciar la aplicación
        val lastStepCount = sharedPreferences.getInt("lastStepCount", 0)
        initialStepCount = lastStepCount
        updateSteps(0) // Actualizar UI con el último valor registrado
        stepSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
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
        // No necesitamos manejar los cambios de precisión
    }

    private fun updateSteps(steps: Int) {
        val goal = _stepCounter.value?.goal ?: 10000
        _stepCounter.value = StepCounter(steps, goal)
        _progress.value = (steps * 100) / goal
        // Guardar el último valor de pasos en SharedPreferences
        sharedPreferences.edit().putInt("lastStepCount", steps).apply()

        // Simular actualización de tiempo de actividad y calorías
        _activityTime.value = (steps / 100) * 10 // Simulación
        _activityCalories.value = (steps / 100) * 5 // Simulación
    }

    private fun calculateTotalCaloriesBurned(): Int {
        return _activityCalories.value ?: 0
    }

    private fun calculateActivityDistance(): Double {
        return (_stepCounter.value?.steps ?: 0) * 0.0008 // Simulación
    }

    fun setGoal(goal: Int) {
        val steps = _stepCounter.value?.steps ?: 0
        _stepCounter.value = StepCounter(steps, goal)
        _progress.value = (steps * 100) / goal
    }

    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(this)
    }
}
