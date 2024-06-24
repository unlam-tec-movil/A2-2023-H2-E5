package ar.edu.unlam.mobile.scaffold.data.repository

import ar.edu.unlam.mobile.scaffold.data.local.LocationDao
import ar.edu.unlam.mobile.scaffold.data.local.entity.LocationEntity
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

class LocationTrackingRepositoryImpl(private val locationDao: LocationDao): LocationTrackingRepository {
    override suspend fun getLocationPoints(): Flow<List<LocationEntity>> {
        return locationDao.getLocationPoints()
    }

    override suspend fun insertLocationPoint(point: LocationEntity) {
        locationDao.insertLocationPoint(point)
    }

    override suspend fun clearLocationPoints(day: Int) {
        locationDao.deleteLocationsByDay(day)
    }
}