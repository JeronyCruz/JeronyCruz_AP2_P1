package edu.ucne.jeronycruz_ap2_p1.presentation.navigation

import kotlinx.serialization.Serializable


sealed class Screen{
    @Serializable
    data class Tarea(val Id: Int?): Screen()

    @Serializable
    data object List: Screen()
}