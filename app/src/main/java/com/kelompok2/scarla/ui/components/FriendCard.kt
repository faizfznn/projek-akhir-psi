package com.kelompok2.scarla.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kelompok2.scarla.ui.viewmodel.FriendProfile
import com.kelompok2.scarla.ui.theme.Neutral100
import com.kelompok2.scarla.ui.theme.Neutral700
import com.kelompok2.scarla.ui.theme.Neutral800
import com.kelompok2.scarla.ui.theme.Primary500

@Composable
fun FriendCard(
    profile: FriendProfile,
    modifier: Modifier = Modifier,
    showMatchCount: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = profile.avatarResId),
                    contentDescription = profile.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(Neutral100)
                )

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = profile.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = Neutral800
                    )
                    Text(
                        text = "${profile.age} tahun",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Neutral700
                    )
                    Text(
                        text = profile.educationStatus,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Neutral700
                    )
                    Text(
                        text = profile.origin,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Neutral700
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Minat: ${profile.interests.joinToString()}",
                style = MaterialTheme.typography.bodyMedium,
                color = Neutral700
            )

            if (showMatchCount) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${profile.matchCount} kecocokan",
                    style = MaterialTheme.typography.labelLarge,
                    color = Primary500
                )
            }
        }
    }
}
