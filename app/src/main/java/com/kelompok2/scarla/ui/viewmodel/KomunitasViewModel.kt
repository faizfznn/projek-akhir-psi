package com.kelompok2.scarla.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.kelompok2.scarla.firebase.FirestoreInitializer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ──────────────────────────────────────────────────────────────────────────────
// DATA MODELS
// ──────────────────────────────────────────────────────────────────────────────

data class CommunityData(
        val id: String = "",
        val name: String = "",
        val iconRes: String = "",
        val description: String = "",
        val memberCount: Int = 0,
        val isJoined: Boolean = false
)

data class CommunityChannel(
        val id: String = "",
        val name: String = "",
        val type: String = "discussion",
        val lastMessage: String = "",
        val lastMessageAt: Long = 0L
)

data class CommunityMessage(
        val id: String = "",
        val senderUid: String = "",
        val senderName: String = "",
        val text: String = "",
        val timestampMillis: Long = 0L
)

data class KomunitasUiState(
        val joinedCommunities: List<CommunityData> = emptyList(),
        val discoverCommunities: List<CommunityData> = emptyList(),
        val allCommunities: List<CommunityData> = emptyList(),
        val channels: List<CommunityChannel> = emptyList(),
        val messages: List<CommunityMessage> = emptyList(),
        val isLoading: Boolean = true,
        val isJoining: Set<String> = emptySet(), // communityIds currently being joined
        val currentMessage: String = "",
        val activeCommunityId: String? = null,
        val activeCommunityName: String = "",
        val activeChannelId: String? = null,
        val activeChannelName: String = ""
)

// ──────────────────────────────────────────────────────────────────────────────
// VIEW MODEL
// ──────────────────────────────────────────────────────────────────────────────

class KomunitasViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val TAG = "KomunitasViewModel"

    private val _uiState = MutableStateFlow(KomunitasUiState())
    val uiState: StateFlow<KomunitasUiState> = _uiState.asStateFlow()

    // Listener registrations
    private var communitiesListener: ListenerRegistration? = null
    private var channelsListener: ListenerRegistration? = null
    private var messagesListener: ListenerRegistration? = null
    private val memberListeners = mutableListOf<ListenerRegistration>()

    init {
        // Seed communities first, then start listening
        viewModelScope.launch {
            Log.d(TAG, "🚀 Init: Seeding communities...")
            try {
                FirestoreInitializer.seedCommunities()
                Log.d(TAG, "✅ Seeding complete, starting listener")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Seeding failed: ${e.message}", e)
            }
            startListeningCommunities()
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // REACTIVE: Listen to all communities + membership status
    // ─────────────────────────────────────────────────────────────────────

    private fun startListeningCommunities() {
        val myUid = auth.currentUser?.uid
        if (myUid == null) {
            Log.e(TAG, "❌ User not logged in, cannot listen to communities")
            _uiState.update { it.copy(isLoading = false) }
            return
        }

        Log.d(TAG, "👂 Starting communities listener for uid=$myUid")

        communitiesListener =
                db.collection("communities").addSnapshotListener { snap, error ->
                    if (error != null) {
                        Log.e(TAG, "❌ Communities listener error: ${error.message}")
                        _uiState.update { it.copy(isLoading = false) }
                        return@addSnapshotListener
                    }

                    val allCommunities =
                            snap?.documents?.map { doc ->
                                CommunityData(
                                        id = doc.id,
                                        name = doc.getString("name") ?: "",
                                        iconRes = doc.getString("iconRes") ?: "",
                                        description = doc.getString("description") ?: "",
                                        memberCount = (doc.getLong("memberCount") ?: 0).toInt(),
                                        isJoined = false // Will be resolved per-community below
                                )
                            }
                                    ?: emptyList()

                    Log.d(
                            TAG,
                            "📦 Communities loaded: ${allCommunities.size} items → ${allCommunities.map { it.name }}"
                    )

                    // For each community, check membership
                    checkMemberships(myUid, allCommunities)
                }
    }

    /**
     * Check membership status for each community and update UI state. Uses individual membership
     * checks (efficient for small community count).
     */
    private fun checkMemberships(uid: String, communities: List<CommunityData>) {
        // Clear old member listeners
        memberListeners.forEach { it.remove() }
        memberListeners.clear()

        // Track membership status per community
        val membershipMap = mutableMapOf<String, Boolean>()
        var checksCompleted = 0

        if (communities.isEmpty()) {
            _uiState.update {
                it.copy(
                        joinedCommunities = emptyList(),
                        discoverCommunities = emptyList(),
                        allCommunities = emptyList(),
                        isLoading = false
                )
            }
            return
        }

        communities.forEach { community ->
            val listener =
                    db.collection("communities")
                            .document(community.id)
                            .collection("members")
                            .document(uid)
                            .addSnapshotListener { memberSnap, memberError ->
                                if (memberError != null) {
                                    Log.e(
                                            TAG,
                                            "Member check error for ${community.id}: ${memberError.message}"
                                    )
                                    membershipMap[community.id] = false
                                } else {
                                    membershipMap[community.id] = memberSnap?.exists() == true
                                }

                                checksCompleted++

                                // Update state when we have info for all communities
                                // (or on each update for reactivity)
                                if (membershipMap.size == communities.size) {
                                    val updatedCommunities =
                                            communities.map { c ->
                                                c.copy(isJoined = membershipMap[c.id] == true)
                                            }

                                    val joined = updatedCommunities.filter { it.isJoined }
                                    val discover = updatedCommunities.filter { !it.isJoined }

                                    _uiState.update { state ->
                                        state.copy(
                                                joinedCommunities = joined,
                                                discoverCommunities = discover,
                                                allCommunities = updatedCommunities,
                                                isLoading = false
                                        )
                                    }
                                }
                            }
            memberListeners.add(listener)
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // ACTIONS: Join / Leave
    // ─────────────────────────────────────────────────────────────────────

    fun joinCommunity(communityId: String) {
        // Mark as joining (for UI loading state)
        _uiState.update { it.copy(isJoining = it.isJoining + communityId) }

        viewModelScope.launch {
            try {
                FirestoreInitializer.joinCommunity(communityId)
                // Snapshot listener will automatically update the UI
            } catch (e: Exception) {
                Log.e(TAG, "joinCommunity error: ${e.message}", e)
            } finally {
                _uiState.update { it.copy(isJoining = it.isJoining - communityId) }
            }
        }
    }

    fun leaveCommunity(communityId: String) {
        viewModelScope.launch {
            try {
                FirestoreInitializer.leaveCommunity(communityId)
                // Snapshot listener will automatically update the UI
            } catch (e: Exception) {
                Log.e(TAG, "leaveCommunity error: ${e.message}", e)
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // CHANNELS: Listen to channels of a specific community
    // ─────────────────────────────────────────────────────────────────────

    fun openCommunityChannels(communityId: String, communityName: String) {
        channelsListener?.remove()

        _uiState.update {
            it.copy(
                    activeCommunityId = communityId,
                    activeCommunityName = communityName,
                    channels = emptyList()
            )
        }

        channelsListener =
                db.collection("communities")
                        .document(communityId)
                        .collection("channels")
                        .addSnapshotListener { snap, error ->
                            if (error != null) {
                                Log.e(TAG, "Channels listener error: ${error.message}")
                                return@addSnapshotListener
                            }

                            val channels =
                                    snap?.documents?.map { doc ->
                                        CommunityChannel(
                                                id = doc.id,
                                                name = doc.getString("name") ?: "",
                                                type = doc.getString("type") ?: "discussion",
                                                lastMessage = doc.getString("lastMessage") ?: "",
                                                lastMessageAt =
                                                        doc.getTimestamp("lastMessageAt")
                                                                ?.toDate()
                                                                ?.time
                                                                ?: 0L
                                        )
                                    }
                                            ?: emptyList()

                            _uiState.update { it.copy(channels = channels) }
                        }
    }

    // Toggle expand/collapse komunitas di halaman Komunitas
    fun toggleCommunityChannels(communityId: String, communityName: String) {
        val current = _uiState.value.activeCommunityId
        if (current == communityId) {
            // collapse
            channelsListener?.remove()
            channelsListener = null
            _uiState.update {
                it.copy(activeCommunityId = null, activeCommunityName = "", channels = emptyList())
            }
        } else {
            // expand komunitas yang dipilih
            openCommunityChannels(communityId, communityName)
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // MESSAGES: Real-time channel messages
    // ─────────────────────────────────────────────────────────────────────

    fun openChannel(
            communityId: String,
            channelId: String,
            communityName: String,
            channelName: String
    ) {
        messagesListener?.remove()

        _uiState.update {
            it.copy(
                    activeCommunityId = communityId,
                    activeCommunityName = communityName,
                    activeChannelId = channelId,
                    activeChannelName = channelName,
                    messages = emptyList(),
                    currentMessage = ""
            )
        }

        messagesListener =
                db.collection("communities")
                        .document(communityId)
                        .collection("channels")
                        .document(channelId)
                        .collection("messages")
                        .orderBy("timestamp", Query.Direction.ASCENDING)
                        .addSnapshotListener { snap, error ->
                            if (error != null) {
                                Log.e(TAG, "Messages listener error: ${error.message}")
                                return@addSnapshotListener
                            }

                            val messages =
                                    snap?.documents?.map { doc ->
                                        CommunityMessage(
                                                id = doc.id,
                                                senderUid = doc.getString("senderUid") ?: "",
                                                senderName = doc.getString("senderName") ?: "",
                                                text = doc.getString("text") ?: "",
                                                timestampMillis =
                                                        doc.getTimestamp("timestamp")
                                                                ?.toDate()
                                                                ?.time
                                                                ?: 0L
                                        )
                                    }
                                            ?: emptyList()

                            _uiState.update { it.copy(messages = messages) }
                        }
    }

    fun closeChannel() {
        messagesListener?.remove()
        messagesListener = null
        _uiState.update {
            it.copy(
                    activeChannelId = null,
                    activeChannelName = "",
                    messages = emptyList(),
                    currentMessage = ""
            )
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // MESSAGING
    // ─────────────────────────────────────────────────────────────────────

    fun onMessageChange(text: String) {
        _uiState.update { it.copy(currentMessage = text) }
    }

    fun sendMessage() {
        val communityId = _uiState.value.activeCommunityId ?: return
        val channelId = _uiState.value.activeChannelId ?: return
        val text = _uiState.value.currentMessage.trim()
        if (text.isBlank()) return

        // Optimistic: clear input immediately
        _uiState.update { it.copy(currentMessage = "") }

        viewModelScope.launch {
            try {
                FirestoreInitializer.sendCommunityMessage(communityId, channelId, text)
            } catch (e: Exception) {
                Log.e(TAG, "sendMessage error: ${e.message}", e)
                // Restore text on failure
                _uiState.update { it.copy(currentMessage = text) }
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // CLEANUP
    // ─────────────────────────────────────────────────────────────────────

    override fun onCleared() {
        super.onCleared()
        communitiesListener?.remove()
        channelsListener?.remove()
        messagesListener?.remove()
        memberListeners.forEach { it.remove() }
    }
}
