package ar.edu.unlam.mobile.scaffold.domain.map

import ar.edu.unlam.mobile.scaffold.data.repository.LocationTrackingRepository
import javax.inject.Inject

class ClearPreviousActivityUseCase @Inject constructor(
    private val repository: LocationTrackingRepository
) {
    suspend operator fun invoke(day: Int) {
        repository.clearLocationPoints(day)
    }
}
