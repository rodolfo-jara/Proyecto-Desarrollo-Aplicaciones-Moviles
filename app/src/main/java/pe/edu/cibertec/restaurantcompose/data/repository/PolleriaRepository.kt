package pe.edu.cibertec.restaurantcompose.data.repository

import pe.edu.cibertec.restaurantcompose.data.model.Pollerias
import pe.edu.cibertec.restaurantcompose.data.remote.ApiClient
import pe.edu.cibertec.restaurantcompose.data.remote.service.PolleriasService
import pe.edu.cibertec.restaurantcompose.util.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PolleriaRepository(
    private val polleriasService: PolleriasService = ApiClient.getRePolleriaService()
) {

    fun getPollerias(callback: (Result<List<Pollerias>>) -> Unit) {

        polleriasService.getPollerias().enqueue(object : Callback<List<Pollerias>> {
            override fun onResponse(
                call: Call<List<Pollerias>>,
                response: Response<List<Pollerias>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    callback(Result.Success(response.body()!!))
                } else {
                    callback(Result.Error("No se encontraron datos"))
                }
            }

            override fun onFailure(call: Call<List<Pollerias>>, t: Throwable) {
                callback(Result.Error(t.message.toString()))
            }
        })
    }
}