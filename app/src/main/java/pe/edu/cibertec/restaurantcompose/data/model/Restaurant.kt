package pe.edu.cibertec.restaurantcompose.data.model

import com.google.gson.annotations.SerializedName

data class Restaurant(
    val id: Int,
    val title: String,
    @SerializedName("poster")
    val posterUrl: String,
    val latitude: Double,
    val longitude: Double,
    val category: String,

    var isFavorite: Boolean = false
)


