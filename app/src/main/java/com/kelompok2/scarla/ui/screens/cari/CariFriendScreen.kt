package com.kelompok2.scarla.ui.screens.cari


import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kelompok2.scarla.R
import com.kelompok2.scarla.ui.components.FriendAcceptedPopup
import com.kelompok2.scarla.ui.components.FriendCard
import com.kelompok2.scarla.ui.components.FriendRequestSubmittedPopup
import com.kelompok2.scarla.ui.components.FriendSearchBar
import com.kelompok2.scarla.ui.components.SendFriendRequestPopup
import com.kelompok2.scarla.ui.viewmodel.CardSwipeDirection
import com.kelompok2.scarla.ui.viewmodel.CariUiState
import com.kelompok2.scarla.ui.viewmodel.FriendProfile
import com.kelompok2.scarla.ui.viewmodel.CariViewModel
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CariFriendRoute(
    viewModel: CariViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CariFriendScreen(
        uiState = uiState,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onAcceptRequest = viewModel::acceptCurrentRequest,
        onRejectRequest = viewModel::rejectCurrentRequest,
        onDismissAcceptedPopup = viewModel::dismissFriendAcceptedPopup,
        onSearchCardClick = viewModel::openFriendRequestPopup,
        onRequestMessageChange = viewModel::onRequestMessageChange,
        onDismissRequestPopup = viewModel::dismissRequestPopup,
        onSubmitRequest = viewModel::submitFriendRequest,
        onDismissRequestSubmittedPopup = viewModel::dismissRequestSubmittedPopup
    )
}

@Composable
private fun CariFriendScreen(
    uiState: CariUiState,
    onSearchQueryChange: (String) -> Unit,
    onAcceptRequest: () -> Unit,
    onRejectRequest: () -> Unit,
    onDismissAcceptedPopup: () -> Unit,
    onSearchCardClick: (FriendProfile) -> Unit,
    onRequestMessageChange: (String) -> Unit,
    onDismissRequestPopup: () -> Unit,
    onSubmitRequest: () -> Unit,
    onDismissRequestSubmittedPopup: () -> Unit
) {
    val cardOffsetX by animateDpAsState(
        targetValue = when (uiState.swipeDirection) {
            CardSwipeDirection.LEFT -> (-360).dp
            CardSwipeDirection.RIGHT -> 360.dp
            null -> 0.dp
        },
        animationSpec = tween(durationMillis = CariViewModel.CARD_ANIMATION_DURATION_MS.toInt()),
        label = "request_card_offset"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFAF0))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Cari Teman",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )

            FriendSearchBar(
                query = uiState.searchQuery,
                onQueryChange = onSearchQueryChange
            )

            if (uiState.friendRequests.isNotEmpty()) {
                val currentRequest = uiState.friendRequests.first()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    FriendCard(
                        profile = currentRequest,
                        modifier = Modifier.offset(x = cardOffsetX),
                        showMatchCount = true
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onRejectRequest,
                        enabled = uiState.swipeDirection == null,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                    ) {
                        Text("Tolak")
                    }
                    Button(
                        onClick = onAcceptRequest,
                        enabled = uiState.swipeDirection == null,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                    ) {
                        Text("Terima")
                    }
                }
            }

            if (uiState.searchQuery.isNotBlank()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (uiState.searchResults.isEmpty()) {
                        item {
                            Text(
                                text = "Teman tidak ditemukan",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    } else {
                        items(uiState.searchResults, key = { it.id }) { friend ->
                            FriendCard(
                                profile = friend,
                                modifier = Modifier.padding(horizontal = 16.dp),
                                showMatchCount = false,
                                onClick = { onSearchCardClick(friend) }
                            )
                        }
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }

        if (uiState.showFriendAcceptedPopup) {
            FriendAcceptedPopup(onDismiss = onDismissAcceptedPopup)
        }

        if (uiState.showRequestPopup && uiState.selectedFriend != null) {
            SendFriendRequestPopup(
                friend = uiState.selectedFriend,
                message = uiState.requestMessage,
                onMessageChange = onRequestMessageChange,
                onDismiss = onDismissRequestPopup,
                onSubmit = onSubmitRequest
            )
        }

        if (uiState.showRequestSubmittedPopup) {
            FriendRequestSubmittedPopup(onDismiss = onDismissRequestSubmittedPopup)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SearchFriendScreenPreview() {
    // Dummy data untuk state
    val dummyState = CariUiState(
        searchQuery = "Alif",
        friendRequests = listOf(
            FriendProfile(
                id = "1",
                name = "Syarif Hidayat",
                age = 17,
                educationStatus = "SMA - Depok",
                origin = "Depok",
                interests = listOf("Matematika", "Fisika"),
                matchCount = 2,
                avatarResId = R.drawable.avatar_default
            )
        ),
        searchResults = listOf(
            FriendProfile(
                id = "2",
                name = "Alif Abyan Syafiq",
                age = 21,
                educationStatus = "Kuliah - Semarang",
                origin = "Semarang",
                interests = listOf("Informatika"),
                matchCount = 1,
                avatarResId = R.drawable.avatar_default
            )
        ),
    )

    CariFriendScreen(
        uiState = dummyState,
        onSearchQueryChange = {},
        onAcceptRequest = {},
        onRejectRequest = {},
        onDismissAcceptedPopup = {},
        onSearchCardClick = {},
        onRequestMessageChange = {},
        onDismissRequestPopup = {},
        onSubmitRequest = {},
        onDismissRequestSubmittedPopup = {},
    )
}
