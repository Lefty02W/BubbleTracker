package data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import data.db.entity.Connection

@Dao
interface ConnectionDao {

    @Query("Select * FROM connections")
    fun getAll(): LiveData<List<Connection>>

    @Insert
    fun insert(connection: Connection): Long

    @Update
    fun update(connection: Connection)

    @Delete
    fun delete(connection: Connection)

}