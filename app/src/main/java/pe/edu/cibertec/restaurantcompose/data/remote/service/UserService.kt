package pe.edu.cibertec.restaurantcompose.data.remote.service

import pe.edu.cibertec.restaurantcompose.data.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {

    @POST("Usuarios")
    fun createUser(@Body user: User): Call<User>

    @GET("Usuarios")
    fun validateUser(@Query("username") username: String): Call<List<User>>

    @GET("Usuarios")
    fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): Call<List<User>>
}