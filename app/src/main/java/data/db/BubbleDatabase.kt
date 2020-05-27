package data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import data.db.dao.ConnectionDao
import data.db.dao.UserDataDao
import data.db.entity.Connection
import data.db.entity.UserData

@Database(
    entities = [UserData::class, Connection::class],
    version = 1
)
abstract class BubbleDatabase: RoomDatabase(){
    abstract fun userDataDao(): UserDataDao
    abstract fun connectionDao(): ConnectionDao

    companion object {
        @Volatile private var instance: BubbleDatabase? = null

        fun getDatabase(context: Context): BubbleDatabase {
            if (instance != null){
                return instance as BubbleDatabase
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext, BubbleDatabase::class.java, "bubbleDb")
                    .build()
                this.instance = instance
                return instance
            }
        }
    }
}