package ar.edu.unlam.mobile.scaffold.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ar.edu.unlam.mobile.scaffold.data.local.entity.LocationEntity
import ar.edu.unlam.mobile.scaffold.data.local.entity.TrackedFoodEntity

@Database(
    entities = [TrackedFoodEntity::class,LocationEntity::class],
    version = 1,
)
abstract class TrackerDatabase : RoomDatabase() {

    abstract val dao: TrackerDao
    abstract val locationDao: LocationDao
}
