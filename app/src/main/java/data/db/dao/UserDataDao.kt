package data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import data.db.entity.CURRENT_USER_ID
import data.db.entity.UserData

@Dao
interface UserDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(userData: UserData)

    @Query("select * from user_data where id = $CURRENT_USER_ID")
    fun getUserData(): LiveData<UserData>

}