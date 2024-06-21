package ar.edu.unlam.mobile.scaffold.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ar.edu.unlam.mobile.scaffold.data.local.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query(
        """
            SELECT *
            FROM location
        """,
    )
    fun getLocationPoints(): Flow<List<LocationEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocationPoint(location: LocationEntity)

}