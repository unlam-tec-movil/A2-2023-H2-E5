package ar.edu.unlam.mobile.scaffold.domain.kitty.services

import ar.edu.unlam.mobile.scaffold.domain.kitty.models.Kitty
import kotlinx.coroutines.flow.Flow

interface KittyGetter {
    suspend fun getKitty(): Flow<Kitty>
    suspend fun getKittyById(id: Int): Flow<Kitty>
}
