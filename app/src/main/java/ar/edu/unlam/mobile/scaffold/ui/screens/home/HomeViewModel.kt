// HomeViewModel.kt
package ar.edu.unlam.mobile.scaffold.ui.screens.home

import androidx.lifecycle.ViewModel
import ar.edu.unlam.mobile.scaffold.ui.screens.TrackerOverviewState

class HomeViewModel : ViewModel() {
    // Aquí defines el estado inicial de los datos, puedes inicializarlo como desees.
    var trackerOverviewState: TrackerOverviewState = TrackerOverviewState(
        totalCalories = 0,
        caloriesGoal = 2000,
        totalCarbs = 0,
        carbsGoal = 150,
        totalProtein = 0,
        proteinGoal = 100,
        totalFat = 0,
        fatGoal = 70,
        steps = 0,
        stepsGoal = 10000,
        water = 0,
        waterGoal = 2000
    )

    // Aquí podrías tener métodos para actualizar los datos de acuerdo a la lógica de tu aplicación.
    // Por ejemplo:
    fun updateCalories(newCalories: Int) {
        trackerOverviewState = trackerOverviewState.copy(totalCalories = newCalories)
    }

    // Agrega otros métodos según sea necesario para tu lógica de negocio.
}
