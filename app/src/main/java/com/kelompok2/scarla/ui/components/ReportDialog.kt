package com.kelompok2.scarla.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompok2.scarla.R

@Composable
fun ReportAccountDialog(
    targetName: String,
    onDismiss: () -> Unit,
    onReportSubmitted: (alsoBlock: Boolean) -> Unit
) {
    var alsoBlock by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFFFF7C6),
        shape = RoundedCornerShape(20.dp),
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Laporkan ke SCARLA",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFF1A1A1A)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Jika kamu merasa akun ini melakukan sesuatu yang tidak benar, seperti:",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        color = Color(0xFF2B2B2B)
                    )
                )

                Bullet("Mengirim pesan yang tidak sopan atau mengganggu")
                Bullet("Menyebarkan berita bohong (hoax)")
                Bullet("Melakukan penipuan atau berpura-pura jadi orang lain")
                Bullet("Mengunggah konten yang tidak pantas atau menyinggung")

                Spacer(modifier = Modifier.height(8.dp))

                // Dummy checkbox "blokir"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF1A1A1A), RoundedCornerShape(14.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Checkbox(
                        checked = alsoBlock,
                        onCheckedChange = { alsoBlock = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFFFFC300),
                            uncheckedColor = Color(0xFF1A1A1A),
                            checkmarkColor = Color(0xFF1A1A1A)
                        )
                    )
                    Column {
                        Text(
                            text = "Blokir $targetName",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_bold)),
                                fontWeight = FontWeight(700),
                                color = Color(0xFF1A1A1A)
                            )
                        )
                        Text(
                            text = "Pengguna ini tidak bisa mengirim pesan kepada Anda",
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                color = Color(0xFF2B2B2B)
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onReportSubmitted(alsoBlock) }) {
                Text(
                    text = "Laporkan",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFF1A1A1A)
                    )
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Batal",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        color = Color(0xFF1A1A1A)
                    )
                )
            }
        }
    )
}

@Composable
fun ReportThanksDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFFFF7C6),
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = "Terima Kasih Sudah Melapor",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF1A1A1A)
                )
            )
        },
        text = {
            Text(
                text = "Laporanmu sudah kami terima dan akan segera kami tinjau. " +
                    "Kami sangat menghargai bantuanmu dalam menjaga lingkungan aplikasi agar tetap aman " +
                    "dan nyaman untuk semua pengguna.",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    color = Color(0xFF2B2B2B)
                )
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Tutup",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFF1A1A1A)
                    )
                )
            }
        }
    )
}

@Composable
private fun Bullet(text: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier
                .padding(top = 7.dp)
                .size(4.dp)
                .background(Color(0xFF1A1A1A), RoundedCornerShape(10.dp))
        )
        Text(
            text = text,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                color = Color(0xFF2B2B2B)
            )
        )
    }
}