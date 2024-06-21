package ar.edu.unlam.mobile.scaffold.ui.screens.podometer

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun PodometerScreen(viewModel: PodometerViewModel = viewModel()) {
    val context = LocalContext.current
    var permissionRequested by remember { mutableStateOf(false) }
    var permissionGranted by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
    }

    LaunchedEffect(Unit) {
        val isPermissionGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACTIVITY_RECOGNITION
        ) == PackageManager.PERMISSION_GRANTED

        if (!isPermissionGranted && !permissionRequested) {
            permissionRequested = true
            permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        } else {
            permissionGranted = true
        }
    }

    if (permissionGranted) {
        val stepCounter by viewModel.stepCounter.observeAsState(StepCounter(0, 10000))
        val progress by viewModel.progress.observeAsState(0)
        val activityTime by viewModel.activityTime.observeAsState(0)
        val activityCalories by viewModel.activityCalories.observeAsState(0)

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(progress = progress / 100f)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Pasos: ${stepCounter.steps} / Objetivo: ${stepCounter.goal}")
            Spacer(modifier = Modifier.height(16.dp))
            Text("Tiempo de actividad: $activityTime / 60 min")
            Spacer(modifier = Modifier.height(16.dp))
            Text("Calorías actividad: $activityCalories / 500 kcal")
            Spacer(modifier = Modifier.height(16.dp))
            Text("Total de calorías quemadas: ${viewModel.totalCaloriesBurned}")
            Text("Distancia durante la actividad: ${viewModel.activityDistance} km")
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Se necesita permiso para acceder a los datos de actividad.")
        }
    }
}

