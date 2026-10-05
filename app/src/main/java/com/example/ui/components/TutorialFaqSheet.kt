package com.example.ui.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Swipe
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TranslationLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialFaqSheet(
    sheetState: SheetState,
    language: TranslationLanguage,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Help,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Column {
                        Text(
                            text = if (language == TranslationLanguage.ENGLISH) "Tutorial & FAQ" else "Panduan & Tanya Jawab",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (language == TranslationLanguage.ENGLISH) "Learn how to get the most out of Juz Amma Swipe" else "Pelajari cara memaksimalkan Juz Amma Swipe",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Feature 1: Swipe & Chevrons Navigation
                item {
                    TutorialCard(
                        icon = Icons.Default.Swipe,
                        title = if (language == TranslationLanguage.ENGLISH) "1. Seamless Swiping & Tactile Chevrons" else "1. Navigasi Geser & Tombol Panah",
                        description = if (language == TranslationLanguage.ENGLISH) {
                            "• Swipe horizontally anywhere on screen to flip between Ayahs smoothly.\n" +
                            "• Tap the tactile chevron arrows (‹ and ›) on either side of the card to jump immediately to the previous or next Ayah.\n" +
                            "• Audio recitations automatically play as you swipe so you can listen along seamlessly."
                        } else {
                            "• Geser (swipe) layar secara horizontal untuk berpindah antar ayat dengan lancar.\n" +
                            "• Ketuk tombol panah (‹ dan ›) di sisi kartu untuk langsung melompat ke ayat sebelumnya atau berikutnya.\n" +
                            "• Audio murottal otomatis berputar saat Anda menggeser ayat agar bisa langsung disimak."
                        }
                    )
                }

                // Feature 2: Memorization Practice
                item {
                    TutorialCard(
                        icon = Icons.Default.Psychology,
                        title = if (language == TranslationLanguage.ENGLISH) "2. Memorization Practice (Tahfidz)" else "2. Latihan Hafalan (Tahfidz)",
                        description = if (language == TranslationLanguage.ENGLISH) {
                            "• Enable 'Memorization Mode' from the top menu to hide verse text.\n" +
                            "• Recite the Ayah from memory, then tap the card to reveal and verify your recitation.\n" +
                            "• Tap the verse card at any time to instantly replay audio from the beginning for repeated listening and retention."
                        } else {
                            "• Aktifkan 'Mode Hafalan' dari menu atas untuk menyembunyikan teks ayat.\n" +
                            "• Lafalkan ayat dari ingatan, lalu ketuk kartu untuk membuka dan mencocokkan hafalan Anda.\n" +
                            "• Ketuk kartu ayat kapan saja untuk mengulang murottal dari awal demi pengulangan hafalan."
                        }
                    )
                }

                // Feature 3: Audio Recitation & Offline
                item {
                    TutorialCard(
                        icon = Icons.Default.CloudDownload,
                        title = if (language == TranslationLanguage.ENGLISH) "3. Audio Recitation & Offline Download" else "3. Murottal Audio & Unduh Offline",
                        description = if (language == TranslationLanguage.ENGLISH) {
                            "• High quality recitation by Sheikh Mishary Rashid Alafasy.\n" +
                            "• The audio panel at the bottom can be collapsed or expanded by tapping the pull-tab.\n" +
                            "• Tap 'Download Surah' inside the audio bar to save all verses of the Surah for complete offline recitation with no internet required."
                        } else {
                            "• Lantunan murottal jernih berkualitas tinggi oleh Syaikh Mishary Rashid Alafasy.\n" +
                            "• Panel audio di bagian bawah dapat dibuka atau ditutup dengan mengetuk pegangan panel.\n" +
                            "• Ketuk 'Unduh Surah' pada panel audio untuk menyimpan seluruh ayat Surah agar bisa didengarkan tanpa koneksi internet."
                        }
                    )
                }

                // Feature 4: Tajweed Color Coding
                item {
                    TutorialCard(
                        icon = Icons.Default.ColorLens,
                        title = if (language == TranslationLanguage.ENGLISH) "4. Dynamic Tajweed Color System" else "4. Sistem Pewarnaan Tajwid",
                        description = if (language == TranslationLanguage.ENGLISH) {
                            "• Red: Madd (Elongation 2, 4, 5, or 6 harakat)\n" +
                            "• Green: Ghunnah & Ikhfa (Nasalization & concealment)\n" +
                            "• Blue: Qalqalah (Echoing bounce on Qaf, Taa, Baa, Jeem, Dal)\n" +
                            "• Purple: Idgham (Merging of letters)\n" +
                            "• Orange: Iqlab (Converting Nun Sakinah/Tanween to Meem)\n" +
                            "• Grey: Hamzat Wasl (Connecting hamzah)"
                        } else {
                            "• Merah: Mad (Panjang bacaan 2, 4, 5, atau 6 harakat)\n" +
                            "• Hijau: Ghunnah & Ikhfa (Dengung dan samar)\n" +
                            "• Biru: Qalqalah (Pantulan huruf Qaf, Tha, Ba, Jim, Dal)\n" +
                            "• Ungu: Idgham (Meleburkan huruf)\n" +
                            "• Oranye: Iqlab (Mengubah nun mati/tanwin menjadi mim)\n" +
                            "• Abu-abu: Hamzat Wasl (Hamzah penyambung)"
                        }
                    )
                }

                // Section 5: FAQ
                item {
                    Text(
                        text = if (language == TranslationLanguage.ENGLISH) "Frequently Asked Questions" else "Pertanyaan yang Sering Diajukan",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }

                item {
                    FaqItem(
                        question = if (language == TranslationLanguage.ENGLISH) {
                            "Can I read the Quran completely offline?"
                        } else {
                            "Apakah saya bisa membaca Al-Qur'an secara offline?"
                        },
                        answer = if (language == TranslationLanguage.ENGLISH) {
                            "Yes! All 37 Surahs of Juz Amma with authentic Uthmani Arabic script, phonetics, and Indonesian & English translations work 100% offline. Audio recitations can also be cached offline per Surah."
                        } else {
                            "Ya! Seluruh 37 Surah Juz Amma dengan teks Arab Utsmani, transliterasi Latin, dan terjemahan Indonesia & Inggris berfungsi 100% offline. Audio murottal juga dapat diunduh per Surah."
                        }
                    )
                }

                item {
                    FaqItem(
                        question = if (language == TranslationLanguage.ENGLISH) {
                            "How do I repeat an Ayah continuously to memorize it?"
                        } else {
                            "Bagaimana cara mengulang satu ayat terus-menerus?"
                        },
                        answer = if (language == TranslationLanguage.ENGLISH) {
                            "Expand the audio controls bar at the bottom and turn ON 'Loop Verse'. The current Ayah will repeat indefinitely until you switch verses or turn loop off."
                        } else {
                            "Buka panel kontrol audio di bawah dan aktifkan tombol 'Ulangi Ayat'. Ayat saat ini akan terus diulang otomatis sampai Anda mematikan fitur tersebut."
                        }
                    )
                }

                item {
                    FaqItem(
                        question = if (language == TranslationLanguage.ENGLISH) {
                            "How do I switch the language between English and Indonesian?"
                        } else {
                            "Bagaimana cara mengganti bahasa antara Inggris dan Indonesia?"
                        },
                        answer = if (language == TranslationLanguage.ENGLISH) {
                            "Tap the language pill (EN | ID) in the header of the main Surah Selection screen, or open the top bar in the recitation view to instantly toggle the entire app language."
                        } else {
                            "Ketuk tombol bahasa (EN | ID) di header layar Daftar Surah atau di bilah menu atas pada tampilan membaca untuk langsung mengubah bahasa seluruh aplikasi."
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun TutorialCard(
    icon: ImageVector,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    modifier = Modifier.size(34.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun FaqItem(
    question: String,
    answer: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.QuestionAnswer,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .size(18.dp)
                        .padding(top = 2.dp)
                )
                Text(
                    text = question,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = answer,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )
        }
    }
}
