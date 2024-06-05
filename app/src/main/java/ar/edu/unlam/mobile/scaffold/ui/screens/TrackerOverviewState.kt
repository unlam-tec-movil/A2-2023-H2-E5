package ar.edu.unlam.mobile.scaffold.ui.screens

import ar.edu.unlam.mobile.scaffold.domain.model.TrackedFood
import java.time.LocalDate

data class TrackerOverviewState(
    val totalCarbs: Int = 0,
    val totalProtein: Int = 0,
    val totalFat: Int = 0,
    val totalCalories: Int = 0,
    val totalSteps: Int = 0,
    val totalWater: Int = 0,
    val carbsGoal: Int = 0,
    val proteinGoal: Int = 0,
    val fatGoal: Int = 0,
    val caloriesGoal: Int = 0,
    val stepsGoal: Int = 0,  // Meta para pasos
    val waterGoal: Int =0,    // Meta para agua en mililitros
    val steps: Int = 0,          // Nueva propiedad para pasos
    val water: Int = 0,          // Nueva propiedad para agua en mililitros
    val date: LocalDate = LocalDate.now(),
    val trackedFoods: List<TrackedFood> = emptyList(),
    val meals: List<Meal> = defaultMeals,
)
