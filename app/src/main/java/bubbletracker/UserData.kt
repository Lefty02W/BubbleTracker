package bubbletracker

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data")
class UserData (
    @ColumnInfo(name = "personal_email") var personalEmail: String,

    @ColumnInfo(name = "personal_name") var personalName: String,

    @ColumnInfo(name = "business_email") var businessEmail: String,

    @ColumnInfo(name = "business_name") var businessName:String
){
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}