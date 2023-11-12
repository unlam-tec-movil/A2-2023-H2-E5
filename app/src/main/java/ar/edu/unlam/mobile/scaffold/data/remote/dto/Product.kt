package ar.edu.unlam.mobile.scaffold.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Comida con sus valores nutricionales, nombre, e imagen.
 */
data class Product(

    @SerializedName("image_front_thumb_url")
    val imageFrontThumbUrl: String?,
    val nutriments: Nutriments,
    @SerializedName("product_name")
    val productName: String?,
)
