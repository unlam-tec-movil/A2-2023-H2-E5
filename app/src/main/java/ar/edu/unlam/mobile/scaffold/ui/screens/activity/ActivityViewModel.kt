package ar.edu.unlam.mobile.scaffold.ui.screens.activity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffold.core.util.UiEvent
import ar.edu.unlam.mobile.scaffold.domain.model.ActivityLevel
import ar.edu.unlam.mobile.scaffold.domain.preferences.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val preferences: Preferences,
) : ViewModel() {

    var selectedActivityLevel by mutableStateOf<ActivityLevel>(ActivityLevel.Medium)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onActivityLevelClick(activityLevel: ActivityLevel) {
        selectedActivityLevel = activityLevel
    }

    fun onNextClick() {
        viewModelScope.launch {
            preferences.saveActivityLevel(selectedActivityLevel)
            _uiEvent.send(UiEvent.Success)
        }
    }
}
