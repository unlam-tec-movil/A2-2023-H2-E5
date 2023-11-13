package ar.edu.unlam.mobile.scaffold.ui.screens.weight

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
class WeightViewModel @Inject constructor(
    private val preferences: Preferences,
    private val filterOutDigits: FilterOutDigits,
) : ViewModel() {
    var weight by mutableStateOf("40.0")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val MIN_WEIGHT = 40.0
    private val MAX_WEIGHT = 250.0

    fun onHeightEnter(weight: String) {
        val weightDouble = weight.toIntOrNull()
        if (weightDouble!= null) {
            this.weight = filterOutDigits(weight)
        }
    }

    suspend fun onWeightEnter(weight: String) {
        val weightDouble = weight.toDoubleOrNull()
        if (weight.length <= 5) {
            this.weight = weight
        } else {
            _uiEvent.send(
                UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_invalid_weight)),
            )
        }
    }

    fun onNextClick() {
        viewModelScope.launch {
            val weightNumber = weight.toIntOrNull()
            if (weightNumber == null) {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_weight_cant_be_empty)),
                )
            } else if (weightNumber == 0) {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_weight_cant_be_zero)),
                )
            } else if (weightNumber < MIN_WEIGHT) {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_min_weight)),
                )
            } else if (weightNumber > MAX_WEIGHT) {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_max_weight),
                ))
            } else {
                preferences.saveHeight(weightNumber)
                _uiEvent.send(UiEvent.Success)
            }
        }
    }
}
