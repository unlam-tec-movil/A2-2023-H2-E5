package ar.edu.unlam.mobile.scaffold.ui.screens.weight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ar.edu.unlam.mobile.scaffold.R
import ar.edu.unlam.mobile.scaffold.core.util.UiEvent
import ar.edu.unlam.mobile.scaffold.core.util.UiText
import ar.edu.unlam.mobile.scaffold.domain.preferences.Preferences
import javax.inject.Inject


@HiltViewModel
class WeightViewModel @Inject constructor(
    private val preferences: Preferences,
) : ViewModel() {
    var weight by mutableStateOf("40.00")
        private set
    private val MIN_WEIGHT = 40.00
    private val MAX_WEIGHT = 500.00

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private fun filterOutNonNumeric(input: String): String {
        return input.filter { it.isDigit() || it == '.' }
    }

    fun onWeightEnter(weight: String) {
        val filteredWeight = filterOutNonNumeric(weight)
        if (filteredWeight != this.weight) {
            this.weight = filteredWeight
        } else {
            // Asigna una cadena vacía si el resultado del filtrado es igual al valor actual
            this.weight = ""
        }
    }

    fun onNextClick() {
        viewModelScope.launch {
            val weightNumber = weight.toDoubleOrNull()
            if (weightNumber == null) {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_weight_cant_be_empty)),
                )
            } else if (weightNumber <= 0) {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_weight_cant_be_zero)),
                )
            } else if (weightNumber < MIN_WEIGHT) {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_MIN_weight)),
                )
            } else if (weightNumber > MAX_WEIGHT) {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_MAX_weight)),
                )
            } else {
                preferences.saveWeight(weightNumber.toFloat())
                _uiEvent.send(UiEvent.Success)
            }
        }
    }
}

