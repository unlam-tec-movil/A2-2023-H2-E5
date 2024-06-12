package ar.edu.unlam.mobile.scaffold.domain.model

sealed class Gender(val name: String) {
    object Male : Gender("Masculino")
    object Female : Gender("Femenino")

    companion object {
        fun fromString(name: String): Gender {
            return when (name) {
                "Masculino" -> Male
                "Femenino" -> Female
                else -> Male
            }
        }
    }
}
