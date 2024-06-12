package ar.edu.unlam.mobile.scaffold.domain.model

sealed class GoalType(val name: String) {
    object LoseWeight : GoalType("Perder peso")
    object KeepWeight : GoalType("Mantener peso")
    object GainWeight : GoalType("Ganar peso")

    companion object {
        fun fromString(name: String): GoalType {
            return when (name) {
                "Perder peso" -> LoseWeight
                "Mantener peso" -> KeepWeight
                "Ganar peso" -> GainWeight
                else -> KeepWeight
            }
        }
    }
}
