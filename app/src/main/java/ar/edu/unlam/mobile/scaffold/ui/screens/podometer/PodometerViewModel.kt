package ar.edu.unlam.mobile.scaffold.ui.screens.pedometer


import android.Manifest.permission.ACTIVITY_RECOGNITION
import android.app.Application
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import java.util.concurrent.TimeUnit

// Modelo de datos para contar pasos y establecer una meta
data class StepCounter(
    val steps: Int,
    val goal: Int,
)

// ViewModel para la pantalla del podómetro
class PodometerViewModel(
    application: Application,
    private val state: SavedStateHandle,
) : AndroidViewModel(application),
    SensorEventListener {

    // LiveData para el contador de pasos
    private val _stepCounter = state.getLiveData("stepCounter", StepCounter(0, 10000))
    val stepCounter: LiveData<StepCounter> = _stepCounter

    // LiveData para el progreso (en porcentaje)
    private val _progress = state.getLiveData("progress", 0)
    val progress: LiveData<Int> = _progress

    // LiveData para el tiempo activo
    private val _activityTime = state.getLiveData("activityTime", 0L)
    val activityTime: LiveData<Long> = _activityTime

    // LiveData para la distancia recorrida
    private val _activityDistance = state.getLiveData("activityDistance", 0.0)
    val activityDistance: LiveData<Double> = _activityDistance

    // LiveData para las calorías quemadas
    private val _activityCalories = state.getLiveData("activityCalories", 0)
    val activityCalories: LiveData<Int> = _activityCalories

    // Gestión del sensor de Android y el sensor de pasos
    private val sensorManager: SensorManager = application.getSystemService(Application.SENSOR_SERVICE) as SensorManager
    private val stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    // Variables para mantener el estado del conteo de pasos
    private var initialSteps: Int? = null
    private var startTime: Long = state["startTime"] ?: 0L

    // Contador de intentos de solicitud de permiso
    private var permissionRequestCount = 0

    init {
        // Inicialización del ViewModel: registrar el listener del sensor si el permiso está concedido
        if (isPermissionGranted) {
            registerSensorListener()
        }
    }

    // Propiedad para obtener o establecer el estado del permiso de actividad
    var isPermissionGranted: Boolean
        get() = state["isPermissionGranted"] ?: false
        set(value) {
            state["isPermissionGranted"] = value
            if (value) {
                // Si se concede el permiso, registrar el listener del sensor
                registerSensorListener()
                // Reiniciar contadores si es la primera vez que se obtiene el permiso
                if (initialSteps == null) {
                    resetCounters()
                }
            } else {
                // Si se revoca el permiso, detener el listener del sensor
                sensorManager.unregisterListener(this)
            }
        }

    // Método para solicitar el permiso de actividad
    @RequiresApi(Build.VERSION_CODES.Q)
    fun requestPermission() {
        if (permissionRequestCount < 2) {
            val permissionGranted =
                ContextCompat.checkSelfPermission(
                    getApplication(),
                    ACTIVITY_RECOGNITION,
                ) == PackageManager.PERMISSION_GRANTED

            if (!permissionGranted) {
                permissionRequestCount++
                // Lanzar solicitud de permiso
                onRequestPermissionResult(true) // Simulación de resultado de solicitud de permiso
            } else {
                isPermissionGranted = true
            }
        } else {
            // Manejar escenario donde el usuario deniega el permiso dos veces
        }
    }

    // Método simulado para manejar el resultado de la solicitud de permiso
    private fun onRequestPermissionResult(granted: Boolean) {
        isPermissionGranted = granted
    }

    // Método privado para registrar el listener del sensor de pasos
    private fun registerSensorListener() {
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    // Método llamado cuando cambian los datos del sensor
    override fun onSensorChanged(event: SensorEvent?) {
        // Verificar si el permiso está concedido y el evento del sensor no es nulo
        if (isPermissionGranted && event != null && event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            val totalSteps = event.values[0].toInt()
            // Si es la primera vez que se obtienen los pasos, inicializar valores iniciales y tiempo de inicio
            if (initialSteps == null) {
                initialSteps = totalSteps
                startTime = System.currentTimeMillis()
                state["startTime"] = startTime
            }
            // Calcular los pasos desde el inicio y actualizar los LiveData correspondientes
            val steps = totalSteps - (initialSteps ?: totalSteps)
            _stepCounter.value = StepCounter(steps, _stepCounter.value?.goal ?: 10000)
            _progress.value = ((steps / (_stepCounter.value?.goal ?: 10000).toFloat()) * 100).toInt()

            // Calcular el tiempo activo transcurrido en segundos
            val elapsedTime = System.currentTimeMillis() - startTime
            _activityTime.value = TimeUnit.MILLISECONDS.toSeconds(elapsedTime)

            // Calcular la distancia y las calorías quemadas
            val distance = calculateDistance(steps)
            _activityDistance.value = distance
            val calories = calculateCalories(distance)
            _activityCalories.value = calories
        }
    }

    // Método llamado cuando cambia la precisión del sensor (no se maneja en este caso)
    override fun onAccuracyChanged(
        sensor: Sensor?,
        accuracy: Int,
    ) {
        // No es necesario manejar cambios de precisión en este caso
    }

    // Método para calcular la distancia recorrida en kilómetros
    private fun calculateDistance(steps: Int): Double {
        // Se asume una longitud promedio de paso de 0.78 metros
        val stepLength = 0.78
        return steps * stepLength / 1000 // Convertir a kilómetros
    }

    // Método para calcular las calorías quemadas basadas en la distancia recorrida
    private fun calculateCalories(distance: Double): Int {
        // Se asume que se queman 50 calorías por kilómetro
        val caloriesPerKm = 50
        return (distance * caloriesPerKm).toInt()
    }

    // Método para reiniciar todos los contadores y valores relacionados
    private fun resetCounters() {
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
