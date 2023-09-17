package ar.edu.unlam.mobile.scaffold.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffold.domain.kitty.models.Kitty
import ar.edu.unlam.mobile.scaffold.domain.kitty.services.KittyGetter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@Immutable
sealed interface KittyUIState {
    data class Success(val kitty: Kitty) : KittyUIState
    object Loading : KittyUIState
    data class Error(val message: String) : KittyUIState
}

data class HomeUIState(
    val kittyState: KittyUIState = KittyUIState.Loading,
)

@HiltViewModel
class HomeViewModel @Inject constructor(val kittyGetter: KittyGetter) : ViewModel() {
    // Mutable State Flow contiene un objeto de estado mutable. Simplifica la operación de actualización de
    // información y de manejo de estados de una aplicación: Cargando, Error, Éxito.
    // (https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)
    // _Kitty State es el estado del componente "Kitty" inicializado como "Cargando"
    private val _kittyState = MutableStateFlow(KittyUIState.Loading)

    // Ui State es el estado general del view model.
    private val _uiState = MutableStateFlow(
        HomeUIState(_kittyState.value),
    )

    // UI expone el estado anterior como un Flujo de Estado de solo lectura.
    // Esto impide que se pueda modificar el estado desde fuera del ViewModel.
    val uiState = _uiState.asStateFlow()

    init {
        getKitty()
    }

    fun getKitty() {
        viewModelScope.launch {
            kittyGetter.getKitty().collect {
                _uiState.value = HomeUIState(KittyUIState.Success(it))
            }
        }
    }
}
