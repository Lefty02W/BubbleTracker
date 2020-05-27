package data.db.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import bubbletracker.BubbleRepository
import data.db.BubbleDatabase
import data.db.dao.UserDataDao
import data.db.entity.Connection
import data.db.entity.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BubbleViewModel (application: Application) : AndroidViewModel(application){
    private val bubbleRepository: BubbleRepository
    val allConnections: LiveData<List<Connection>>
    val currentUser: LiveData<UserData>

    init {
        val userDataDao = BubbleDatabase.getDatabase(application).userDataDao()
        val connectionDao = BubbleDatabase.getDatabase(application).connectionDao()
        bubbleRepository = BubbleRepository(userDataDao, connectionDao)
        allConnections = bubbleRepository.allConnections
        currentUser = bubbleRepository.userData
    }

    fun insertConnection(connection: Connection) = viewModelScope.launch(Dispatchers.IO){
        connection.id = bubbleRepository.insertConnection(connection)
    }

    fun updateConnection(connection: Connection) = viewModelScope.launch(Dispatchers.IO){
        bubbleRepository.updateConnection(connection)
    }

    fun deleteConnection(connection: Connection){
        bubbleRepository.deleteConnection(connection)
    }

    fun upsertUser(userData: UserData) = viewModelScope.launch(Dispatchers.IO){
        bubbleRepository.upsertUser(userData)
    }
}