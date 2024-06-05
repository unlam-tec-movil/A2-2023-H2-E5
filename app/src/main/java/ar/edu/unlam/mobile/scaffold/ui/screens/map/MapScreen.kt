package ar.edu.unlam.mobile.scaffold.ui.screens.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.maps.android.compose.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ar.edu.unlam.mobile.scaffold.R
import ar.edu.unlam.mobile.scaffold.domain.model.Point
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng


@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MapScreen(
    modifier: androidx.compose.ui.Modifier,
    viewModel: MapViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    when (val currentState = viewState) {
        is ViewState.Loading -> {
            if (!isGpsEnabled(context)) {
                Box(
                    modifier = modifier,
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Por Favor activa el Gps para usar el mapa",
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Box(
                    modifier = modifier,
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        ViewState.RevokedPermissions -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Necesitas los permisos de localizacion y Gps activado para usar el mapa",
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        modifier = Modifier.padding(16.dp),
                        onClick = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.data = Uri.parse("package:${context.packageName}")
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(Color.Yellow)
                    ) {
                        Text(
                            "Settings",
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }

        is ViewState.Success -> {
            val currentLoc = currentState.location ?: LatLng(0.0, 0.0)
            val cameraState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(currentLoc, 18f)
            }

            val puntosDeEncuentroState by viewModel.point.collectAsStateWithLifecycle()

            Map(
                modifier = modifier.fillMaxSize(),
                currentPosition = currentLoc,
                cameraState = cameraState,
                puntosDeEncuentroState = puntosDeEncuentroState
            )
        }
    }
}


@Composable
fun isGpsEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}
@Composable
fun Map(
    modifier: Modifier = Modifier,
    currentPosition: LatLng,
    cameraState: CameraPositionState,
    puntosDeEncuentroState: List<Point>
) {
    val punto1 = LatLng(puntosDeEncuentroState[0].coordinates1, puntosDeEncuentroState[0].coordinates2)
    val punto2 = LatLng(puntosDeEncuentroState[1].coordinates1, puntosDeEncuentroState[1].coordinates2)
    val marker = LatLng(currentPosition.latitude, currentPosition.longitude)

    var selectedDestination by remember { mutableStateOf<LatLng?>(null) }

    Box(modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Yellow),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Puntos de Encuentro",
                modifier = Modifier
                    .padding(10.dp)
                    .testTag("MapScreen title"),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                fontSize = 40.sp,
                color = Color.Black
            )

            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("MapScreen googleMap"),
                cameraPositionState = cameraState,
                properties = MapProperties(
                    isMyLocationEnabled = true,
                    mapType = MapType.NORMAL,
                )
            ) {
                Marker(
                    state = MarkerState(position = marker),
                    title = "Mi Posición Actual",
                )
                MarkerInfoWindowContent(
                    state = MarkerState(position = punto1),
                    snippet = "Punto de encuentro 1",
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_map),
                ) {
                    selectedDestination = punto1
                    Box(
                        modifier = Modifier
                            .height(290.dp)
                            .width(300.dp)
                            .background(Color.Green)
                    ) {
                        // Aquí iría el contenido que deseamos mostrar para el punto de encuentro 1
                    }
                }

                MarkerInfoWindowContent(
                    state = MarkerState(position = punto2),
                    snippet = "Punto de encuentro 2",
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_map)
                ) {
                    selectedDestination = punto2

                    Box(
                        modifier = Modifier
                            .height(270.dp)
                            .width(300.dp)
                            .background(Color.Green)
                    ) {
                        // Aquí iría el contenido que deseamos mostrar para el punto de encuentro 2
                    }
                }
            }
        }
    }
}

