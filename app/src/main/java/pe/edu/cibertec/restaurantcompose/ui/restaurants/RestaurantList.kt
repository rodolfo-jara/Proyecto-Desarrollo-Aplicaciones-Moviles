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
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.skydoves.landscapist.coil.CoilImage
import pe.edu.cibertec.restaurantcompose.data.model.Pollerias
import pe.edu.cibertec.restaurantcompose.data.model.Restaurant
import pe.edu.cibertec.restaurantcompose.data.repository.PolleriaRepository
import pe.edu.cibertec.restaurantcompose.data.repository.RestaurantRepository
import pe.edu.cibertec.restaurantcompose.util.Result


@Composable
fun RestaurantList(navController: NavController) {
    val allRestaurants = remember { mutableStateListOf<Restaurant>() }
    val filteredRestaurants = remember { mutableStateListOf<Restaurant>() }
    val isLoading = remember { mutableStateOf(true) }
    val showFavorites = remember { mutableStateOf(false) }

    val searchQuery = remember { mutableStateOf("") }

    val restaurantRepository = RestaurantRepository()
    val context = LocalContext.current
    val titleStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        textAlign = TextAlign.Center
    )
    val polleriaRepository = PolleriaRepository()
    val pollerias = remember { mutableStateListOf<Pollerias>() }

    polleriaRepository.getPollerias { result ->
        if (result is Result.Success) {
            pollerias.clear()
            pollerias.addAll(result.data!!)
        } else {
            Toast.makeText(context, result.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    restaurantRepository.getRestaurants { result ->
        if (result is Result.Success) {
            allRestaurants.clear()
            allRestaurants.addAll(result.data!!)
            filteredRestaurants.addAll(allRestaurants)
        } else {
            Toast.makeText(context, result.message.toString(), Toast.LENGTH_SHORT).show()
        }
        isLoading.value = false
    }

    fun filterRestaurants(query: String) {
        filteredRestaurants.clear()
        if (query.isNotEmpty()) {
            val searchResults = allRestaurants.filter { restaurant ->
                restaurant.title.contains(query, ignoreCase = true)
            }
            filteredRestaurants.addAll(searchResults)
        } else {
            filteredRestaurants.addAll(allRestaurants)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "BIENVENIDO\nELIGE EL RESTAURANTE QUE MÁS TE GUSTE",
            style = titleStyle,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchQuery.value,
            onValueChange = { newValue ->
                searchQuery.value = newValue
                filterRestaurants(newValue)
            },
            label = { Text("Buscar") },
            leadingIcon = { Icon(Icons.Default.Search, null) }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { showFavorites.value = false },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(text = "Todos")
            }
            Button(
                onClick = { showFavorites.value = true },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(text = "Favoritos")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    navController.navigate("pollerias")
                },
                colors = ButtonDefaults.textButtonColors(Color(0xFF4CAF50)),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(text = "Pollerías")
            }

            Button(
                onClick = {
                    navController.navigate("marinos")
                },
                colors = ButtonDefaults.textButtonColors(Color(0xFF4CAF50)),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(text = "Marino")
            }
            Button(
                onClick = {
                    navController.navigate("criollos")
                },
                colors = ButtonDefaults.textButtonColors(Color(0xFF4CAF50)),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(text = "Criollo")
            }
        }

            if (isLoading.value) {
            Dialog(onDismissRequest = { isLoading.value = false }) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                ) {
                    CircularProgressIndicator()
                }
            }
        } else {
            if (filteredRestaurants.isNotEmpty()) {
                val restaurantsToDisplay = if (showFavorites.value) {
                    filteredRestaurants.filter { it.isFavorite }
                } else {
                    filteredRestaurants
                }

                if (restaurantsToDisplay.isNotEmpty()) {
                    LazyColumn {
                        items(restaurantsToDisplay) { restaurant ->
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
                                            val isFavorite = remember { mutableStateOf(restaurant.isFavorite) }
                                            IconButton(
                                                onClick = {
                                                    isFavorite.value = !isFavorite.value
                                                    restaurant.isFavorite = isFavorite.value // Actualizar el estado del elemento
                                                },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Favorite,
                                                    contentDescription = null,
                                                    tint = if (isFavorite.value) Color.Red else Color.Gray
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Text(text = "No se encontraron resultados")
                }
            } else {
                Text(text = "No se encontraron resultados")
            }
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