package ar.edu.unlam.mobile.scaffold.core.di

import ar.edu.unlam.mobile.scaffold.domain.preferences.Preferences
import ar.edu.unlam.mobile.scaffold.domain.repository.TrackerRepository
import ar.edu.unlam.mobile.scaffold.domain.usecase.CalculateMealNutrients
import ar.edu.unlam.mobile.scaffold.domain.usecase.DeleteTrackedFood
import ar.edu.unlam.mobile.scaffold.domain.usecase.GetFoodsForDate
import ar.edu.unlam.mobile.scaffold.domain.usecase.SearchFood
import ar.edu.unlam.mobile.scaffold.domain.usecase.TrackFood
import ar.edu.unlam.mobile.scaffold.domain.usecase.TrackerUseCases
import ar.edu.unlam.mobile.scaffold.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object TrackerDomainModule {

    @ViewModelScoped
    @Provides
    fun provideTrackerUseCases(
        repository: TrackerRepository,
        preferences: Preferences
    ): TrackerUseCases {
        return TrackerUseCases(
            trackFood = TrackFood(repository),
            searchFood = SearchFood(repository),
            getFoodsForDate = GetFoodsForDate(repository),
            deleteTrackedFood = DeleteTrackedFood(repository),
            calculateMealNutrients = CalculateMealNutrients(preferences)
        )
    }
}