package ar.edu.unlam.mobile.scaffold.ui.screens.podometer

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PodometerScreen(viewModel: PodometerViewModel = viewModel()) {
    val stepCounter by viewModel.stepCounter.observeAsState(StepCounter(0, 10000))
    val progress by viewModel.progress.observeAsState(0)
    val activityTime by viewModel.activityTime.observeAsState(0L)
    val activityCalories by viewModel.activityCalories.observeAsState(0)
    val activityDistance by viewModel.activityDistance.observeAsState(0.0)
    val stepHistory by viewModel.stepHistory.observeAsState(emptyList())
    val context = LocalContext.current

    var selectedDate by remember { mutableStateOf(getCurrentDate()) }
    var showDatePicker by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted) {
                viewModel.handlePermissionDenied(context)
            }
        }
    )

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            context = context,
            onDateSelected = { date ->
                selectedDate = date
                viewModel.filterHistoryByDate(date)
                showDatePicker = false
            },
            onDismissRequest = { showDatePicker = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Reemplazar el título por la fecha actual con capacidad de abrir el calendario
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .padding(8.dp)
                .clickable { showDatePicker = true }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = selectedDate,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(8.dp)
                )
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Calendar",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Barra de progreso circular
        CircularProgressBar(
            progress = progress.toFloat(),
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Pasos: ${stepCounter.steps} / ${stepCounter.goal}",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Tiempo activo: ${activityTime}s",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Distancia: ${String.format("%.2f", activityDistance)} km",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Calorías: ${activityCalories} kcal",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Lista del historial de pasos
        Text(
            text = "Historial de pasos",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxHeight()
        ) {
            items(stepHistory.size) { index ->
                val historyItem = stepHistory[index]
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(MaterialTheme.colors.surface)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Fecha: ${historyItem.date}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Pasos: ${historyItem.steps}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Tiempo activo: ${historyItem.activeTime}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Distancia: ${String.format("%.2f", historyItem.distance)} km",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Calorías: ${historyItem.calories} kcal",
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}

@Composable
fun CircularProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 8.dp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        CircularProgressIndicator(
            progress = progress / 100,
            modifier = Modifier.fillMaxSize(),
            strokeWidth = strokeWidth
        )
        Text(
            text = "${progress.toInt()}%",
            style = MaterialTheme.typography.h6
        )
    }
}

@Composable
fun DatePickerDialog(
    context: Context,
    onDateSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val calendar = Calendar.getInstance()
    val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            onDateSelected(dateFormatter.format(calendar.time))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    DisposableEffect(Unit) {
        datePickerDialog.setOnDismissListener { onDismissRequest() }
        datePickerDialog.show()

        onDispose { datePickerDialog.dismiss() }
    }
}

fun getCurrentDate(): String {
    val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return dateFormatter.format(Date())
}
