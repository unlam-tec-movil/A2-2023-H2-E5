package ar.edu.unlam.mobile.scaffold.ui.screens.home

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    context: Context
) : ViewModel(), SensorEventListener {

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    private val _stepCount = MutableStateFlow(0)
    val stepCount: StateFlow<Int> = _stepCount

    private val _accuracyStatus = MutableStateFlow("")
    val accuracyStatus: StateFlow<String> = _accuracyStatus

    fun startPedometer() {
        stepSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stopPedometer() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                _stepCount.value = it.values[0].toInt()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        if (sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            viewModelScope.launch {
                when (accuracy) {
                    SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> {
                        _accuracyStatus.value = "Alta precisión"
                    }
                    SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> {
                        _accuracyStatus.value = "Precisión media"
                    }
                    SensorManager.SENSOR_STATUS_ACCURACY_LOW -> {
                        _accuracyStatus.value = "Baja precisión"
                    }
                    SensorManager.SENSOR_STATUS_UNRELIABLE -> {
                        _accuracyStatus.value = "Precisión no confiable"
                        stopPedometer()
                        _stepCount.value = 0 // Reiniciar el contador de pasos
                    }
                }
            }
        }
    }
}
