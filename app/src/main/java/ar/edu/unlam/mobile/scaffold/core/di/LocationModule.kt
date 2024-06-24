package ar.edu.unlam.mobile.scaffold.di

import android.content.Context
import ar.edu.unlam.mobile.scaffold.core.map.ILocationService
import ar.edu.unlam.mobile.scaffold.core.map.LocationService
import ar.edu.unlam.mobile.scaffold.data.repository.LocationTrackingRepository
import ar.edu.unlam.mobile.scaffold.domain.map.ClearPreviousActivityUseCase
import ar.edu.unlam.mobile.scaffold.domain.map.GetCurrentActivityStateUseCase
import ar.edu.unlam.mobile.scaffold.domain.map.SaveCurrentActivityUseCase
import ar.edu.unlam.mobile.scaffold.domain.usecase.LocationUseCases
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Singleton
    @Provides
    fun provideLocationClient(
        @ApplicationContext context: Context
    ): ILocationService = LocationService(
        context,
        LocationServices.getFusedLocationProviderClient(context)
    )

    @Singleton
    @Provides
    fun provideLocationUseCases(repository: LocationTrackingRepository): LocationUseCases{
        return LocationUseCases(
            getCurrentActivityState = GetCurrentActivityStateUseCase(repository),
            saveCurrentActivityState = SaveCurrentActivityUseCase(repository),
            clearPreviousActivityState = ClearPreviousActivityUseCase(repository)
        )
    }
}