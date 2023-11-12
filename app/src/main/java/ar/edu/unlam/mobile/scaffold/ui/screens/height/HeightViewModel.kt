package ar.edu.unlam.mobile.scaffold.ui.screens.height

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffold.R
import ar.edu.unlam.mobile.scaffold.core.util.UiEvent
import ar.edu.unlam.mobile.scaffold.core.util.UiText
import ar.edu.unlam.mobile.scaffold.domain.preferences.Preferences
import ar.edu.unlam.mobile.scaffold.domain.usecase.FilterOutDigits
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeightViewModel @Inject constructor(
    private val preferences: Preferences,
    private val filterOutDigits: FilterOutDigits,
) : ViewModel() {
    var height by mutableStateOf("160")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onHeightEnter(height: String) {
        val heightInt= height.toIntOrNull()
        if (height.length <= 3 && (heightInt == null || heightInt <= 210 )) {
            this.height = filterOutDigits(height)
        }
    }

    fun onNextClick() {
        viewModelScope.launch {
            val heightNumber = height.toIntOrNull() ?: kotlin.run {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_height_cant_be_empty)),
                )
                return@launch
            }
            preferences.saveHeight(heightNumber)
            _uiEvent.send(UiEvent.Success)
        }
    }
}
