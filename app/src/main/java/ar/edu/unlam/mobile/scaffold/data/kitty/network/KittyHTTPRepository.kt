package ar.edu.unlam.mobile.scaffold.data.kitty.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class KittyHTTPRepository @Inject constructor(private val api: KittyAPI) : KittyNetworkRepository {
    override suspend fun getRandomKitty(): Flow<KittyAPIModel> {
        return flow {
            emit(api.getKitties()[0])
        }
    }
}
