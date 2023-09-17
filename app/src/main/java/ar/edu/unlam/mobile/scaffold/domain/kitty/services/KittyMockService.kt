package ar.edu.unlam.mobile.scaffold.domain.kitty.services

import ar.edu.unlam.mobile.scaffold.domain.kitty.models.Kitty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KittyMockService @Inject constructor() : KittyGetter {
    override suspend fun getKitty(): Flow<Kitty> {
        return flow {
            emit(
                Kitty(
                    id = "VEINTE MILLONES 四百",
                    url = "https://img.freepik.com/foto-gratis/cerrar-lindo-gato-interior_23-2148882585.jpg",
                    width = 100,
                    height = 100,
                ),
            )
        }
    }

    override suspend fun getKittyById(id: Int): Flow<Kitty> {
        return flow {
            emit(
                Kitty(
                    id = "$id",
                    url = "https://img.freepik.com/foto-gratis/cerrar-lindo-gato-interior_23-2148882585.jpg",
                    width = 100,
                    height = 100,
                ),
            )
        }
    }
}
