package ar.edu.unlam.mobile.scaffold

import CustomBottomNavigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ar.edu.unlam.mobile.scaffold.domain.preferences.Preferences
import ar.edu.unlam.mobile.scaffold.navigation.AppState
import ar.edu.unlam.mobile.scaffold.navigation.NavigationScreen
import ar.edu.unlam.mobile.scaffold.navigation.Route
import ar.edu.unlam.mobile.scaffold.ui.screens.SearchScreen
import ar.edu.unlam.mobile.scaffold.ui.screens.TrackerOverviewScreen
import ar.edu.unlam.mobile.scaffold.ui.screens.activity.ActivityScreen
import ar.edu.unlam.mobile.scaffold.ui.screens.age.AgeScreen
import ar.edu.unlam.mobile.scaffold.ui.screens.gender.GenderScreen
import ar.edu.unlam.mobile.scaffold.ui.screens.goal.GoalScreen
import ar.edu.unlam.mobile.scaffold.ui.screens.height.HeightScreen
import ar.edu.unlam.mobile.scaffold.ui.screens.home.HomeScreen
import ar.edu.unlam.mobile.scaffold.ui.screens.map.MapScreen
import ar.edu.unlam.mobile.scaffold.ui.screens.nutrientgoal.NutrientGoalScreen
import ar.edu.unlam.mobile.scaffold.ui.screens.profile.ProfileScreen
import ar.edu.unlam.mobile.scaffold.ui.screens.steps.StepScreen
import ar.edu.unlam.mobile.scaffold.ui.screens.weight.WeightScreen
import ar.edu.unlam.mobile.scaffold.ui.screens.welcome.WelcomeScreen
import ar.edu.unlam.mobile.scaffold.ui.theme.CalorieTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val shouldShowOnboarding = preferences.loadShouldShowOnboarding()
        setContent {
            CalorieTrackerTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                val bottomBarScreens = listOf(NavigationScreen.Home, NavigationScreen.Search, NavigationScreen.Map, NavigationScreen.Profile)
                val appState = AppState(navController, bottomBarScreens)
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState,
                    bottomBar = {
                        if (appState.shouldShowBottomBar) {
                            CustomBottomNavigation(
                                items = bottomBarScreens,
                                navController = navController,
                            )
                        }
                    },
                ) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = if (shouldShowOnboarding) Route.WELCOME else Route.HOME,
                        modifier = Modifier.padding(padding),
                    ) {
                        composable(Route.WELCOME) {
                            WelcomeScreen(onNextClick = {
                                navController.navigate(Route.GENDER)
                            })
                        }
                        composable(Route.GENDER) {
                            GenderScreen(onNextClick = {
                                navController.navigate(Route.AGE)
                            })
                        }
                        composable(Route.AGE) {
                            AgeScreen(
                                onNextClick = {
                                    navController.navigate(Route.HEIGHT)
                                },
                                scaffoldState = scaffoldState,
                            )
                        }
                        composable(Route.HEIGHT) {
                            HeightScreen(
                                scaffoldState = scaffoldState,
                                onNextClick = {
                                    navController.navigate(Route.WEIGHT)
                                },
                            )
                        }
                        composable(Route.WEIGHT) {
                            WeightScreen(
                                scaffoldState = scaffoldState,
                                onNextClick = {
                                    navController.navigate(Route.STEP)
                                },
                            )
                        }
                        composable(Route.STEP) {
                            StepScreen(
                                scaffoldState = scaffoldState,
                                onNextClick = {
                                    navController.navigate(Route.ACTIVITY)
                                },
                            )
                        }
                        composable(Route.ACTIVITY) {
                            ActivityScreen(
                                onNextClick = {
                                    navController.navigate(Route.GOAL)
                                },
                            )
                        }
                        composable(Route.GOAL) {
                            GoalScreen(
                                onNextClick = {
                                    navController.navigate(Route.NUTRIENT_GOAL)
                                },
                            )
                        }
                        composable(Route.NUTRIENT_GOAL) {
                            NutrientGoalScreen(
                                scaffoldState = scaffoldState,
                                onNextClick = {
                                    navController.navigate(Route.HOME)
                                },
                            )
                        }
                        composable(Route.TRACKER_OVERVIEW) {
                            TrackerOverviewScreen(
                                onNavigateToSearch = { mealName, day, month, year ->
                                    navController.navigate(
                                        Route.SEARCH +
                                            "/$mealName" + "/$day" + "/$month" + "/$year",
                                    )
                                },
                            )
                        }
                        composable(
                            route = Route.SEARCH + "/{mealName}/{dayOfMonth}/{month}/{year}",
                            arguments = listOf(
                                navArgument("mealName") {
                                    type = NavType.StringType
                                },
                                navArgument("dayOfMonth") {
                                    type = NavType.IntType
                                },
                                navArgument("month") {
                                    type = NavType.IntType
                                },
                                navArgument("year") {
                                    type = NavType.IntType
                                },
                            ),
                        ) {
                            val mealName = it.arguments?.getString("mealName")!!
                            val dayOfMonth = it.arguments?.getInt("dayOfMonth")!!
                            val month = it.arguments?.getInt("month")!!
                            val year = it.arguments?.getInt("year")!!
                            SearchScreen(
                                scaffoldState = scaffoldState,
                                mealName = mealName,
                                dayOfMonth = dayOfMonth,
                                month = month,
                                year = year,
                                onNavigateUp = {
                                    navController.navigateUp()
                                },
                            )
                        }
                        composable(Route.HOME) {
                            HomeScreen(modifier = Modifier.padding(padding))
                        }
                        composable(Route.PROFILE) {
                            ProfileScreen(modifier = Modifier.padding(padding))
                        }
                        composable(Route.MAP) {
                            MapScreen(modifier = Modifier.padding(padding))
                        }
                    }
                }
            }
        }
    }
}
