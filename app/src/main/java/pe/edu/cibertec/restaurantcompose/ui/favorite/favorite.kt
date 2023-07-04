package pe.edu.cibertec.restaurantcompose.ui.favorite

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.skydoves.landscapist.coil.CoilImage
import pe.edu.cibertec.restaurantcompose.data.model.Restaurant
import pe.edu.cibertec.restaurantcompose.data.repository.RestaurantRepository
import pe.edu.cibertec.restaurantcompose.util.Result

@Composable
fun FavoriteList(navController: NavController) {
    val allRestaurants = remember { mutableStateListOf<Restaurant>() }
    val favoriteRestaurants = remember { mutableStateListOf<Restaurant>() }

    val restaurantRepository = RestaurantRepository()
    val context = LocalContext.current

    restaurantRepository.getRestaurants { result ->
        if (result is Result.Success) {
            allRestaurants.clear()
            allRestaurants.addAll(result.data!!)
            favoriteRestaurants.clear()
            favoriteRestaurants.addAll(allRestaurants.filter { it.isFavorite })
        } else {
            Toast.makeText(context, result.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (favoriteRestaurants.isNotEmpty()) {
            LazyColumn {
                items(favoriteRestaurants) { restaurant ->
                    Box {
                        Card(modifier = Modifier.padding(50.dp)) {
                            CoilImage(imageModel = { restaurant.posterUrl })
                        }

                        Column(modifier = Modifier.padding(50.dp)) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        shape = RoundedCornerShape(2.dp)
                                    )
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = restaurant.title,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(4.dp)
                                    )
                                    IconButton(
                                        onClick = {
                                            // Lógica para manejar el evento del corazón
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Favorite,
                                            contentDescription = null,
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Text(text = "No has seleccionado ningún restaurante favorito")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Button(
            onClick = {
                navController.popBackStack("login", inclusive = true)
                navController.navigate("login")
            }
        ) {
            Text(text = "Volver al inicio")
        }
    }
}
