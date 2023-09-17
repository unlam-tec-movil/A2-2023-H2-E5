package ar.edu.unlam.mobile.scaffold.data.kitty.di

import ar.edu.unlam.mobile.scaffold.data.kitty.network.KittyAPI
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KittyDataProvider {
    @Provides
    @Singleton
    fun provideKittyAPI(gson: Gson): KittyAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://api.thecatapi.com")
            .build()
            .create(KittyAPI::class.java)
    }
}
