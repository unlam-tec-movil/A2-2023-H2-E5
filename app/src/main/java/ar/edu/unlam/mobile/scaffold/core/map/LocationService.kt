package ar.edu.unlam.mobile.scaffold.core.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

// Servicio para manejar la ubicación del dispositivo
class LocationService
    @Inject
    constructor(
        private val context: Context,
        private val locationClient: FusedLocationProviderClient,
    ) : ILocationService { // Implementa la interfaz ILocationService para gestionar la ubicación

        // Método para solicitar actualizaciones de ubicación
        @SuppressLint("MissingPermission")
        @RequiresApi(Build.VERSION_CODES.S)
        override fun requestLocationUpdates(): Flow<LatLng?> =
            callbackFlow {
                // Verificar permisos de ubicación
                if (!context.hasLocationPermission()) {
                    trySend(null) // Enviar null si no se tienen permisos
                    return@callbackFlow // Salir del flow
                }

                // Configurar solicitud de ubicación
                val request =
                    LocationRequest
                        .Builder(10000L) // Intervalo de actualización de ubicación en milisegundos
                        .setIntervalMillis(10000L)
                        .setPriority(Priority.PRIORITY_HIGH_ACCURACY) // Prioridad alta de precisión
                        .build()

                // Callback para manejar resultados de ubicación
                val locationCallback =
                    object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            locationResult.locations.lastOrNull()?.let {
                                trySend(LatLng(it.latitude, it.longitude)) // Enviar la ubicación como LatLng
                                Log.i("locationExample", "onLocationResult: $it") // Log para depuración
                            }
                        }
                    }

                // Solicitar actualizaciones de ubicación al cliente de ubicación
                locationClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper(), // Looper para manejar el hilo principal
                )

                // Cerrar el flow cuando no se necesiten más actualizaciones
                awaitClose {
                    locationClient.removeLocationUpdates(locationCallback)
                }
            }

        // Método para solicitar la ubicación actual
        override fun requestCurrentLocation(): Flow<LatLng?> {
            TODO("Not yet implemented")
        }

        // Función de extensión para verificar permisos de ubicación en el contexto dado
        fun Context.hasLocationPermission(): Boolean =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ) == PackageManager.PERMISSION_GRANTED
    }
