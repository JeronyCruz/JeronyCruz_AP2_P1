package edu.ucne.jeronycruz_ap2_p1.presentation.tarea

sealed interface TareaEvent{
    data class TareaChange(val tareaId: Int) : TareaEvent
    data class DescripcionChange(val descripcion: String) : TareaEvent
    data class TiempoChange(val tiempo: Int): TareaEvent
    data object save: TareaEvent
    data object delete: TareaEvent
    data object new: TareaEvent

}