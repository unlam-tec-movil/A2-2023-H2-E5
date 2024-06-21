package ar.edu.unlam.mobile.scaffold.data.mapper

import ar.edu.unlam.mobile.scaffold.data.local.entity.LocationEntity
import com.google.android.gms.maps.model.LatLng

fun LocationEntity.toLatLng(): LatLng{
    return LatLng(this.latitud,this.longitud)
}

fun LatLng.toLocationEntity(): LocationEntity{
    return LocationEntity(
        latitud = this.latitude,
        longitud = this.longitude
    )
}