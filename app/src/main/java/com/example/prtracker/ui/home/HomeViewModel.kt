import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prtracker.Graph
import com.example.prtracker.data.room.models.Lifts
import com.example.prtracker.data.room.models.PR
import com.example.prtracker.data.room.models.User
import com.example.prtracker.ui.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: Repository = Graph.repository
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    init {
        getUsers()
    }

    private fun getUsers() {
        viewModelScope.launch {
            repository.getUsers().collectLatest {
                _state.value = _state.value.copy(users = it)
            }
        }
    }

    fun insertUser(user: User) {
        viewModelScope.launch {
            repository.insertUser(user)
        }
    }
}

data class HomeState(
    val users: List<User> = emptyList(),
    val lifts: List<Lifts> = emptyList(),
    val PRs: List<PR> = emptyList(),
    val prUpdated: Boolean = false
)