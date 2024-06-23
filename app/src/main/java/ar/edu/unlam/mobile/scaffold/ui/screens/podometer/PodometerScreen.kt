package ar.edu.unlam.mobile.scaffold.ui.screens.podometer

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import ar.edu.unlam.mobile.scaffold.ui.screens.pedometer.PodometerViewModel
import ar.edu.unlam.mobile.scaffold.ui.screens.pedometer.StepCounter
import java.util.concurrent.TimeUnit

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

    val stepCounter by viewModel.stepCounter.observeAsState(StepCounter(0, 10000))
    val progress by viewModel.progress.observeAsState(0)
    val activeTime by viewModel.activityTime.observeAsState(0L)
    val distance by viewModel.activityDistance.observeAsState(0.0)
    val caloriesBurnt by viewModel.activityCalories.observeAsState(0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${progress}%", style = MaterialTheme.typography.h6)
                Text("Goal", style = MaterialTheme.typography.body2)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(formatActiveTime(activeTime), style = MaterialTheme.typography.h6)
                Text("Active Time", style = MaterialTheme.typography.body2)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(String.format("%.2f km", distance), style = MaterialTheme.typography.h6)
                Text("Distance", style = MaterialTheme.typography.body2)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(200.dp)
        ) {
            CircularProgressIndicator(
                progress = progress / 100f,
                strokeWidth = 8.dp,
                modifier = Modifier.fillMaxSize()
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Today's Step",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "${stepCounter.steps}",
                    style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Goal: ${stepCounter.goal}",
                    style = MaterialTheme.typography.body2
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Calories Burnt",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = "$caloriesBurnt Kcal",
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun formatActiveTime(seconds: Long): String {
    val hours = TimeUnit.SECONDS.toHours(seconds)
    val minutes = TimeUnit.SECONDS.toMinutes(seconds) % 60
    val secs = seconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, secs)
}

