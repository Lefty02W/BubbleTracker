package bubbletracker

import androidx.room.*

@Dao
interface ConnectionDao {
    @Query("Select * FROM connections")
    fun getAll(): List<Connection>

    @Insert
    fun insert(connection: Connection): Long

    @Update
    fun update(connection: Connection)

    @Delete
    fun delete(connection: Connection)
}