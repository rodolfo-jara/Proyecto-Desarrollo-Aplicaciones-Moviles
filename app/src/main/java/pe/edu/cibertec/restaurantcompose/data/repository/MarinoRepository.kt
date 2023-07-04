package pe.edu.cibertec.restaurantcompose.data.repository

import pe.edu.cibertec.restaurantcompose.data.model.Pollerias
import pe.edu.cibertec.restaurantcompose.data.remote.ApiClient
import pe.edu.cibertec.restaurantcompose.data.remote.service.MarinoService
import pe.edu.cibertec.restaurantcompose.util.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class MarinoRepository(
    private val marinoService: MarinoService = ApiClient.getReMarinoService()
) {

    fun getMarino(callback: (Result<List<Pollerias>>) -> Unit) {

        marinoService.getMarino().enqueue(object : Callback<List<Pollerias>> {
            override fun onResponse(
                call: Call<List<Pollerias>>,
                response: Response<List<Pollerias>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    callback(Result.Success(response.body()!!))
                } else {
                    callback(Result.Error("No data found"))
                }
            }

            override fun onFailure(call: Call<List<Pollerias>>, t: Throwable) {
                callback(Result.Error(t.message.toString()))
            }
        })
    }
}

