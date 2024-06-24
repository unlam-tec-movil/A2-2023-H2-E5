package ar.edu.unlam.mobile.scaffold.data.repository

import ar.edu.unlam.mobile.scaffold.data.local.entity.LocationEntity
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface LocationTrackingRepository {

    suspend fun insertLocationPoint(point: LocationEntity)

    suspend fun getLocationPoints(): Flow<List<LocationEntity>>

    suspend fun clearLocationPoints(day: Int)
}