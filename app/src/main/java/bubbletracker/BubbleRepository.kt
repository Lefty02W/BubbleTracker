package bubbletracker

import androidx.lifecycle.LiveData
import data.db.dao.ConnectionDao
import data.db.dao.UserDataDao
import data.db.entity.Connection
import data.db.entity.UserData

class BubbleRepository(private val userDataDao: UserDataDao, private val connectionDao: ConnectionDao) {

    val userData = userDataDao.getUserData()
    val allConnections = connectionDao.getAll()

    fun getConnectionOnEmail(email: String): LiveData<List<Connection?>>{
        return connectionDao.getConnectionWithGivenEmail(email)
    }

    fun insertConnection(connection: Connection): Long{
        return connectionDao.insert(connection)
    }

    fun updateConnection(connection: Connection){
        connectionDao.update(connection)
    }

    fun deleteConnection(connection: Connection){
        connectionDao.delete(connection)
    }

    fun upsertUser(userData: UserData){
        userDataDao.upsert(userData)
    }
}