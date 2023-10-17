package ar.edu.unlam.mobile.scaffold.ui.screens

import ar.edu.unlam.mobile.scaffold.domain.model.TrackableFood

data class TrackableFoodUiState(
    val food: TrackableFood,
    val isExpanded: Boolean = false,
    val amount: String = ""
)
