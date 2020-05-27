package bubbletracker

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import java.lang.ref.WeakReference

class LoadDatabaseTask(mainActivity: MainActivity) : AsyncTask<Unit, Unit, ConnectionDatabase?>() {
    private val mainActivity : WeakReference<MainActivity> = WeakReference(mainActivity)

    override fun doInBackground(vararg params: Unit?): ConnectionDatabase? {
        var database: ConnectionDatabase? = null
        mainActivity.get()?.let{
            database = Room.databaseBuilder(it.applicationContext, ConnectionDatabase::class.java, "random").build()
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
        val connectionDao : ConnectionDao = database.connectionDao()
        return connectionDao.getAll()
    }

    override fun onPostExecute(connections: List<Connection>) {
        mainActivity.connections = connections.toMutableList()
    }
}

class LoadUserTask(private val database: ConnectionDatabase,
                   private val mainActivity: MainActivity): AsyncTask<Unit, Unit, UserData>(){
    override fun doInBackground(vararg params: Unit?): UserData {
        val connectionDao : ConnectionDao = database.connectionDao()
        return connectionDao.getUserData()
    }

    override fun onPostExecute(result: UserData?) {
        mainActivity.userData = result
    }
}

class NewConnectionTask(private val database: ConnectionDatabase,
                  private val connection: Connection) : AsyncTask<Unit, Unit, Unit>() {
    override fun doInBackground(vararg p0: Unit?) {
        //todo check this is correct
        connection.email = database.connectionDao().insert(connection).toString()
    }
}

class UpdateUserTask(private  val database: ConnectionDatabase,
                     private val userData: UserData) : AsyncTask<Unit, Unit, Unit>() {
    override fun doInBackground(vararg params: Unit?) {
        database.connectionDao().upsert(userData)
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
