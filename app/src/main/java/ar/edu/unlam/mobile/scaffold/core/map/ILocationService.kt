package ar.edu.unlam.mobile.scaffold.core.map

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

// Interfaz para un servicio de ubicación
interface ILocationService {
    // Método para solicitar actualizaciones de ubicación
    fun requestLocationUpdates(): Flow<LatLng?>

    // Método para solicitar la ubicación actual
    fun requestCurrentLocation(): Flow<LatLng?>
}
