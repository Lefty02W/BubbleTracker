package data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "connections", indices = [Index(value = ["email"], unique = true)])
class Connection (
    @ColumnInfo(name = "direct_connections") var directConnections: Int,
    var longitude: String,
    var latitude: String,
    var email: String
) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}

