package ar.edu.unlam.mobile.scaffold.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "location", primaryKeys = ["latitud", "longitud"])
data class LocationEntity(
    @ColumnInfo("latitud")
    val latitud: Double,
    @ColumnInfo("longitud")
    val longitud: Double,
    @ColumnInfo("day")
    val day: Int,
)
