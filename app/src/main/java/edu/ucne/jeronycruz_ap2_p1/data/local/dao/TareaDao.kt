package edu.ucne.jeronycruz_ap2_p1.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.jeronycruz_ap2_p1.data.local.entities.TareaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao{
    @Upsert
    suspend fun save(tarea: TareaEntity)

    @Query("""
            SELECT *
            FROM TAREAS
            WHERE tareaId =:id
            Limit 1
    """)
    suspend fun find(id: Int): TareaEntity?

    @Delete
    suspend fun delete(tarea: TareaEntity)

    @Query("SELECT * FROM Tareas")
    fun getAll(): Flow<List<TareaEntity>>
}