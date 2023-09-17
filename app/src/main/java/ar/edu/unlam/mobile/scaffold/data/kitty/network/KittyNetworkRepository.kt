package ar.edu.unlam.mobile.scaffold.data.kitty.network

import kotlinx.coroutines.flow.Flow

interface KittyNetworkRepository {
    suspend fun getRandomKitty(): Flow<KittyAPIModel>
}
