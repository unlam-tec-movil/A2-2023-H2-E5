package ar.edu.unlam.mobile.scaffold.domain.usecase

import ar.edu.unlam.mobile.scaffold.domain.model.TrackedFood
import ar.edu.unlam.mobile.scaffold.domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GetFoodsForDate(
    private val repository: TrackerRepository,
) {

    operator fun invoke(
        date: LocalDate,
    ): Flow<List<TrackedFood>> {
        return repository.getFoodsForDate(date)
    }
}
