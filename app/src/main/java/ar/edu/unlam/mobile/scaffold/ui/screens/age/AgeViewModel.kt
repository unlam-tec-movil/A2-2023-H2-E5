package ar.edu.unlam.mobile.scaffold.ui.screens.age

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
class AgeViewModel @Inject constructor(
    private val preferences: Preferences,
    private val filterOutDigits: FilterOutDigits,
) : ViewModel() {
    var age by mutableStateOf("13")
        private set
    private val MIN_AGE = 13
    private val MAX_AGE = 100

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAgeEnter(age: String) {
        val filteredAge = filterOutDigits(age)
        if (filteredAge != this.age) {
            this.age = filteredAge
        }
    }

    fun onNextClick() {
        viewModelScope.launch {
            val ageNumber = age.toIntOrNull()
            if (ageNumber == null) {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_age_cant_be_empty)),
                )
            } else if (ageNumber <= 0) {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_age_cant_be_zero)),
                )
            } else if (ageNumber < MIN_AGE) {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_MIN_AGE)),
                )
            } else if (ageNumber > MAX_AGE) {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_MAX_AGE)),
                )
            } else {
                preferences.saveAge(ageNumber)
                _uiEvent.send(UiEvent.Success)
            }
        }
    }
}
