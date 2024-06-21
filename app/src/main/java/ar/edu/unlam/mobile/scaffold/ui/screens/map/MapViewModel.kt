package ar.edu.unlam.mobile.scaffold.ui.screens.map

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffold.data.mapper.toLocationEntity
import ar.edu.unlam.mobile.scaffold.domain.map.GetCurrentActivityStateUseCase
import ar.edu.unlam.mobile.scaffold.domain.map.GetLocationUseCase
import ar.edu.unlam.mobile.scaffold.domain.map.SaveCurrentActivityUseCase
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import ar.edu.unlam.mobile.scaffold.domain.model.Point
import kotlinx.coroutines.flow.update

@RequiresApi(Build.VERSION_CODES.S)
@HiltViewModel
class MapViewModel @Inject constructor(
    private val getLocationUseCase: GetLocationUseCase,
    private val saveCurrentActivityUseCase: SaveCurrentActivityUseCase,
    private val getCurrentActivityStateUseCase: GetCurrentActivityStateUseCase
) : ViewModel() {

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
    val viewState = _viewState.asStateFlow()

    private var _point = MutableStateFlow(listOf<Point>())
    val point = _point.asStateFlow()

    private var _locations = MutableStateFlow(listOf<LatLng>())
    val locations = _locations.asStateFlow()

    init {
        viewModelScope.launch {
            updateLocations()
            _point.value = listOf(
                Point(-34.63333, -58.56667),
                Point(-34.7, -58.58333)
            )

        }
    }

    fun handle(event: PermissionEvent) {
        when (event) {
            is PermissionEvent.Granted -> {
                viewModelScope.launch {
                    getLocationUseCase.invoke().collect {
                        _viewState.value = ViewState.Success(it)
                        if(it != null){
                            saveCurrentActivityUseCase(it)
                        }
                    }

                }
            }

            PermissionEvent.Revoked -> {
                _viewState.value = ViewState.RevokedPermissions
            }
        }
    }

    private suspend fun updateLocations(){
        getCurrentActivityStateUseCase().collect{
            _locations.value = it
        }
    }
}

sealed interface ViewState {
    object Loading : ViewState
    data class Success(val currentLocation: LatLng?) : ViewState
    object RevokedPermissions : ViewState
}

sealed interface PermissionEvent {
    object Granted : PermissionEvent
    object Revoked : PermissionEvent
}
