package ar.edu.unlam.mobile.scaffold.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import ar.edu.unlam.mobile.scaffold.R

sealed class NavigationScreen(
    val icon: ImageVector,
    val title: Int,
    val route: String,
) {
    object Home : NavigationScreen(Icons.Default.Home, R.string.home, Route.HOME)
    object Search : NavigationScreen(Icons.Default.Search, R.string.search, Route.TRACKER_OVERVIEW) //Cambiar ruta por la pantalla de Busqueda
    object Profile : NavigationScreen(Icons.Default.Person, R.string.profile, Route.PROFILE)
    object Map : NavigationScreen(Icons.Default.Place, R.string.map, Route.MAP)

    //Agregar pantalla de comidas y tracker
}
