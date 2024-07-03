package ar.edu.unlam.mobile.scaffold.domain.map

import ar.edu.unlam.mobile.scaffold.data.mapper.toLocationEntity
import ar.edu.unlam.mobile.scaffold.data.repository.LocationTrackingRepository
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class SaveCurrentActivityUseCase @Inject constructor(
    private val repository: LocationTrackingRepository
) {
    suspend operator fun invoke(location: LatLng): Unit{
        repository.insertLocationPoint(location.toLocationEntity())
    }
}