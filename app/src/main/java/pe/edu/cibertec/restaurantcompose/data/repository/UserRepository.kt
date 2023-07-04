package pe.edu.cibertec.restaurantcompose.data.repository

import pe.edu.cibertec.restaurantcompose.data.model.User
import pe.edu.cibertec.restaurantcompose.data.remote.ApiClient
import pe.edu.cibertec.restaurantcompose.data.remote.service.UserService
import pe.edu.cibertec.restaurantcompose.util.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(
    private val userService: UserService = ApiClient.getUserService()
) {
    private fun validateUser(username: String, callback: (Result<Boolean>) -> Unit) {

        userService.validateUser(username).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful && response.body() != null) {
                    if (response.body()!!.isEmpty()) {
                        callback(Result.Success(true))
                    } else {
                        callback(Result.Error("Username already registered", false))
                    }
                } else {
                    callback(Result.Error("No data found"))
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                callback(Result.Error(t.message.toString()))
            }
        })
    }

    fun createUser(username: String, password: String, callback: (Result<Boolean>) -> Unit) {
        validateUser(username) { result ->
            if (result is Result.Success) {
                userService.createUser(User(username, password)).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful && response.body() != null) {
                            callback(Result.Success(true))
                        }
                    }
                    override fun onFailure(call: Call<User>, t: Throwable) {
                        callback(Result.Error(t.message.toString()))
                    }
                })
            } else {
                callback(Result.Error(result.message.toString()))
            }
        }
    }

    fun login(username: String, password: String, callback: (Result<Boolean>) -> Unit) {
        userService.login(username, password).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {

                if (response.isSuccessful && response.body() != null) {
                    if (response.body()!!.isNotEmpty()) {
                        callback(Result.Success(true))
                    } else {
                        validateUser(username) { result ->
                            if (result is Result.Error && result.data != null && !result.data) {
                                callback(Result.Error("Wrong password"))
                            } else {
                                callback(Result.Error("Wrong username"))
                            }
                        }
                    }
                } else {
                    callback(Result.Error("No data found"))
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                callback(Result.Error(t.message.toString()))
            }
        })
    }
}