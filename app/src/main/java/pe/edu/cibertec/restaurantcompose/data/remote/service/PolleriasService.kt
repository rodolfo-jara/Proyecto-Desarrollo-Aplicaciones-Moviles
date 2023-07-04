package pe.edu.cibertec.restaurantcompose.data.remote.service

import pe.edu.cibertec.restaurantcompose.data.model.Pollerias
import retrofit2.Call
import retrofit2.http.GET

interface PolleriasService {
    @GET("pollerias")
    fun getPollerias(): Call<List<Pollerias>>
}