package com.kelompok2.scarla.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.kelompok2.scarla.R
import com.kelompok2.scarla.ui.theme.*

@Composable
fun AuthChoiceScreen(
    onBack: () -> Unit,
    onDaftarSekarang: () -> Unit,
    onMasuk: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AuthBackground)
    ) {
        BubbleDecoration(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 24.dp, end = 8.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(top = 48.dp)
                    .size(40.dp)
                    .border(1.dp, Neutral300, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Neutral800
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "SCARLA",
                    style = PoppinsH4Bold,
                    color = Neutral900
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Solusi Cari Teman Belajar",
                    style = PoppinsSmallRegular,
                    color = Secondary500
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Begin")
                        append("\n")
                        append("Your ")
                        withStyle(SpanStyle(background = Primary500, color = Neutral900)) {
                            append("Journey")
                        }
                        append("\n")
                        append("to ")
                        withStyle(SpanStyle(background = Primary500, color = Neutral900)) {
                            append("Smart")
                        }
                    },
                    style = PoppinsH5Bold,
                    color = Neutral900,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.maskot_splash),
                        contentDescription = "Maskot burung hantu",
                        modifier = Modifier
                            .padding(0.dp)
                            .width(202.5291.dp)
                            .height(166.42667.dp)
                            .absoluteOffset(x = -80.dp, y = 70.dp)
                            .rotate(-33.17f)
                    )
                    Image(
                        painter = painterResource(R.drawable.maskot_splash),
                        contentDescription = "Maskot burung hantu",
                        modifier = Modifier
                            .padding(0.dp)
                            .width(202.5291.dp)
                            .height(166.42667.dp)
                            .absoluteOffset(x = 70.dp, y = 70.dp)
                            .rotate(25.68f)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Primary500),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Ikon Google
                    Box(
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable { /* Google Sign-In */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "G",
                            style = PoppinsMediumBold,
                            color = Color(0xFF4285F4)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable { /* Apple Sign-In */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "",
                            style = PoppinsMediumBold,
                            color = Neutral900
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(start = 4.dp)
                            .clickable { onDaftarSekarang() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Daftar Sekarang",
                            style = PoppinsSmallMedium,
                            color = Neutral900
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Text(
                        text = "Sudah punya akun? ",
                        style = PoppinsSmallRegular,
                        color = Neutral700
                    )
                    Text(
                        text = "Masuk",
                        style = PoppinsSmallMedium,
                        color = Secondary500,
                        modifier = Modifier.clickable { onMasuk() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun BubbleDecoration(modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(160.dp)) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .offset(x = 40.dp, y = (-10).dp)
                .clip(CircleShape)
                .background(Primary500)
        )
        Box(
            modifier = Modifier
                .size(50.dp)
                .offset(x = 10.dp, y = 40.dp)
                .clip(CircleShape)
                .background(Primary500)
        )
        Box(
            modifier = Modifier
                .size(30.dp)
                .offset(x = 70.dp, y = 50.dp)
                .clip(CircleShape)
                .background(Secondary500)
        )
    }
}