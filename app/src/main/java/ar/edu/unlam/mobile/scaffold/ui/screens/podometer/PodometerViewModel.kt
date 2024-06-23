package ar.edu.unlam.mobile.scaffold.ui.screens.pedometer

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import java.util.concurrent.TimeUnit

data class StepCounter(val steps: Int, val goal: Int)

class PodometerViewModel(application: Application, private val state: SavedStateHandle) : AndroidViewModel(application), SensorEventListener {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
    }

    private val _stepCounter = state.getLiveData("stepCounter", StepCounter(0, 10000))
    val stepCounter: LiveData<StepCounter> = _stepCounter

    private val _progress = state.getLiveData("progress", 0)
    val progress: LiveData<Int> = _progress

    private val _activityTime = state.getLiveData("activityTime", 0L)
    val activityTime: LiveData<Long> = _activityTime

    private val _activityDistance = state.getLiveData("activityDistance", 0.0)
    val activityDistance: LiveData<Double> = _activityDistance

    private val _activityCalories = state.getLiveData("activityCalories", 0)
    val activityCalories: LiveData<Int> = _activityCalories

    private val sensorManager: SensorManager = application.getSystemService(Application.SENSOR_SERVICE) as SensorManager
    private val stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    private var initialSteps: Int? = null
    private var startTime: Long = state["startTime"] ?: 0L

    private var permissionRequestCount = 0

    init {
        if (isPermissionGranted) {
            registerSensorListener()
        }
    }

    var isPermissionGranted: Boolean
        get() = state["isPermissionGranted"] ?: false
        set(value) {
            state["isPermissionGranted"] = value
            if (value) {
                registerSensorListener()
                if (initialSteps == null) {
                    resetCounters()
                }
            } else {
                sensorManager.unregisterListener(this)
            }
        }

    fun requestPermission() {
        if (permissionRequestCount < 2) {
            val permissionGranted = ContextCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED

            if (!permissionGranted) {
                permissionRequestCount++
                // Launch permission request
                // This should ideally be handled from your activity or fragment
                // For demonstration purposes, we use a hypothetical onRequestPermissionResult method
                // which would be called from the activity/fragment after permission request is handled.
                onRequestPermissionResult(true) // Simulating permission result callback
            } else {
                isPermissionGranted = true
            }
        } else {
            // Handle scenario where user denied permission twice
            // Optionally, you can set a flag or handle this scenario in your activity/fragment
        }
    }

    fun onRequestPermissionResult(granted: Boolean) {
        isPermissionGranted = granted
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
        if (isPermissionGranted && event != null && event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            val totalSteps = event.values[0].toInt()
            if (initialSteps == null) {
                initialSteps = totalSteps
                startTime = System.currentTimeMillis()
                state["startTime"] = startTime
            }
            val steps = totalSteps - (initialSteps ?: totalSteps)
            _stepCounter.value = StepCounter(steps, _stepCounter.value?.goal ?: 10000)
            _progress.value = ((steps / (_stepCounter.value?.goal ?: 10000).toFloat()) * 100).toInt()

            val elapsedTime = System.currentTimeMillis() - startTime
            _activityTime.value = TimeUnit.MILLISECONDS.toSeconds(elapsedTime)

            // Calculate distance and calories
            val distance = calculateDistance(steps)
            _activityDistance.value = distance
            val calories = calculateCalories(distance)
            _activityCalories.value = calories
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No need to handle accuracy changes in this case
    }

    private fun calculateDistance(steps: Int): Double {
        // Assuming average step length is 0.78 meters
        val stepLength = 0.78
        return steps * stepLength / 1000 // Convert to kilometers
    }

    private fun calculateCalories(distance: Double): Int {
        // Assuming 50 calories are burned per kilometer
        val caloriesPerKm = 50
        return (distance * caloriesPerKm).toInt()
    }

    fun resetCounters() {
        _stepCounter.value = StepCounter(0, 10000)
        _progress.value = 0
        _activityTime.value = 0L
        _activityDistance.value = 0.0
        _activityCalories.value = 0
        initialSteps = null
        startTime = System.currentTimeMillis()
        state["startTime"] = startTime
    }
}
