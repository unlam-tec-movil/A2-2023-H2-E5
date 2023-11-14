package ar.edu.unlam.mobile.scaffold.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import ar.edu.unlam.mobile.scaffold.data.local.TrackerDao
import ar.edu.unlam.mobile.scaffold.data.mapper.toTrackableFood
import ar.edu.unlam.mobile.scaffold.data.mapper.toTrackedFood
import ar.edu.unlam.mobile.scaffold.data.mapper.toTrackedFoodEntity
import ar.edu.unlam.mobile.scaffold.data.remote.OpenFoodApi
import ar.edu.unlam.mobile.scaffold.domain.model.TrackableFood
import ar.edu.unlam.mobile.scaffold.domain.model.TrackedFood
import ar.edu.unlam.mobile.scaffold.domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class TrackerRepositoryImpl(
    private val dao: TrackerDao,
    private val api: OpenFoodApi,
) : TrackerRepository {
    override suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int,
    ): Result<List<TrackableFood>> {
        return try {
            val searchDto = api.searchFood(
                query = query,
                page = page,
                pageSize = pageSize,
            )
            Result.success(
                searchDto.products
                    .filter {
                        val calculatedCalories =
                            it.nutriments.carbohydrates100g * 4f +
                                it.nutriments.proteins100g * 4f +
                                it.nutriments.fat100g * 9f
                        val lowerBound = calculatedCalories * 0.99f
                        val upperBound = calculatedCalories * 1.01f
                        it.nutriments.energyKcal100g in (lowerBound..upperBound) &&
                            it.nutriments.energyKcal100g != 0.0
                    }
                    .mapNotNull { it.toTrackableFood() },
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun insertTrackedFood(food: TrackedFood) {
        dao.insertTrackedFood(food.toTrackedFoodEntity())
    }

    override suspend fun deleteTrackedFood(food: TrackedFood) {
        dao.deleteTrackedFood(food.toTrackedFoodEntity())
    }

    override fun getFoodsForDate(localDate: LocalDate): Flow<List<TrackedFood>> {
        return dao.getFoodsForDate(
            day = localDate.dayOfMonth,
            month = localDate.monthValue,
            year = localDate.year,
        ).map { entities ->
            entities.map { it.toTrackedFood() }
        }
    }
}
