package ar.edu.unlam.mobile.scaffold.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

class AppState(
    val navController: NavHostController,
    private val navigationTabs: List<NavigationScreen>,
) {
    private val bottomBarRoutes = navigationTabs.map { it.route }

    val shouldShowBottomBar: Boolean
        @Composable get() =
            navController
                .currentBackStackEntryAsState()
                .value
                ?.destination
                ?.route in bottomBarRoutes
}
