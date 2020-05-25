package data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import data.db.dao.UserDataDao
import data.db.entity.UserData

@Database(
    entities = [UserData::class],
    version = 1
)
abstract class BubbleDatabase: RoomDatabase(){
    abstract fun userDataDao(): UserDataDao

    companion object {
        @Volatile private var instance: BubbleDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                BubbleDatabase::class.java, "bubble.db")
                .build()
    }
}