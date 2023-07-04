package pe.edu.cibertec.restaurantcompose.data.remote.service

import pe.edu.cibertec.restaurantcompose.data.model.Pollerias
import pe.edu.cibertec.restaurantcompose.data.model.Restaurant
import retrofit2.Call
import retrofit2.http.GET

interface RestaurantService {

    @GET("Restaurantes")
    fun getRestaurants(): Call<List<Restaurant>>


}