package ar.edu.unlam.mobile.scaffold.data.mapper

import ar.edu.unlam.mobile.scaffold.data.remote.dto.Product
import ar.edu.unlam.mobile.scaffold.domain.model.TrackableFood
import kotlin.math.roundToInt

fun Product.toTrackableFood(): TrackableFood? {
    val carbsPer100g = nutriments.carbohydrates100g.roundToInt()
    val proteinsPer100g = nutriments.proteins100g.roundToInt()
    val fatPer100g = nutriments.fat100g.roundToInt()
    val caloriesPer100g = nutriments.energyKcal100g.roundToInt()
    return TrackableFood(
        name = productName ?: return null,
        carbsPer100g = carbsPer100g,
        proteinPer100g = proteinsPer100g,
        fatPer100g = fatPer100g,
        caloriesPer100g = caloriesPer100g,
        imageUrl = imageFrontThumbUrl
    )
}