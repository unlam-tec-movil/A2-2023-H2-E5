package ar.edu.unlam.mobile.scaffold.domain.kitty.di

import ar.edu.unlam.mobile.scaffold.domain.kitty.services.KittyGetter
import ar.edu.unlam.mobile.scaffold.domain.kitty.services.KittyService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class KittyDomainModule {

    @Binds
    abstract fun bindKittyUseCase(kittyUseCaseImpl: KittyService): KittyGetter
}
