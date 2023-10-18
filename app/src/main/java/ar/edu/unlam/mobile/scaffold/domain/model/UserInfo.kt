package ar.edu.unlam.mobile.scaffold.domain.model

import ar.edu.unlam.mobile.scaffold.domain.model.ActivityLevel
import ar.edu.unlam.mobile.scaffold.domain.model.Gender
import ar.edu.unlam.mobile.scaffold.domain.model.GoalType

data class UserInfo(
    val gender: Gender,
    val age: Int,
    val weight: Float,
    val height: Int,
    val activityLevel: ActivityLevel,
    val goalType: GoalType,
    val carbRatio: Float,
    val proteinRatio: Float,
    val fatRatio: Float
)
