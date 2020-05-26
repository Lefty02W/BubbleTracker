package bubbletracker

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Connection::class], version = 1)
abstract class ConnectionDatabase: RoomDatabase() {
    abstract fun connectionDao(): ConnectionDao
}