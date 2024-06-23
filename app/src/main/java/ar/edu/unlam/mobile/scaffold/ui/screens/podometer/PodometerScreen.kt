@file:Suppress("ktlint:standard:no-wildcard-imports")

package ar.edu.unlam.mobile.scaffold.ui.screens.podometer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import ar.edu.unlam.mobile.scaffold.ui.screens.pedometer.PodometerViewModel
import ar.edu.unlam.mobile.scaffold.ui.screens.pedometer.StepCounter
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun PodometerScreen(viewModel: PodometerViewModel = viewModel()) {
    val context = LocalContext.current
    var permissionRequested by remember { mutableStateOf(false) }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
        ) { isGranted ->
            viewModel.isPermissionGranted = isGranted
        }

    LaunchedEffect(Unit) {
        val isPermissionGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACTIVITY_RECOGNITION,
            ) == PackageManager.PERMISSION_GRANTED

        if (!isPermissionGranted && !permissionRequested) {
            permissionRequested = true
            permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        } else {
            viewModel.isPermissionGranted = true
        }
    }

    val stepCounter by viewModel.stepCounter.observeAsState(StepCounter(0, 10000))
    val progress by viewModel.progress.observeAsState(0)
    val activeTime by viewModel.activityTime.observeAsState(0L)
    val distance by viewModel.activityDistance.observeAsState(0.0)
    val caloriesBurnt by viewModel.activityCalories.observeAsState(0)

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("$progress%", style = MaterialTheme.typography.h6)
                Text("Meta", style = MaterialTheme.typography.body2)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(formatActiveTime(activeTime), style = MaterialTheme.typography.h6)
                Text("Tiempo activo", style = MaterialTheme.typography.body2)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(String.format("%.2f km", distance), style = MaterialTheme.typography.h6)
                Text("Distancia", style = MaterialTheme.typography.body2)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(200.dp),
        ) {
            CircularProgressIndicator(
                progress = progress / 100f,
                strokeWidth = 8.dp,
                modifier = Modifier.fillMaxSize(),
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Pasos de hoy",
                    style = MaterialTheme.typography.body1,
                )
                Text(
                    text = "${stepCounter.steps}",
                    style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
                )
                Text(
                    text = "Meta: ${stepCounter.goal}",
                    style = MaterialTheme.typography.body2,
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Calorias quemadas",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(vertical = 8.dp),
        )
        Text(
            text = "$caloriesBurnt",
            style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
        )
    }
}

fun formatActiveTime(seconds: Long): String {
    val hours = TimeUnit.SECONDS.toHours(seconds)
    val minutes = TimeUnit.SECONDS.toMinutes(seconds) % 60
    val secs = seconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, secs)
}
