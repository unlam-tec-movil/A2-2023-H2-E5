import android.app.Application
import android.hardware.SensorManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import ar.edu.unlam.mobile.scaffold.ui.screens.pedometer.PodometerViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

// Declaración de la clase de prueba para PodometerViewModel
class PodometerViewModelTest {
    // Regla para ejecutar tareas en el hilo principal de manera síncrona
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Declaración de variables necesarias para las pruebas
    private lateinit var viewModel: PodometerViewModel
    private lateinit var application: Application
    private lateinit var state: SavedStateHandle
    private lateinit var sensorManager: SensorManager

    // Método que prepara el entorno de prueba antes de cada test
    @Before
    fun setup() {
        // Creación de instancias simuladas para Application y SensorManager
        application = mock(Application::class.java)
        state = SavedStateHandle()
        sensorManager = mock(SensorManager::class.java)

        // Inicialización del ViewModel con las instancias simuladas y SavedStateHandle vacío
        viewModel = PodometerViewModel(application, state, sensorManager)
    }

    // Prueba para verificar la inicialización de datos del ViewModel
    @Test
    fun testUpdateData() {
        // Verifica que los valores iniciales sean los esperados
        assert(viewModel.stepCounter.value?.steps == 0)
        assert(viewModel.stepCounter.value?.goal == 10000)
        assert(viewModel.progress.value == 0)
        assert(viewModel.activityTime.value == 0L)
        assert(viewModel.activityDistance.value == 0.0)
        assert(viewModel.activityCalories.value == 0)
    }

    // Prueba para verificar el manejo de permisos concedidos
    @Test
    fun testPermissionGranted() {
        // Establece que el permiso está concedido y verifica
        viewModel.isPermissionGranted = true
        assert(viewModel.isPermissionGranted)
    }

    // Prueba para verificar el cálculo de distancia
    @Test
    fun testCalculateDistance() {
        // Llama al método de cálculo de distancia y verifica el resultado aproximado
        val distance = viewModel.calculateDistance(1000)
        assert(distance == 0.78)
    }

    // Prueba para verificar el cálculo de calorías
    @Test
    fun testCalculateCalories() {
        // Llama al método de cálculo de calorías y verifica el resultado esperado
        val calories = viewModel.calculateCalories(1.0)
        assert(calories == 50)
    }
}
