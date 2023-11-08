package ar.edu.unlam.mobile.scaffold.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import ar.edu.unlam.mobile.scaffold.data.local.entity.TrackedFoodEntity
import ar.edu.unlam.mobile.scaffold.domain.model.MealType
import ar.edu.unlam.mobile.scaffold.domain.model.TrackedFood
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun TrackedFoodEntity.toTrackedFood(): TrackedFood {
    return TrackedFood(
        name = name,
        carbs = carbs,
        protein = protein,
        fat = fat,
        imageUrl = imageUrl,
        mealType = MealType.fromString(type),
        amount = amount,
        date = LocalDate.of(year, month, dayOfMonth),
        calories = calories,
        id = id,
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun TrackedFood.toTrackedFoodEntity(): TrackedFoodEntity {
    return TrackedFoodEntity(
        name = name,
        carbs = carbs,
        protein = protein,
        fat = fat,
        calories = calories,
        imageUrl = imageUrl,
        type = mealType.name,
        amount = amount,
        dayOfMonth = date.dayOfMonth,
        month = date.monthValue,
        year = date.year,
        id = id,
    )
}
