package data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val USER_ID = 0

@Entity(tableName = "user_data")
data class UserData (
    @PrimaryKey(autoGenerate = false) val userId: Int = USER_ID,
    @ColumnInfo(name = "direct_connections") val directConnections: Int,
    @ColumnInfo(name = "indirect_connections") val indirectConnections: Int
    //todo add all stored user data
)