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
    var height by mutableStateOf("140")
        private set
    private val MIN_HEIGHT = 140
    private val MAX_HEIGHT = 215

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onHeightEnter(height: String) {
        val filteredHeight = filterOutDigits(height)
        if (filteredHeight != this.height) {
            this.height = filteredHeight
        }
    }

    fun onNextClick() {
        viewModelScope.launch {
            val heightNumber = height.toIntOrNull()
            if (heightNumber == null) {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_height_cant_be_empty)),
                )
            } else if (heightNumber <= 0) {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_height_cant_be_zero)),
                )
            } else if (heightNumber < MIN_HEIGHT) {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_height_too_short)),
                )
            } else if (heightNumber > MAX_HEIGHT) {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_height_too_tall)),
                )
            } else {
                preferences.saveHeight(heightNumber)
                _uiEvent.send(UiEvent.Success)
            }
        }
    }
}
