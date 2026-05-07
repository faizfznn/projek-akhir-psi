package com.kelompok2.scarla.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kelompok2.scarla.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Models
data class FriendProfile(
    val id: String,
    val name: String,
    val age: Int,
    val educationStatus: String,
    val origin: String,
    val interests: List<String>,
    val matchCount: Int,
    val avatarResId: Int
)

enum class CardSwipeDirection {
    LEFT,
    RIGHT
}

data class CariUiState(
    val friendRequests: List<FriendProfile> = emptyList(),
    val searchQuery: String = "",
    val searchResults: List<FriendProfile> = emptyList(),
    val selectedFriend: FriendProfile? = null,
    val requestMessage: String = "",
    val showRequestPopup: Boolean = false,
    val showRequestSubmittedPopup: Boolean = false,
    val showFriendAcceptedPopup: Boolean = false,
    val swipeDirection: CardSwipeDirection? = null
)

// Repository
interface ICariRepository {
    fun getAllProfiles(): List<FriendProfile>
    fun searchProfiles(query: String): List<FriendProfile>
}

class CariRepository : ICariRepository {
    private val allProfiles = listOf(
        FriendProfile(
            id = "f1",
            name = "Alya Putri",
            age = 21,
            educationStatus = "Mahasiswa S1",
            origin = "Bandung",
            interests = listOf("UI/UX", "K-Pop", "Fotografi"),
            matchCount = 8,
            avatarResId = R.drawable.avatar_1
        ),
        FriendProfile(
            id = "f2",
            name = "Raka Mahendra",
            age = 23,
            educationStatus = "Fresh Graduate",
            origin = "Jakarta",
            interests = listOf("Mobile Dev", "Musik", "Gaming"),
            matchCount = 6,
            avatarResId = R.drawable.avatar_2
        ),
        FriendProfile(
            id = "f3",
            name = "Dina Lestari",
            age = 22,
            educationStatus = "Mahasiswa S2",
            origin = "Surabaya",
            interests = listOf("Data Science", "Baca Buku", "Travel"),
            matchCount = 7,
            avatarResId = R.drawable.avatar_3
        )
    )

    override fun getAllProfiles(): List<FriendProfile> = allProfiles

    override fun searchProfiles(query: String): List<FriendProfile> {
        val normalizedQuery = query.trim()
        return if (normalizedQuery.isBlank()) {
            emptyList()
        } else {
            allProfiles.filter {
                it.name.contains(normalizedQuery, ignoreCase = true) ||
                    it.origin.contains(normalizedQuery, ignoreCase = true) ||
                    it.interests.any { interest ->
                        interest.contains(normalizedQuery, ignoreCase = true)
                    }
            }
        }
    }
}

class CariViewModel(
    private val repository: ICariRepository = CariRepository(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {
    companion object {
        internal const val CARD_ANIMATION_DURATION_MS = 320L
    }

    private val _uiState = MutableStateFlow(
        CariUiState(friendRequests = repository.getAllProfiles().take(1))
    )
    val uiState: StateFlow<CariUiState> = _uiState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        val normalizedQuery = query.trim()
        val results = repository.searchProfiles(normalizedQuery)

        _uiState.update {
            it.copy(
                searchQuery = normalizedQuery,
                searchResults = results,
                selectedFriend = null,
                showRequestPopup = false,
                requestMessage = ""
            )
        }
    }

    fun acceptCurrentRequest() {
        if (_uiState.value.friendRequests.isEmpty()) return
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(swipeDirection = CardSwipeDirection.RIGHT) }
            delay(CARD_ANIMATION_DURATION_MS)
            _uiState.update {
                it.copy(
                    friendRequests = it.friendRequests.drop(1),
                    swipeDirection = null,
                    showFriendAcceptedPopup = true
                )
            }
        }
    }

    fun rejectCurrentRequest() {
        if (_uiState.value.friendRequests.isEmpty()) return
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(swipeDirection = CardSwipeDirection.LEFT) }
            delay(CARD_ANIMATION_DURATION_MS)
            _uiState.update {
                it.copy(
                    friendRequests = it.friendRequests.drop(1),
                    swipeDirection = null
                )
            }
        }
    }

    fun dismissFriendAcceptedPopup() {
        _uiState.update { it.copy(showFriendAcceptedPopup = false) }
    }

    fun openFriendRequestPopup(friend: FriendProfile) {
        _uiState.update {
            it.copy(
                selectedFriend = friend,
                showRequestPopup = true,
                requestMessage = ""
            )
        }
    }

    fun onRequestMessageChange(message: String) {
        _uiState.update { it.copy(requestMessage = message) }
    }

    fun dismissRequestPopup() {
        _uiState.update {
            it.copy(
                showRequestPopup = false,
                selectedFriend = null,
                requestMessage = ""
            )
        }
    }

    fun submitFriendRequest() {
        _uiState.update {
            it.copy(
                showRequestPopup = false,
                selectedFriend = null,
                requestMessage = "",
                showRequestSubmittedPopup = true
            )
        }
    }

    fun dismissRequestSubmittedPopup() {
        _uiState.update { it.copy(showRequestSubmittedPopup = false) }
    }
}
