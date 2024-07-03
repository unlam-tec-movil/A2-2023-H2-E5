package ar.edu.unlam.mobile.scaffold.ui.screens.map

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffold.domain.map.GetLocationUseCase
import ar.edu.unlam.mobile.scaffold.domain.model.Point
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Requiere API nivel 31 (Android 12)
@RequiresApi(Build.VERSION_CODES.S)
@HiltViewModel
class MapViewModel
    @Inject
    constructor(
        private val getLocationUseCase: GetLocationUseCase,
    ) : ViewModel() {
        // Estado de la vista, se inicializa como Loading
        private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
        val viewState = _viewState.asStateFlow() // Estado de la vista como flujo inmutable

        // Lista mutable de puntos
        private var _point = MutableStateFlow(listOf<Point>())
        val point = _point.asStateFlow() // Lista de puntos como flujo inmutable

        init {
            // Inicialización de puntos (simulación de datos)
            viewModelScope.launch {
                _point.value =
                    listOf(
                        Point(-34.63333, -58.56667),
                        Point(-34.7, -58.58333),
                    )
            }
        }

        // Manejo de eventos de permisos
        fun handle(event: PermissionEvent) {
            when (event) {
                is PermissionEvent.Granted -> {
                    // Acción cuando se otorgan permisos
                    viewModelScope.launch {
                        getLocationUseCase.invoke().collect {
                            _viewState.value = ViewState.Success(it)
                        }
                    }
                }

                PermissionEvent.Revoked -> {
                    // Acción cuando se revocan permisos
                    _viewState.value = ViewState.RevokedPermissions
                }
            }
        }
    }

// Interfaz sellada para representar los estados de la vista
sealed interface ViewState {
    object Loading : ViewState // Estado de carga

    data class Success(
        val location: LatLng?,
    ) : ViewState // Estado de éxito con ubicación

    object RevokedPermissions : ViewState // Estado de permisos revocados
}

// Interfaz sellada para representar eventos de permisos
sealed interface PermissionEvent {
    object Granted : PermissionEvent // Evento de permisos otorgados

    object Revoked : PermissionEvent // Evento de permisos revocados
}
