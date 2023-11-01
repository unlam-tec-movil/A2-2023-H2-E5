package ar.edu.unlam.mobile.scaffold.ui.screens

data class SearchState(
    val query: String = "",
    val isHintVisible: Boolean = true,
    val isSearching: Boolean = false,
    val trackableFoods: List<TrackableFoodUiState> = emptyList(),
)
