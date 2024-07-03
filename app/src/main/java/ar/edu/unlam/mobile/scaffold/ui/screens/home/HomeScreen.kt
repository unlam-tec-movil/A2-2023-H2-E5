package ar.edu.unlam.mobile.scaffold.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import ar.edu.unlam.mobile.scaffold.ui.screens.podometer.PodometerScreen

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Surface(modifier = modifier) {
        PodometerScreen(viewModel = viewModel())
    }
}
