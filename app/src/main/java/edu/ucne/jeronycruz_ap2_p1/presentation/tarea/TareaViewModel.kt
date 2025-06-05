package edu.ucne.jeronycruz_ap2_p1.presentation.tarea

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.jeronycruz_ap2_p1.data.local.entities.TareaEntity
import edu.ucne.jeronycruz_ap2_p1.data.repository.TareaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TareaViewModel @Inject constructor(
    private val tareaRepository: TareaRepository
):ViewModel() {
    private val _uiState = MutableStateFlow(TareaUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getTareas()
    }

    fun OnEvent(event: TareaEvent){
        when(event){
            is TareaEvent.DescripcionChange -> OnDescripcionChange(event.descripcion)
            is TareaEvent.TareaChange -> OnTareaChange(event.tareaId)
            is TareaEvent.TiempoChange -> OnTiempoChange(event.tiempo)
            TareaEvent.delete -> deteleTarea()
            TareaEvent.new -> Nuevo()
            TareaEvent.save -> viewModelScope.launch { saveTarea() }
        }
    }

    private fun Nuevo(){
        _uiState.update {
            it.copy(
                tareaId = null,
                descripcion = "",
                tiempo = 0,
                errorMessage = ""
            )
        }
    }

    private fun getTareas(){
        viewModelScope.launch {
            tareaRepository.getAll().collect{ tareas ->
                _uiState.update {
                    it.copy(tareas = tareas)
                }
            }
        }
    }

    fun findTarea(tareaId: Int){
        viewModelScope.launch {
            if(tareaId > 0){
                val tarea = tareaRepository.find(tareaId)
                _uiState.update {
                    it.copy(
                        tareaId = tarea?.tareaId,
                        descripcion = tarea?.descripcion ?: "",
                        tiempo = tarea?.tiempo ?: 0
                    )
                }
            }
        }
    }

    suspend fun saveTarea():Boolean {
        return if(_uiState.value.descripcion.isNullOrBlank() || _uiState.value.tiempo <=0){
            _uiState.update {
                it.copy(errorMessage = "Campos Vacios o Invalidos")
            }
            false
        }else{
            tareaRepository.save(_uiState.value.toEntity())
            true
        }
    }

    private fun deteleTarea(){
        viewModelScope.launch {
            tareaRepository.delete(_uiState.value.toEntity())
        }
    }

    private fun OnTiempoChange(tiempo: Int){
        _uiState.update {
            it.copy(tiempo = tiempo)
        }
    }

    private fun OnDescripcionChange(descripcion: String){
        _uiState.update {
            it.copy(descripcion = descripcion)
        }
    }

    private fun OnTareaChange(tareaId: Int){
        _uiState.update {
            it.copy(tareaId = tareaId)
        }
    }
}


fun TareaUiState.toEntity() = TareaEntity(
    tareaId = tareaId,
    descripcion = descripcion ?: "",
    tiempo = tiempo ?: 0
)
