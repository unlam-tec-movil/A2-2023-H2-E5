package ar.edu.unlam.mobile.scaffold.domain.map

import ar.edu.unlam.mobile.scaffold.data.local.LocationDao
import ar.edu.unlam.mobile.scaffold.data.map.PuntoDeEncuentro
import ar.edu.unlam.mobile.scaffold.data.mapper.toLatLng
import ar.edu.unlam.mobile.scaffold.data.repository.LocationTrackingRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


/**
 * Devuelve datos de la actividad actual
 */
class GetCurrentActivityStateUseCase @Inject constructor(
    private val repository: LocationTrackingRepository
) {
    suspend operator fun invoke(): Flow<List<LatLng>> {
       return repository.getLocationPoints().map {
           it.map {
               it.toLatLng()
           }
       }

    }
}