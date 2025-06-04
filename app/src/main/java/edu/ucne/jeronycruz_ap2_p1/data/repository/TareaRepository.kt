package edu.ucne.jeronycruz_ap2_p1.data.repository

import edu.ucne.jeronycruz_ap2_p1.data.local.dao.TareaDao
import edu.ucne.jeronycruz_ap2_p1.data.local.entities.TareaEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TareaRepository @Inject constructor(
    private val dao: TareaDao
) {
    suspend fun save(tarea: TareaEntity) = dao.save(tarea)

    suspend fun find(id: Int) = dao.find(id)

    suspend fun delete(tarea: TareaEntity) = dao.delete(tarea)

    fun getAll(): Flow<List<TareaEntity>> = dao.getAll()
}