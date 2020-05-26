package bubbletracker

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "connections")
class Connection (
    @ColumnInfo(name = "direct_connections") var directConnections: Int,
    var longitude: Double,
    var latitude: Double
) {
    @PrimaryKey(autoGenerate = false)
    lateinit var email: String
}