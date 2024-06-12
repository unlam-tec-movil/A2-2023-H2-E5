package ar.edu.unlam.mobile.scaffold.domain.model

/**
 * Nivel de actividad
 */
sealed class ActivityLevel(val name: String) {
    object Low : ActivityLevel("Baja")
    object Medium : ActivityLevel("Media")
    object High : ActivityLevel("Alta")

    companion object {
        fun fromString(name: String): ActivityLevel {
            return when (name) {
                "low" -> Low
                "medium" -> Medium
                "high" -> High
                else -> Low
            }
        }
    }
}
