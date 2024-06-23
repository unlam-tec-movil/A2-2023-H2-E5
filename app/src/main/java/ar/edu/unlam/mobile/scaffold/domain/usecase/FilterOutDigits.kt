package ar.edu.unlam.mobile.scaffold.domain.usecase

class FilterOutDigits {
    operator fun invoke(text: String): String = text.filter { it.isDigit() }
}
