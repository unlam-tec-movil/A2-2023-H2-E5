package ar.edu.unlam.mobile.scaffold.domain.usecase

import ar.edu.unlam.mobile.scaffold.domain.map.GetCurrentActivityStateUseCase
import ar.edu.unlam.mobile.scaffold.domain.map.SaveCurrentActivityUseCase

data class LocationUseCases(
    val getCurrentActivityState: GetCurrentActivityStateUseCase,
    val saveCurrentActivityState: SaveCurrentActivityUseCase
)
