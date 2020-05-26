package bubbletracker

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import java.lang.ref.WeakReference

class LoadDatabaseTask(mainActivity: MainActivity, var context: Context) : AsyncTask<Unit, Unit, ConnectionDatabase?>() {
    private val mainActivity : WeakReference<MainActivity> = WeakReference(mainActivity)

    override fun doInBackground(vararg params: Unit?): ConnectionDatabase? {
        var database: ConnectionDatabase? = null
        mainActivity.get()?.let{
            database = Room.databaseBuilder(context.applicationContext, ConnectionDatabase::class.java, "connections").build()
        }
        return database
    }

    override fun onPostExecute(result: ConnectionDatabase?) {
        mainActivity.get()?.let {
            it.database = result
        }
    }

}

class LoadConnectionsTask(private val database: ConnectionDatabase,
                    private val mainActivity: MainActivity) : AsyncTask<Unit, Unit, List<Connection>>() {
    override fun doInBackground(vararg p0: Unit?): List<Connection> {
        val songDao = database.connectionDao()
        return songDao.getAll()
    }

    override fun onPostExecute(connections: List<Connection>) {
        mainActivity.connections = connections.toMutableList()
    }
}

class NewConnectionTask(private val database: ConnectionDatabase,
                  private val connection: Connection) : AsyncTask<Unit, Unit, Unit>() {
    override fun doInBackground(vararg p0: Unit?) {
        //todo check this is correct
        connection.email = database.connectionDao().insert(connection).toString()
    }
}

class UpdateConnectionTask(private val database: ConnectionDatabase,
                     private val connection: Connection) : AsyncTask<Unit, Unit, Unit>() {
    override fun doInBackground(vararg p0: Unit?) {
        database.connectionDao().update(connection)
    }
}

class DeleteConnectionTask(private val database: ConnectionDatabase,
                     private val connection: Connection) : AsyncTask<Unit, Unit, Unit>() {
    override fun doInBackground(vararg p0: Unit?) {
        database.connectionDao().delete(connection)
    }
}

class ClearDatabaseTask(private val database: ConnectionDatabase) : AsyncTask<Unit, Unit, Unit>() {
    override fun doInBackground(vararg p0: Unit?) {
        database.clearAllTables()
    }
}
