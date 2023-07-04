package pe.edu.cibertec.restaurantcompose.ui.marino

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import pe.edu.cibertec.restaurantcompose.data.repository.MarinoRepository
import pe.edu.cibertec.restaurantcompose.util.Result


@Composable
fun MarinoList(navController: NavController) {
    val allPollerias = remember { mutableStateListOf<Pollerias>() }
    val filteredPollerias = remember { mutableStateListOf<Pollerias>() }
    val isLoading = remember { mutableStateOf(true) }

    val searchQuery = remember { mutableStateOf("") }

    val marinoRepository = MarinoRepository()
    val context = LocalContext.current
    val titleStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        textAlign = TextAlign.Center
    )

    marinoRepository.getMarino { result ->
        if (result is Result.Success) {
            allPollerias.clear()
            allPollerias.addAll(result.data!!)
            filteredPollerias.addAll(allPollerias)
        } else {
            Toast.makeText(context, result.message.toString(), Toast.LENGTH_SHORT).show()
        }
        isLoading.value = false
    }

    fun filterPollerias(query: String) {
        filteredPollerias.clear()
        if (query.isNotEmpty()) {
            val searchResults = allPollerias.filter { polleria ->
                polleria.nombre.contains(query, ignoreCase = true)
            }
            filteredPollerias.addAll(searchResults)
        } else {
            filteredPollerias.addAll(allPollerias)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ENCUENTRA\nEL LUGAR QUE MÁS TE GUSTE",
            style = titleStyle,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchQuery.value,
            onValueChange = { newValue ->
                searchQuery.value = newValue
                filterPollerias(newValue)
            },
            label = { Text("Buscar") },
            leadingIcon = { Icon(Icons.Default.Search, null) }
        )

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
            if (filteredPollerias.isNotEmpty()) {
                LazyColumn {
                    items(filteredPollerias) { polleria ->
                        Box {
                            Card(modifier = Modifier.padding(start = 10.dp, top = 250.dp, end = 200.dp, bottom = 20.dp)) {
                                CoilImage(imageModel = { polleria.photo })
                            }

                            Column(modifier = Modifier.padding(start = 20.dp, top = 2.dp, end = 5.dp, bottom = 20.dp)) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            shape = RoundedCornerShape(1.dp)
                                        )
                                ) {
                                    Column(Modifier.padding(8.dp)) {
                                        Text(
                                            text = polleria.nombre,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )
                                        Text(text = "Dirección: ${polleria.direccion}")
                                        Text(text = "Ciudad: ${polleria.ciudad}")
                                        Text(text = "Distrito: ${polleria.distrito}")
                                        Text(text = "Teléfono: ${polleria.telefono}")
                                        Text(text = "Horario: ${polleria.horario}")
                                        Text(text = "Página web: ${polleria.pag}")
                                    }
                                }
                            }
                        }
                    }
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
                navController.popBackStack("restaurants", inclusive = true)
                navController.navigate("restaurants")
            }
        ) {
            Text(text = "Volver a la lista")
        }
    }
}