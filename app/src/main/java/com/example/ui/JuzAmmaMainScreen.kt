package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.TajweedRuleType
import com.example.ui.components.AudioControlBar
import com.example.ui.components.BookmarksSheet
import com.example.ui.components.JuzAmmaTopBar
import com.example.ui.components.SurahIndexSheet
import com.example.ui.components.TajweedGuideSheet
import com.example.ui.components.TutorialFaqSheet
import com.example.ui.components.VersePageContent
import com.example.ui.components.VerseSelectionSheet
import com.example.ui.screens.SurahSelectionScreen
import com.example.ui.theme.JuzAmmaTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

enum class ScreenDestination {
    SURAH_SELECTION,
    VERSE_SWIPE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JuzAmmaMainScreen(
    viewModel: JuzAmmaViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    var currentScreen by remember { mutableStateOf(ScreenDestination.SURAH_SELECTION) }

    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val currentVerseIndex by viewModel.currentVerseIndex.collectAsState()
    val currentVerseIndexInSurah by viewModel.currentVerseIndexInSurah.collectAsState()
    val currentSurahVerses by viewModel.currentSurahVerses.collectAsState()
    val currentVerse by viewModel.currentVerse.collectAsState()
    val currentSurah by viewModel.currentSurah.collectAsState()

    val isTajweedEnabled by viewModel.isTajweedEnabled.collectAsState()
    val showTransliteration by viewModel.showTransliteration.collectAsState()
    val showTranslation by viewModel.showTranslation.collectAsState()
    val translationLanguage by viewModel.translationLanguage.collectAsState()
    val isMemorizationMode by viewModel.isMemorizationMode.collectAsState()
    val bookmarkedIndexes by viewModel.bookmarkedVerseIndexes.collectAsState()
    val bookmarkedVerses by viewModel.bookmarkedVerses.collectAsState()

    // Audio player states
    val isPlaying by viewModel.audioPlayer.isPlaying.collectAsState()
    val isLoading by viewModel.audioPlayer.isLoading.collectAsState()
    val audioProgress by viewModel.audioPlayer.progress.collectAsState()
    val audioSpeed by viewModel.audioPlayer.speed.collectAsState()
    val isAutoPlayOnSwipe by viewModel.audioPlayer.isAutoPlayOnSwipe.collectAsState()
    val isAutoNextEnabled by viewModel.audioPlayer.isAutoNextEnabled.collectAsState()
    val isLoopingVerse by viewModel.audioPlayer.isLoopingVerse.collectAsState()
    val errorMessage by viewModel.audioPlayer.errorMessage.collectAsState()
    val offlineStatusMessage by viewModel.audioPlayer.offlineDownloadStatus.collectAsState()
    val isDownloadingOffline by viewModel.audioPlayer.isDownloadingOffline.collectAsState()

    val isCurrentVerseCached = remember(currentVerse, isPlaying, offlineStatusMessage) {
        viewModel.audioPlayer.isVerseCached(currentVerse)
    }

    // Bottom sheets state
    var showSurahIndexSheet by remember { mutableStateOf(false) }
    var showTajweedGuideSheet by remember { mutableStateOf(false) }
    var showVersePickerSheet by remember { mutableStateOf(false) }
    var showTutorialFaqSheet by remember { mutableStateOf(false) }
    var showBookmarksSheet by remember { mutableStateOf(false) }
    var selectedTajweedRuleForGuide by remember { mutableStateOf<TajweedRuleType?>(null) }

    val surahSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val tajweedSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val versePickerSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val tutorialFaqSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val bookmarksSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Pager state for verse-by-verse swiping strictly within the current Surah
    val surahVersesCount = currentSurahVerses.size
    val pagerState = rememberPagerState(
        initialPage = currentVerseIndexInSurah.coerceIn(0, (surahVersesCount - 1).coerceAtLeast(0)),
        pageCount = { surahVersesCount }
    )

    // Intercept back button when in Verse Swipe mode to return to Surah Selection
    BackHandler(enabled = currentScreen == ScreenDestination.VERSE_SWIPE) {
        currentScreen = ScreenDestination.SURAH_SELECTION
    }

    // Sync Pager swipe with ViewModel & Audio Recitation auto-play
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .distinctUntilChanged()
            .collect { page ->
                // Only auto-play if we are actively in the verse swipe screen
                if (currentScreen == ScreenDestination.VERSE_SWIPE) {
                    viewModel.onVerseInSurahSelected(page, autoStartAudio = true)
                }
            }
    }

    // Sync ViewModel index changes to Pager
    LaunchedEffect(currentVerseIndexInSurah, currentSurahVerses) {
        val safeIndex = currentVerseIndexInSurah.coerceIn(0, (currentSurahVerses.size - 1).coerceAtLeast(0))
        if (pagerState.currentPage != safeIndex) {
            val pageDistance = kotlin.math.abs(pagerState.currentPage - safeIndex)
            if (pageDistance > 1) {
                pagerState.scrollToPage(safeIndex)
            } else {
                pagerState.animateScrollToPage(safeIndex)
            }
        }
    }

    JuzAmmaTheme(darkTheme = isDarkMode) {
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "screen_transition"
        ) { screen ->
            when (screen) {
                ScreenDestination.SURAH_SELECTION -> {
                    SurahSelectionScreen(
                        surahs = viewModel.surahs,
                        lastReadVerse = currentVerse,
                        isDarkMode = isDarkMode,
                        language = translationLanguage,
                        bookmarkedVerses = bookmarkedVerses,
                        onSurahSelected = { surahId ->
                            viewModel.jumpToSurah(surahId)
                            currentScreen = ScreenDestination.VERSE_SWIPE
                        },
                        onResumeLastRead = {
                            viewModel.resumeLastRead()
                            currentScreen = ScreenDestination.VERSE_SWIPE
                        },
                        onBookmarkSelected = { surahNumber, verseNumber ->
                            viewModel.jumpToVerseInSurah(surahNumber, verseNumber)
                            currentScreen = ScreenDestination.VERSE_SWIPE
                        },
                        onOpenBookmarksSheet = { showBookmarksSheet = true },
                        onToggleDarkMode = { viewModel.toggleDarkMode() },
                        onToggleLanguage = { viewModel.toggleTranslationLanguage() },
                        onOpenTutorialFaq = { showTutorialFaqSheet = true }
                    )
                }

                ScreenDestination.VERSE_SWIPE -> {
                    Scaffold(
                        topBar = {
                            JuzAmmaTopBar(
                                currentSurah = currentSurah,
                                isDarkMode = isDarkMode,
                                isTajweedEnabled = isTajweedEnabled,
                                isMemorizationMode = isMemorizationMode,
                                showTransliteration = showTransliteration,
                                showTranslation = showTranslation,
                                translationLanguage = translationLanguage,
                                onBackToSurahList = {
                                    currentScreen = ScreenDestination.SURAH_SELECTION
                                },
                                onOpenSurahIndex = { showSurahIndexSheet = true },
                                onOpenTajweedGuide = {
                                    selectedTajweedRuleForGuide = null
                                    showTajweedGuideSheet = true
                                },
                                onOpenTutorialFaq = {
                                    showTutorialFaqSheet = true
                                },
                                onOpenBookmarks = {
                                    showBookmarksSheet = true
                                },
                                onToggleDarkMode = { viewModel.toggleDarkMode() },
                                onToggleTajweed = { viewModel.toggleTajweed() },
                                onToggleMemorization = { viewModel.toggleMemorizationMode() },
                                onToggleTransliteration = { viewModel.toggleTransliteration() },
                                onToggleTranslation = { viewModel.toggleTranslation() },
                                onToggleTranslationLanguage = { viewModel.toggleTranslationLanguage() },
                                onDownloadCurrentSurah = { viewModel.downloadCurrentSurahOffline() },
                                onDownloadAllJuzAmma = { viewModel.downloadAllJuzAmmaOffline() }
                            )
                        },
                        bottomBar = {
                            AudioControlBar(
                                currentVerse = currentVerse,
                                isPlaying = isPlaying,
                                isLoading = isLoading,
                                progress = audioProgress,
                                speed = audioSpeed,
                                isAutoPlayOnSwipe = isAutoPlayOnSwipe,
                                isAutoNextEnabled = isAutoNextEnabled,
                                isLoopingVerse = isLoopingVerse,
                                isOfflineCached = isCurrentVerseCached,
                                offlineStatusMessage = offlineStatusMessage,
                                isDownloadingOffline = isDownloadingOffline,
                                errorMessage = errorMessage,
                                language = translationLanguage,
                                onPlayPauseClicked = { viewModel.audioPlayer.togglePlayPause() },
                                onPrevClicked = {
                                    if (currentVerseIndexInSurah > 0) {
                                        viewModel.onVerseInSurahSelected(currentVerseIndexInSurah - 1, autoStartAudio = true)
                                    }
                                },
                                onNextClicked = {
                                    if (currentVerseIndexInSurah < surahVersesCount - 1) {
                                        viewModel.onVerseInSurahSelected(currentVerseIndexInSurah + 1, autoStartAudio = true)
                                    }
                                },
                                onSeekTo = { fraction -> viewModel.audioPlayer.seekTo(fraction) },
                                onSpeedSelected = { newSpeed -> viewModel.audioPlayer.setPlaybackSpeed(newSpeed) },
                                onToggleAutoPlayOnSwipe = { viewModel.audioPlayer.toggleAutoPlayOnSwipe() },
                                onToggleAutoNext = { viewModel.audioPlayer.toggleAutoNext() },
                                onToggleLoopingVerse = { viewModel.audioPlayer.toggleLoopingVerse() },
                                onDownloadSurahOffline = { viewModel.downloadCurrentSurahOffline() }
                            )
                        },
                        containerColor = MaterialTheme.colorScheme.surface
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            // Horizontal Verse Pager ("each verse acting as a page") - isolated strictly to current Surah
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxSize()
                            ) { pageIndex ->
                                val verse = currentSurahVerses.getOrNull(pageIndex) ?: currentVerse
                                val surah = currentSurah
                                val isBookmarked = bookmarkedIndexes.contains(verse.globalIndex)

                                VersePageContent(
                                    verse = verse,
                                    surah = surah,
                                    isTajweedEnabled = isTajweedEnabled,
                                    showTransliteration = showTransliteration,
                                    showTranslation = showTranslation,
                                    translationLanguage = translationLanguage,
                                    isMemorizationMode = isMemorizationMode,
                                    isBookmarked = isBookmarked,
                                    isPlaying = isPlaying && currentVerse.verseNumber == verse.verseNumber,
                                    hasPrevVerse = pageIndex > 0,
                                    hasNextVerse = pageIndex < surahVersesCount - 1,
                                    onPrevVerseClicked = {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(pageIndex - 1)
                                        }
                                    },
                                    onNextVerseClicked = {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(pageIndex + 1)
                                        }
                                    },
                                    onPlayPauseClicked = {
                                        if (currentVerse.verseNumber == verse.verseNumber) {
                                            viewModel.audioPlayer.togglePlayPause()
                                        } else {
                                            viewModel.onVerseInSurahSelected(pageIndex, autoStartAudio = true)
                                        }
                                    },
                                    onReplayClicked = {
                                        viewModel.replayVerseAtIndex(verse.globalIndex)
                                    },
                                    onBookmarkToggled = { viewModel.toggleBookmark(verse.globalIndex) },
                                    onRuleClicked = { rule ->
                                        selectedTajweedRuleForGuide = rule
                                        showTajweedGuideSheet = true
                                    },
                                    onOpenVersePicker = {
                                        showVersePickerSheet = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- Shared Bottom Sheets ---

        // Saved Bookmarks Sheet
        if (showBookmarksSheet) {
            BookmarksSheet(
                sheetState = bookmarksSheetState,
                bookmarkedVerses = bookmarkedVerses,
                surahs = viewModel.surahs,
                language = translationLanguage,
                onVerseSelected = { surahNumber, verseNumber ->
                    viewModel.jumpToVerseInSurah(surahNumber, verseNumber)
                    showBookmarksSheet = false
                    currentScreen = ScreenDestination.VERSE_SWIPE
                },
                onRemoveBookmark = { globalIndex ->
                    viewModel.toggleBookmark(globalIndex)
                },
                onDismiss = { showBookmarksSheet = false }
            )
        }

        // Tutorial & FAQ Sheet
        if (showTutorialFaqSheet) {
            TutorialFaqSheet(
                sheetState = tutorialFaqSheetState,
                language = translationLanguage,
                onDismiss = { showTutorialFaqSheet = false }
            )
        }

        // Verse Quick Picker Sheet (for current Surah)
        if (showVersePickerSheet && currentSurah != null) {
            VerseSelectionSheet(
                sheetState = versePickerSheetState,
                surah = currentSurah!!,
                currentVerseNumber = currentVerse.verseNumber,
                surahVerses = currentSurahVerses,
                onVerseSelected = { selectedVerseNum ->
                    viewModel.jumpToVerseInSurah(currentSurah!!.id, selectedVerseNum)
                },
                onDownloadSurahOffline = {
                    viewModel.downloadCurrentSurahOffline()
                },
                onDismiss = { showVersePickerSheet = false }
            )
        }

        // Surah Index Sheet
        if (showSurahIndexSheet) {
            SurahIndexSheet(
                sheetState = surahSheetState,
                surahs = viewModel.surahs,
                selectedSurahNumber = currentVerse.surahNumber,
                onSurahSelected = { surahNumber ->
                    viewModel.jumpToSurah(surahNumber)
                },
                onDismiss = { showSurahIndexSheet = false },
                language = translationLanguage
            )
        }

        // Tajweed Rule Guide Sheet
        if (showTajweedGuideSheet) {
            TajweedGuideSheet(
                sheetState = tajweedSheetState,
                highlightedRule = selectedTajweedRuleForGuide,
                onDismiss = { showTajweedGuideSheet = false }
            )
        }
    }
}
