package ar.edu.unlam.mobile.scaffold.data.kitty.di

import ar.edu.unlam.mobile.scaffold.data.kitty.network.KittyHTTPRepository
import ar.edu.unlam.mobile.scaffold.data.kitty.network.KittyNetworkRepository
import ar.edu.unlam.mobile.scaffold.data.kitty.repository.KittyDefaultRepository
import ar.edu.unlam.mobile.scaffold.data.kitty.repository.KittyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class KittyDataModule {
    @Binds
    abstract fun bindKittyRepository(kittyRepositoryImpl: KittyDefaultRepository): KittyRepository

    @Binds
    abstract fun bindKittyNetworkRepo(kittyHTTPClient: KittyHTTPRepository): KittyNetworkRepository
}
