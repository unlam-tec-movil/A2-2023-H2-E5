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
    var age by mutableStateOf("12")
        private set
    private val MIN_AGE = 12
    private val MAX_AGE = 99

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAgeEnter(age: String) {
        val filteredAge = filterOutDigits(age)
        this.age = filteredAge
    }

    fun onNextClick() {
        viewModelScope.launch {
            if (age.isEmpty()) {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_age_cant_be_empty))
                )
                return@launch
            }

            val ageNumber = age.toIntOrNull()
            if (ageNumber == null) {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_invalid_number))
                )
                return@launch
            }

            if (ageNumber <= MIN_AGE) {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_age_ten))
                )
                return@launch
            }

            if (ageNumber > MAX_AGE) {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_age_exceeds_limit))
                )
                return@launch
            }

            preferences.saveAge(ageNumber)
            _uiEvent.send(UiEvent.Success)
        }
    }
}
