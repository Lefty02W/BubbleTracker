package data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "connections")
data class Connections (
    @PrimaryKey val email: String,
    @ColumnInfo(name = "direct_connections") val directConnections: Int,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "date_met") val dateMet: Date
)
