package ar.edu.unlam.mobile.scaffold.data.kitty.repository

import ar.edu.unlam.mobile.scaffold.domain.kitty.models.Kitty
import kotlinx.coroutines.flow.Flow

interface KittyRepository {
    suspend fun getKitty(): Flow<Kitty>
}
