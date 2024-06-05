import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prtracker.Graph
import com.example.prtracker.data.room.models.LiftWithPR
import com.example.prtracker.data.room.models.Lifts
import com.example.prtracker.data.room.models.PR
import com.example.prtracker.data.room.models.User
import com.example.prtracker.ui.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date

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
    fun getUserPRsWithLifts(userId: Int): Flow<List<LiftWithPR>> {
        return repository.getUserPRsWithLifts(userId)
    }
    fun insertPR(userId:Int, liftName:String, weight:Double, repetitions:Int){
        viewModelScope.launch{
            var lift = repository.findLiftByName(liftName)
            if (lift == null) {
                lift = Lifts(id = 0, lift = liftName)
                repository.insertLift(lift)
                lift = repository.findLiftByName(liftName)
            }

            val pr = lift?.let { PR(userId = userId, liftId = it.id, weight = weight, repetitions = repetitions, date = Date()) }
            if (pr != null) {
                repository.insertPR(pr)
            }
        }
    }

    fun getUserPRs(userId: Int): Flow<List<PR>> {
        return repository.getUserPR(userId)
    }

    fun deletePR(pr: PR) {
        viewModelScope.launch{
            repository.deletePR(pr)
        }
    }
}

data class HomeState(
    val users: List<User> = emptyList(),
    val lifts: List<Lifts> = emptyList(),
    val PRs: List<PR> = emptyList(),
    val prUpdated: Boolean = false
)