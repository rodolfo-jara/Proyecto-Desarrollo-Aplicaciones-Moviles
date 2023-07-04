package pe.edu.cibertec.restaurantcompose.data.remote


import pe.edu.cibertec.restaurantcompose.data.remote.service.CriolloService
import pe.edu.cibertec.restaurantcompose.data.remote.service.MarinoService
import pe.edu.cibertec.restaurantcompose.data.remote.service.PolleriasService
import pe.edu.cibertec.restaurantcompose.data.remote.service.RestaurantService
import pe.edu.cibertec.restaurantcompose.data.remote.service.UserService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val API_BASE_URL = "https://violet-gigantic-jay.glitch.me/"

    private fun getRetrofit() = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getRestaurantService(): RestaurantService {
        return getRetrofit().create(RestaurantService::class.java)
    }
    fun getRePolleriaService(): PolleriasService {
        return getRetrofit().create(PolleriasService::class.java)
    }
    fun getReMarinoService(): MarinoService {
        return getRetrofit().create(MarinoService::class.java)
    }
    fun getReCriolloService(): CriolloService {
        return getRetrofit().create(CriolloService::class.java)
    }


    fun getUserService(): UserService {
        return getRetrofit().create(UserService::class.java)
    }
}