package ar.edu.unlam.mobile.scaffold.domain.model

sealed class MealType(val name: String) {
    object Desayuno : MealType("desayuno")
    object Almuerzo : MealType("almuerzo")
    object Cena : MealType("cena")
    object Snack : MealType("snacks")

    companion object {
        fun fromString(name: String): MealType {
            return when (name.lowercase()) {
                "desayuno" -> Desayuno
                "almuerzo" -> Almuerzo
                "cena" -> Cena
                "snacks" -> Snack
                else -> Snack
            }
        }
    }
}
