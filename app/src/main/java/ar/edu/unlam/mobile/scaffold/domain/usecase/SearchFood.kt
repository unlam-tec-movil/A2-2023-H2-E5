package ar.edu.unlam.mobile.scaffold.domain.usecase

import ar.edu.unlam.mobile.scaffold.domain.model.TrackableFood
import ar.edu.unlam.mobile.scaffold.domain.repository.TrackerRepository

class SearchFood(
    private val repository: TrackerRepository,
) {

    suspend operator fun invoke(
        query: String,
        page: Int = 1,
        pageSize: Int = 40,
    ): Result<List<TrackableFood>> {
        if (query.isBlank()) {
            return Result.success(emptyList())
        }
        return repository.searchFood(query.trim(), page, pageSize)
    }
}
