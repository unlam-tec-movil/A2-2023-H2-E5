package ar.edu.unlam.mobile.scaffold.ui.screens.pedometer

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.TimeUnit

data class StepCounter(val steps: Int, val goal: Int)

class PodometerViewModel(application: Application) : AndroidViewModel(application), SensorEventListener {
    private val _stepCounter = MutableLiveData(StepCounter(0, 10000))
    val stepCounter: LiveData<StepCounter> = _stepCounter

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> = _progress

    private val _activityTime = MutableLiveData(0L)
    val activityTime: LiveData<Long> = _activityTime

    private val _activityDistance = MutableLiveData(0.0)
    val activityDistance: LiveData<Double> = _activityDistance

    private val _activityCalories = MutableLiveData(0)
    val activityCalories: LiveData<Int> = _activityCalories

    private val sensorManager: SensorManager = application.getSystemService(SensorManager::class.java)
    private val stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    private var initialSteps: Int? = null
    private var startTime: Long = 0L

    init {
        registerSensorListener()
    }

    private fun registerSensorListener() {
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            val totalSteps = event.values[0].toInt()
            if (initialSteps == null) {
                initialSteps = totalSteps
                startTime = System.currentTimeMillis()
            }
            val steps = totalSteps - (initialSteps ?: totalSteps)
            _stepCounter.value = StepCounter(steps, _stepCounter.value?.goal ?: 10000)
            _progress.value = ((steps / (_stepCounter.value?.goal ?: 10000).toFloat()) * 100).toInt()

            val elapsedTime = System.currentTimeMillis() - startTime
            _activityTime.value = TimeUnit.MILLISECONDS.toSeconds(elapsedTime)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No necesita manejar cambios de precisión en este caso
    }

    fun updateActivityDistance(distance: Double) {
        _activityDistance.value = distance
    }

    fun updateActivityCalories(calories: Int) {
        _activityCalories.value = calories
    }

    fun resetCounters() {
        _stepCounter.value = StepCounter(0, 10000)
        _progress.value = 0
        _activityTime.value = 0L
        _activityDistance.value = 0.0
        _activityCalories.value = 0
        initialSteps = null
        startTime = System.currentTimeMillis()
    }
}
