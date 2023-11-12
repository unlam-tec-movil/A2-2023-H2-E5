package ar.edu.unlam.mobile.scaffold.domain.usecase

import ar.edu.unlam.mobile.scaffold.domain.model.TrackedFood
import ar.edu.unlam.mobile.scaffold.domain.repository.TrackerRepository

class DeleteTrackedFood(
    private val repository: TrackerRepository,
) {

    suspend operator fun invoke(
        trackedFood: TrackedFood,
    ) {
        repository.deleteTrackedFood(trackedFood)
    }
}
