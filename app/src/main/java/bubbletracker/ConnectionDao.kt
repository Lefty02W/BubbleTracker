package bubbletracker

import androidx.room.*

@Dao
interface ConnectionDao {
    @Query("Select * FROM connections")
    fun getAll(): List<Connection>

    @Insert
    fun insert(connection: Connection): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(userData: UserData)

    @Query("select * from user_data where id = $CURRENT_USER_ID")
    fun getUserData(): UserData

    @Update
    fun update(connection: Connection)

    @Delete
    fun delete(connection: Connection)
}