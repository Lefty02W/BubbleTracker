package data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import data.db.entity.USER_ID
import data.db.entity.UserData

@Dao
interface UserDataDao {
//    @Query("SELECT * FROM USER_DATA")
//    fun getAll(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(userData: UserData)

    @Query(value = "SELECT * FROM user_data where userId = $USER_ID")
    fun getUserData(): LiveData<UserData>

//    @Insert
//    fun insertAll(vararg users: User)

//    @Delete
//    fun delete(user: User)
}