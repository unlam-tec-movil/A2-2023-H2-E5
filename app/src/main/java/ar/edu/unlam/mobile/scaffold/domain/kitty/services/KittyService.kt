package ar.edu.unlam.mobile.scaffold.domain.kitty.services

import ar.edu.unlam.mobile.scaffold.data.kitty.repository.KittyRepository
import ar.edu.unlam.mobile.scaffold.domain.kitty.models.Kitty
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KittyService @Inject constructor(val repository: KittyRepository) : KittyGetter {
    override suspend fun getKitty(): Flow<Kitty> {
        return this.repository.getKitty()
    }

    override suspend fun getKittyById(id: Int): Flow<Kitty> {
        TODO("Not yet implemented")
    }
}
