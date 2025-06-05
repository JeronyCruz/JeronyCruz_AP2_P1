package edu.ucne.jeronycruz_ap2_p1.presentation.tarea

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

@Composable
fun TareaScreen(
    tareaId: Int?,
    viewModel: TareaViewModel = hiltViewModel(),
    goback: () -> Unit
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(tareaId) {
        println("Id: $tareaId")
        tareaId?.let {
            if(it > 0){
                viewModel.findTarea(it)
            }
        }
    }

    TareaBodyScreen(
        uiState = uiState,
        viewModel::OnEvent,
        goback = goback,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareaBodyScreen(
    uiState: TareaUiState,
    onAction: (TareaEvent) -> Unit,
    goback: () -> Unit,
    viewModel: TareaViewModel

) {
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.tareaId != null && uiState.tareaId != 0) "Editar Tarea " else "Nueva Tarea") },
                navigationIcon = {
                    IconButton(
                        onClick = goback
                    ) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.tareaId?.toString() ?: "0",
                onValueChange = {},
                label = { Text("Id:") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.descripcion,
                onValueChange = { onAction(TareaEvent.DescripcionChange(it ?: "")) },
                label = { Text("Descripcion: ") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errorMessage != null
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.tiempo.toString(),
                onValueChange = { onAction(TareaEvent.TiempoChange(it.toIntOrNull() ?: 0)) },
                label = { Text("Tiempo en Minutos") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errorMessage != null
            )

            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = colors.error
                )
            }
            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    onClick = { onAction(TareaEvent.new) }
                ) {
                    Text("Limpiar")
                    Spacer(modifier = Modifier.height(8.dp))
                    Icon(Icons.Default.Refresh, "Limpiar")
                }

                OutlinedButton(
                    onClick = {
                        scope.launch {
                            val result = viewModel.saveTarea()
                            if (result) {
                                goback()
                            }
                        }
                    }
                ) {
                    Text("Guardar")
                    Spacer(modifier = Modifier.height(8.dp))
                    Icon(Icons.Default.Edit, "Guardar")

                }
            }
        }
    }
}