package ar.edu.unlam.mobile.scaffold.core.di

import ar.edu.unlam.mobile.scaffold.domain.usecase.ValidateNutrients
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnboardingDomainModule {

    @Provides
    @Singleton
    fun provideValidateNutrientUseCase(): ValidateNutrients {
        return ValidateNutrients()
    }
}
