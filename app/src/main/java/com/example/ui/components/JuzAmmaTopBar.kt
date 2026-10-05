package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TranslationLanguage
import com.example.data.getLocalizedName
import com.example.model.Surah
import com.example.ui.localization.AppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JuzAmmaTopBar(
    currentSurah: Surah?,
    isDarkMode: Boolean,
    isTajweedEnabled: Boolean,
    isMemorizationMode: Boolean,
    showTransliteration: Boolean,
    showTranslation: Boolean,
    translationLanguage: TranslationLanguage,
    onBackToSurahList: () -> Unit,
    onOpenSurahIndex: () -> Unit,
    onOpenTajweedGuide: () -> Unit,
    onOpenTutorialFaq: () -> Unit,
    onOpenBookmarks: () -> Unit = {},
    onToggleDarkMode: () -> Unit,
    onToggleTajweed: () -> Unit,
    onToggleMemorization: () -> Unit,
    onToggleTransliteration: () -> Unit,
    onToggleTranslation: () -> Unit,
    onToggleTranslationLanguage: () -> Unit,
    onDownloadCurrentSurah: () -> Unit,
    onDownloadAllJuzAmma: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMoreMenu by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = onBackToSurahList,
                modifier = Modifier.testTag("back_to_surah_list_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = AppStrings.backToSurahs(translationLanguage),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        title = {
            // Surah Quick Selector Button with compact single-line styling
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onOpenSurahIndex() }
                    .testTag("surah_selector_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ListAlt,
                        contentDescription = "Select Surah",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = currentSurah?.getLocalizedName(translationLanguage) ?: "Surah Index",
                        style = MaterialTheme.typography.titleSmall.copy(fontSize = 11.5.sp),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.widthIn(max = 110.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Expand",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        },
        actions = {
            // Language Pill (EN | ID) Toggle
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = onToggleTranslationLanguage)
                    .testTag("topbar_language_toggle")
            ) {
                Text(
                    text = if (translationLanguage == TranslationLanguage.ENGLISH) "EN" else "ID",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                )
            }

            // Dark / Light Theme Toggle
            IconButton(
                onClick = onToggleDarkMode,
                modifier = Modifier.testTag("theme_toggle_button")
            ) {
                Icon(
                    imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = if (isDarkMode) "Light Theme" else "Dark Theme",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            // Toggle Tajweed Color Highlighting
            IconButton(
                onClick = onToggleTajweed,
                modifier = Modifier.testTag("tajweed_toggle_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ColorLens,
                    contentDescription = "Toggle Tajweed",
                    tint = if (isTajweedEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                )
            }

            // Memorization Mode
            IconButton(
                onClick = onToggleMemorization,
                modifier = Modifier.testTag("memorization_toggle_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = "Memorization Practice Mode",
                    tint = if (isMemorizationMode) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                )
            }

            // Overflow Menu
            Box {
                IconButton(
                    onClick = { showMoreMenu = true },
                    modifier = Modifier.testTag("overflow_menu_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More View Options"
                    )
                }

                DropdownMenu(
                    expanded = showMoreMenu,
                    onDismissRequest = { showMoreMenu = false }
                ) {
                    // Saved Bookmarks
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        },
                        text = {
                            Text(
                                text = if (translationLanguage == TranslationLanguage.INDONESIAN) "Ayat Ditandai" else "Saved Bookmarks",
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        onClick = {
                            onOpenBookmarks()
                            showMoreMenu = false
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    // Tutorial & FAQ
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Help,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        text = {
                            Text(
                                text = AppStrings.guideAndFaq(translationLanguage),
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        onClick = {
                            onOpenTutorialFaq()
                            showMoreMenu = false
                        }
                    )

                    // Tajweed Guide
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.HelpOutline,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        },
                        text = {
                            Text(AppStrings.tajweedColorGuide(translationLanguage))
                        },
                        onClick = {
                            onOpenTajweedGuide()
                            showMoreMenu = false
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    // App-Wide Language Toggle
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Translate,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        text = {
                            Column {
                                Text(
                                    text = AppStrings.languageLabel(translationLanguage),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = AppStrings.tapToSwitchLanguage(translationLanguage),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        onClick = {
                            onToggleTranslationLanguage()
                            showMoreMenu = false
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    DropdownMenuItem(
                        text = {
                            Text(
                                if (showTransliteration) {
                                    if (translationLanguage == TranslationLanguage.ENGLISH) "Hide Transliteration" else "Sembunyikan Transliterasi"
                                } else {
                                    if (translationLanguage == TranslationLanguage.ENGLISH) "Show Transliteration" else "Tampilkan Transliterasi"
                                }
                            )
                        },
                        onClick = {
                            onToggleTransliteration()
                            showMoreMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                if (showTranslation) {
                                    if (translationLanguage == TranslationLanguage.ENGLISH) "Hide Translation" else "Sembunyikan Terjemahan"
                                } else {
                                    if (translationLanguage == TranslationLanguage.ENGLISH) "Show Translation" else "Tampilkan Terjemahan"
                                }
                            )
                        },
                        onClick = {
                            onToggleTranslation()
                            showMoreMenu = false
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CloudDownload,
                                contentDescription = "Download Current Surah",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        },
                        text = {
                            Text(
                                if (translationLanguage == TranslationLanguage.ENGLISH)
                                    "Download Surah ${currentSurah?.nameTransliteration ?: ""} (Offline)"
                                else
                                    "Unduh Surah ${currentSurah?.nameTransliteration ?: ""} (Offline)"
                            )
                        },
                        onClick = {
                            onDownloadCurrentSurah()
                            showMoreMenu = false
                        }
                    )

                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CloudDownload,
                                contentDescription = "Download All Juz 30",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        text = {
                            Text(
                                if (translationLanguage == TranslationLanguage.ENGLISH)
                                    "Download All Juz 30 (Offline)"
                                else
                                    "Unduh Seluruh Juz 30 (Offline)"
                            )
                        },
                        onClick = {
                            onDownloadAllJuzAmma()
                            showMoreMenu = false
                        }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}
