package ar.edu.unlam.mobile.scaffold.data.mapper

import android.annotation.SuppressLint
import ar.edu.unlam.mobile.scaffold.data.local.entity.LocationEntity
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime

fun LocationEntity.toLatLng(): LatLng = LatLng(this.latitud, this.longitud)

@SuppressLint("NewApi")
fun LatLng.toLocationEntity(): LocationEntity =
    LocationEntity(
        latitud = this.latitude,
        longitud = this.longitude,
        day = LocalDateTime.now().dayOfMonth,
    )
