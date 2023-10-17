package ar.edu.unlam.mobile.scaffold.ui.screens

import androidx.annotation.DrawableRes
import ar.edu.unlam.mobile.scaffold.core.util.UiText
import ar.edu.unlam.mobile.scaffold.domain.model.MealType
import ar.edu.unlam.mobile.scaffold.R

data class Meal(
    val name: UiText,
    @DrawableRes val drawableRes: Int,
    val mealType: MealType,
    val carbs: Int = 0,
    val protein: Int = 0,
    val fat: Int = 0,
    val calories: Int = 0,
    val isExpanded: Boolean = false
)

val defaultMeals = listOf(
    Meal(
        name = UiText.StringResource(R.string.desayuno),
        drawableRes = R.drawable.ic_breakfast,
        mealType = MealType.Desayuno
    ),
    Meal(
        name = UiText.StringResource(R.string.almuerzo),
        drawableRes = R.drawable.ic_lunch,
        mealType = MealType.Almuerzo
    ),
    Meal(
        name = UiText.StringResource(R.string.cena),
        drawableRes = R.drawable.ic_dinner,
        mealType = MealType.Cena
    ),
    Meal(
        name = UiText.StringResource(R.string.snacks),
        drawableRes = R.drawable.ic_snack,
        mealType = MealType.Snack
    )
)
