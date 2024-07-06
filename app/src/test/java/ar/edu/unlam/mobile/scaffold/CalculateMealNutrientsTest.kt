package ar.edu.unlam.mobile.scaffold

import ar.edu.unlam.mobile.scaffold.domain.model.ActivityLevel
import ar.edu.unlam.mobile.scaffold.domain.model.Gender
import ar.edu.unlam.mobile.scaffold.domain.model.GoalType
import ar.edu.unlam.mobile.scaffold.domain.model.MealType
import ar.edu.unlam.mobile.scaffold.domain.model.TrackedFood
import ar.edu.unlam.mobile.scaffold.domain.model.UserInfo
import ar.edu.unlam.mobile.scaffold.domain.preferences.Preferences
import ar.edu.unlam.mobile.scaffold.domain.usecase.CalculateMealNutrients
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.math.roundToInt

class CalculateMealNutrientsTest {
    private lateinit var preferences: Preferences
    private lateinit var calculateMealNutrients: CalculateMealNutrients

    @Before
    fun setUp() {
        preferences = mockk()
        calculateMealNutrients = CalculateMealNutrients(preferences)
    }

    @Test
    fun calculateCorrectMealNutrientsAndGoals() {
        // Given
        val userInfo =
            UserInfo(
                gender = Gender.Male,
                age = 25,
                weight = 70f,
                height = 175,
                activityLevel = ActivityLevel.Medium,
                goalType = GoalType.KeepWeight,
                carbRatio = 0.5f,
                proteinRatio = 0.3f,
                fatRatio = 0.2f,
                steps = 10000,
            )

        val trackedFoods =
            listOf(
                TrackedFood(
                    name = "Food1",
                    carbs = 50,
                    protein = 20,
                    fat = 10,
                    calories = 400,
                    mealType = MealType.Desayuno,
                    date = LocalDate.now(),
                    imageUrl = "https://example.com/food1.jpg",
                    amount = 2,
                ),
                TrackedFood(
                    name = "Food2",
                    carbs = 30,
                    protein = 15,
                    fat = 5,
                    calories = 300,
                    mealType = MealType.Almuerzo,
                    date = LocalDate.now(),
                    imageUrl = "https://example.com/food2.jpg",
                    amount = 2,
                ),
            )

        every { preferences.loadUserInfo() } returns userInfo

        // When
        val result = calculateMealNutrients(trackedFoods)

        // Then
        val expectedCalorieGoal = calculateMealNutrients.dailyCalorieRequirement(userInfo)
        val expectedCarbsGoal = (expectedCalorieGoal * userInfo.carbRatio / 4f).roundToInt()
        val expectedProteinGoal = (expectedCalorieGoal * userInfo.proteinRatio / 4f).roundToInt()
        val expectedFatGoal = (expectedCalorieGoal * userInfo.fatRatio / 9f).roundToInt()

        assertEquals(expectedCalorieGoal, result.caloriesGoal)
        assertEquals(expectedCarbsGoal, result.carbsGoal)
        assertEquals(expectedProteinGoal, result.proteinGoal)
        assertEquals(expectedFatGoal, result.fatGoal)

        assertEquals(80, result.totalCarbs)
        assertEquals(35, result.totalProtein)
        assertEquals(15, result.totalFat)
        assertEquals(700, result.totalCalories)

        assertEquals(2, result.mealNutrients.size)
        assertEquals(
            CalculateMealNutrients.MealNutrients(50, 20, 10, 400, MealType.Desayuno),
            result.mealNutrients[MealType.Desayuno],
        )
        assertEquals(
            CalculateMealNutrients.MealNutrients(30, 15, 5, 300, MealType.Almuerzo),
            result.mealNutrients[MealType.Almuerzo],
        )
    }
}
