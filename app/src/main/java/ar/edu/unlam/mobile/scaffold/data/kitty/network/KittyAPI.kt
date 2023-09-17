package ar.edu.unlam.mobile.scaffold.data.kitty.network

import retrofit2.http.GET

interface KittyAPI {
    @GET("/v1/images/search?limit=3")
    suspend fun getKitties(): List<KittyAPIModel>
}
