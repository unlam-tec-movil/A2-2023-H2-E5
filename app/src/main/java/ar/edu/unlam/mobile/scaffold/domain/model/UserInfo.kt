package ar.edu.unlam.mobile.scaffold.domain.model

/**
 * Informacion sobre el usuario y lo relacionado a sus objetivos alimenticios.
 */
data class UserInfo(
    val gender: Gender,
    val age: Int,
    val weight: Float,
    val height: Int,
    val activityLevel: ActivityLevel,
    val goalType: GoalType,
    val carbRatio: Float,
    val proteinRatio: Float,
    val fatRatio: Float,
)
