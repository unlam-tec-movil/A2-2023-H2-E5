package ar.edu.unlam.mobile.scaffold.data.kitty.network

import ar.edu.unlam.mobile.scaffold.domain.kitty.models.Kitty

data class KittyAPIModel(
    val id: String,
    val url: String,
    val width: Int,
    val height: Int,
) {
    fun toKitty(): Kitty {
        return Kitty(
            id = id,
            url = url,
            width = width,
            height = height,
        )
    }
}
