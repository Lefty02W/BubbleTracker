package data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.PropertyKey

const val CURRENT_USER_ID = 0

@Entity(tableName = "user_data")
class UserData (
    @ColumnInfo(name = "personal_email") var personalEmail: String,

    @ColumnInfo(name = "personal_name") var personalName: String,

    @ColumnInfo(name = "business_email") var businessEmail: String,

    @ColumnInfo(name = "business_name") var businessName:String
){
    @PrimaryKey(autoGenerate = false) var id: Int = CURRENT_USER_ID
}
