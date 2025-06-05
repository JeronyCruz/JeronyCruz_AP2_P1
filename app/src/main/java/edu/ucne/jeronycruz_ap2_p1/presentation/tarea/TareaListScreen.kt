package edu.ucne.jeronycruz_ap2_p1.presentation.tarea

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.jeronycruz_ap2_p1.data.local.entities.TareaEntity

@Composable
fun TareaListScreen(
    viewModel: TareaViewModel = hiltViewModel(),
    goToTareas: (Int) -> Unit,
    createTarea: () -> Unit,
    deleteTarea: ((TareaEntity) -> Unit)? = null
){
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    TareaBodyScreen(
        tareas = state.tareas,
        goToTarea = { id -> goToTareas(id)},
        createTarea = createTarea,
        deleteTarea = { tarea ->
            viewModel.OnEvent(TareaEvent.TareaChange(tarea.tareaId ?: 0))
            viewModel.OnEvent((TareaEvent.delete))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareaBodyScreen(
    tareas: List<TareaEntity>,
    goToTarea: (Int) -> Unit,
    createTarea: () -> Unit,
    deleteTarea: (TareaEntity) -> Unit
){
    Scaffold(
        modifier = Modifier
            .fillMaxWidth(),
        topBar = {
            TopAppBar(
                title = { Text("Lista de Tareas") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = createTarea
            ) {
                Icon(Icons.Filled.Add, "Agregar")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {
            items(tareas){ tareas ->
                TareaCardItem(
                    tarea = tareas,
                    goToTarea = {goToTarea(tareas.tareaId ?: 0)},
                    deleteTarea = {deleteTarea(tareas)}
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

    }
}

@Composable
fun TareaCardItem(
    tarea: TareaEntity,
    goToTarea: () -> Unit,
    deleteTarea: (TareaEntity) -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = "Id: ${tarea.tareaId}",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = tarea.descripcion,
                    color = Color.Black,
                    fontSize = 14.sp
                )
                Text(
                    text = "${tarea.tiempo} Minutos",
                    color = Color.Black
                )
            }
            IconButton(
                onClick = goToTarea
            ) {
                Icon(Icons.Default.Edit, "Editar", tint = MaterialTheme.colors.primary)
            }
            IconButton(
                onClick = {deleteTarea(tarea)}
            ) {
                Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colors.secondary)
            }
        }

    }
}