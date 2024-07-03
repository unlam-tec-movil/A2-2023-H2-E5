package ar.edu.unlam.mobile.scaffold.ui.screens.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ar.edu.unlam.mobile.scaffold.data.local.LocationDao
import ar.edu.unlam.mobile.scaffold.data.local.entity.LocationEntity
import ar.edu.unlam.mobile.scaffold.data.repository.LocationTrackingRepository
import ar.edu.unlam.mobile.scaffold.data.repository.LocationTrackingRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.time.LocalDateTime

class MapViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var dao: LocationDao

    @Mock
    private lateinit var repository: LocationTrackingRepository

    @Before
    fun setup() {
        dao = mock(LocationDao::class.java)
        repository = LocationTrackingRepositoryImpl(dao)
    }

    @Test
    fun seGuardanLosDatosDeLocalizacion() =
        runTest {
            // Dado
            val punto = LocationEntity(-34.670905396924034, -58.562866152042936, LocalDateTime.now().dayOfMonth)
            val listaEsperada = listOf(punto)

            // Cuando
            repository.insertLocationPoint(punto)
            val puntosObtenidosFlow = repository.getLocationPoints()
            var puntosObtenidos = puntosObtenidosFlow.first()

            // Verificacion del Insert
            verify(dao, times(1)).insertLocationPoint(punto)

            // Entonces
            assert(puntosObtenidos.isNotEmpty())
            assert(puntosObtenidos.contains(punto))
        }
}
