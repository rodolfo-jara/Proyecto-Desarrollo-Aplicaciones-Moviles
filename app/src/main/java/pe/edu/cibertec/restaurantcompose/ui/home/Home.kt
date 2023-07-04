package pe.edu.cibertec.restaurantcompose.ui.home

import PolleriaList
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pe.edu.cibertec.restaurantcompose.ui.login.Login
import RestaurantList
import pe.edu.cibertec.restaurantcompose.ui.criollo.CriolloList
import pe.edu.cibertec.restaurantcompose.ui.favorite.FavoriteList
import pe.edu.cibertec.restaurantcompose.ui.marino.MarinoList
import pe.edu.cibertec.restaurantcompose.ui.signup.SignUp

@Composable
fun Home(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login"){
        composable("signup") {
            SignUp(navController)
        }
        composable("login"){
            Login(navController)
        }
        composable("restaurants") {
            RestaurantList(navController)
        }
        composable ("favorites") {
            FavoriteList(navController)
        }
        composable ("pollerias") {
            PolleriaList(navController)
        }
        composable ("marinos") {
            MarinoList(navController)
        }
        composable ("criollos") {
            CriolloList(navController)
        }
    }
}