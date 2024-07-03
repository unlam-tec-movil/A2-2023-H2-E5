package ar.edu.unlam.mobile.scaffold.ui.screens.home

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffold.domain.preferences.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Anotación para indicar que este ViewModel es gestionado por Hilt para inyección de dependencias
@HiltViewModel
class HomeViewModel @Inject constructor(
    context: Context,
    preferences: Preferences
) : ViewModel(), SensorEventListener {

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Obtiene el sensor de contador de pasos
        private val stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        // Flujo mutable para el conteo de pasos
        // MutableStateFlow: Mantener un estado que puede ser observado y actualizado.
        // _stepCount es privado y solo puede ser modificado dentro del ViewModel.
        private val _stepCount = MutableStateFlow(0)

        // Flujo público e inmutable para el conteo de pasos
        val stepCount: StateFlow<Int> = _stepCount
        // StateFlow es la interfaz inmutable de MutableStateFlow. Solo permite leer el valor, pero no modificarlo.
        // stepCount es público, permitiendo que otras partes de la aplicación puedan observar los cambios
        // en el conteo de pasos sin poder modificar el valor directamente.

        // Flujo mutable para el estado de precisión del sensor
        private val _accuracyStatus = MutableStateFlow("")

        // Flujo público e inmutable para el estado de precisión del sensor
        val accuracyStatus: StateFlow<String> = _accuracyStatus

        // Función para iniciar el podómetro
        fun startPedometer() {
            stepSensor?.also { sensor ->
                // Registra el listener para el sensor de pasos
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            }
        }

        // Función para detener el podómetro
        fun stopPedometer() {
            sensorManager.unregisterListener(this)
        }

        // Se llama cuando el sensor detecta un cambio en sus datos
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                    // Actualiza el valor del contador de pasos
                    _stepCount.value = it.values[0].toInt()
                }
            }
        }

        // Se llama cuando cambia la precisión del sensor
        override fun onAccuracyChanged(
            sensor: Sensor?,
            accuracy: Int,
        ) {
            if (sensor?.type == Sensor.TYPE_STEP_COUNTER) {
                viewModelScope.launch {
                    // Actualiza el estado de precisión basado en el nuevo valor de precisión
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
                            // Detiene el podómetro si la precisión es no confiable
                            stopPedometer()
                            _stepCount.value = 0 // Reinicia el contador de pasos
                        }
                    }
                }
            }
        }
    }
