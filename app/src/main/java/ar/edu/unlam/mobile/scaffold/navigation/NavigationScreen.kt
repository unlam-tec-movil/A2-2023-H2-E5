package ar.edu.unlam.mobile.scaffold.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import ar.edu.unlam.mobile.scaffold.R

sealed class NavigationScreen(
    val icon: ImageVector,
    val title: Int,
    val route: String,
) {
    object Home : NavigationScreen(Icons.Default.Home, R.string.home, Route.HOME)
    object Search : NavigationScreen(Icons.Default.Search, R.string.search, Route.TRACKER_OVERVIEW) //Cambiar ruta
    object Profile : NavigationScreen(Icons.Default.Search, R.string.profile, Route.PROFILE)
}
