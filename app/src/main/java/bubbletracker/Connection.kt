package bubbletracker

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "connections")
class Connection (
    @ColumnInfo(name = "direct_connections") var directConnections: Int,
    var longitude: String,
    var latitude: String,
    var email: String
) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}