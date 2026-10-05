package com.example.ui

import android.app.Application
import android.content.res.Configuration
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.AudioPlayerManager
import com.example.data.JuzAmmaRepository
import com.example.data.TranslationLanguage
import com.example.data.UserPreferencesManager
import com.example.model.Surah
import com.example.model.TajweedRuleType
import com.example.model.Verse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class JuzAmmaViewModel(application: Application) : AndroidViewModel(application) {

    val audioPlayer = AudioPlayerManager(application)
    val preferencesManager = UserPreferencesManager(application)

    val surahs: List<Surah> = JuzAmmaRepository.surahs
    val allVerses: List<Verse> = JuzAmmaRepository.allVerses

    // System dark mode detection - follows user's system phone setting on startup
    private val systemIsDarkMode: Boolean = run {
        val nightModeFlags = application.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }

    private val _isDarkMode = MutableStateFlow(systemIsDarkMode)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    // Persistent Language preference
    private val _translationLanguage = MutableStateFlow(
        preferencesManager.getSavedTranslationLanguage() ?: TranslationLanguage.INDONESIAN
    )
    val translationLanguage: StateFlow<TranslationLanguage> = _translationLanguage.asStateFlow()

    // Current active Surah and Verse within that Surah
    private val initialSurahId = preferencesManager.getLastReadSurahId().coerceIn(78, 114)
    private val _currentSurahId = MutableStateFlow(initialSurahId)
    val currentSurahId: StateFlow<Int> = _currentSurahId.asStateFlow()

    // Verses of currently selected Surah
    val currentSurahVerses: StateFlow<List<Verse>> = _currentSurahId.combine(
        MutableStateFlow(allVerses)
    ) { sId, verses ->
        val list = verses.filter { it.surahNumber == sId }
        if (list.isNotEmpty()) list else verses.take(40)
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        allVerses.filter { it.surahNumber == initialSurahId }
    )

    // Current verse index inside the current Surah (0-based, e.g. 0..39 for An-Naba)
    private val initialVerseNum = preferencesManager.getLastReadVerseNumber()
    private val initialSurahVerses = allVerses.filter { it.surahNumber == initialSurahId }
    private val initialSafeIndex = (initialVerseNum - 1).coerceIn(0, (initialSurahVerses.size - 1).coerceAtLeast(0))
    private val initialVerse = initialSurahVerses.getOrElse(initialSafeIndex) { allVerses.first() }

    private val _currentVerseIndexInSurah = MutableStateFlow(initialSafeIndex)
    val currentVerseIndexInSurah: StateFlow<Int> = _currentVerseIndexInSurah.asStateFlow()

    // Global verse index for backward compatibility and index-based lookups
    val currentVerse: StateFlow<Verse> = combine(
        currentSurahVerses,
        _currentVerseIndexInSurah
    ) { verses, indexInSurah ->
        val safeIndex = indexInSurah.coerceIn(0, (verses.size - 1).coerceAtLeast(0))
        verses.getOrElse(safeIndex) { allVerses.first() }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        initialVerse
    )

    val currentVerseIndex: StateFlow<Int> = currentVerse.map { it.globalIndex }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        preferencesManager.getLastReadGlobalIndex()
    )

    val currentSurah: StateFlow<Surah?> = _currentSurahId.combine(
        MutableStateFlow(surahs)
    ) { sId, sList ->
        sList.find { it.id == sId } ?: sList.firstOrNull()
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        surahs.find { it.id == initialSurahId } ?: surahs.first()
    )

    private val _isTajweedEnabled = MutableStateFlow(false)
    val isTajweedEnabled: StateFlow<Boolean> = _isTajweedEnabled.asStateFlow()

    private val _showTransliteration = MutableStateFlow(true)
    val showTransliteration: StateFlow<Boolean> = _showTransliteration.asStateFlow()

    private val _showTranslation = MutableStateFlow(true)
    val showTranslation: StateFlow<Boolean> = _showTranslation.asStateFlow()

    private val _isMemorizationMode = MutableStateFlow(false)
    val isMemorizationMode: StateFlow<Boolean> = _isMemorizationMode.asStateFlow()

    private val _bookmarkedVerseIndexes = MutableStateFlow<Set<Int>>(
        preferencesManager.getBookmarkedIndexes()
    )
    val bookmarkedVerseIndexes: StateFlow<Set<Int>> = _bookmarkedVerseIndexes.asStateFlow()

    val bookmarkedVerses: StateFlow<List<Verse>> = _bookmarkedVerseIndexes.map { set ->
        allVerses.filter { set.contains(it.globalIndex) }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    private val _selectedTajweedRuleFilter = MutableStateFlow<TajweedRuleType?>(null)
    val selectedTajweedRuleFilter: StateFlow<TajweedRuleType?> = _selectedTajweedRuleFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        // Wire up auto-next callback from audio player strictly within current Surah
        audioPlayer.onVerseCompletedCallback = {
            viewModelScope.launch {
                val versesInSurah = currentSurahVerses.value
                val nextIndexInSurah = _currentVerseIndexInSurah.value + 1
                if (nextIndexInSurah < versesInSurah.size) {
                    onVerseInSurahSelected(nextIndexInSurah, autoStartAudio = true)
                }
                // If it is the last verse of the Surah, we stay on this verse and stop recitation
            }
        }
    }

    /**
     * Select a verse within the currently active Surah
     */
    fun onVerseInSurahSelected(indexInSurah: Int, autoStartAudio: Boolean = true) {
        val versesInSurah = currentSurahVerses.value
        if (indexInSurah in versesInSurah.indices) {
            val isNewIndex = indexInSurah != _currentVerseIndexInSurah.value
            _currentVerseIndexInSurah.value = indexInSurah
            val selectedVerse = versesInSurah[indexInSurah]

            // Persist last read location
            preferencesManager.saveLastRead(
                surahId = selectedVerse.surahNumber,
                verseNumber = selectedVerse.verseNumber,
                globalIndex = selectedVerse.globalIndex
            )

            if (isNewIndex && autoStartAudio && audioPlayer.isAutoPlayOnSwipe.value) {
                audioPlayer.playVerse(selectedVerse, autoStart = true)
            }
        }
    }

    fun replayCurrentVerse() {
        audioPlayer.replayVerse(currentVerse.value)
    }

    fun replayVerseAtIndexInSurah(indexInSurah: Int) {
        val versesInSurah = currentSurahVerses.value
        if (indexInSurah in versesInSurah.indices) {
            if (_currentVerseIndexInSurah.value != indexInSurah) {
                _currentVerseIndexInSurah.value = indexInSurah
                val selectedVerse = versesInSurah[indexInSurah]
                preferencesManager.saveLastRead(
                    surahId = selectedVerse.surahNumber,
                    verseNumber = selectedVerse.verseNumber,
                    globalIndex = selectedVerse.globalIndex
                )
                audioPlayer.playVerse(selectedVerse, autoStart = true)
            } else {
                audioPlayer.replayVerse(versesInSurah[indexInSurah])
            }
        }
    }

    fun downloadCurrentSurahOffline() {
        val versesToDownload = currentSurahVerses.value
        audioPlayer.downloadVersesForOffline(versesToDownload)
    }

    fun downloadAllJuzAmmaOffline() {
        audioPlayer.downloadVersesForOffline(allVerses)
    }

    fun jumpToSurah(surahNumber: Int, verseNumber: Int = 1) {
        _currentSurahId.value = surahNumber
        val versesInSurah = allVerses.filter { it.surahNumber == surahNumber }
        val targetIndex = (verseNumber - 1).coerceIn(0, (versesInSurah.size - 1).coerceAtLeast(0))
        _currentVerseIndexInSurah.value = targetIndex
        
        val selectedVerse = versesInSurah.getOrNull(targetIndex) ?: allVerses.first { it.surahNumber == surahNumber }
        preferencesManager.saveLastRead(
            surahId = selectedVerse.surahNumber,
            verseNumber = selectedVerse.verseNumber,
            globalIndex = selectedVerse.globalIndex
        )

        if (audioPlayer.isAutoPlayOnSwipe.value) {
            audioPlayer.playVerse(selectedVerse, autoStart = true)
        }
    }

    fun jumpToVerseInSurah(surahNumber: Int, verseNumber: Int) {
        jumpToSurah(surahNumber, verseNumber)
    }

    fun jumpToGlobalVerse(globalIndex: Int) {
        val targetVerse = allVerses.getOrNull(globalIndex) ?: return
        jumpToSurah(targetVerse.surahNumber, targetVerse.verseNumber)
    }

    fun resumeLastRead() {
        val savedSurahId = preferencesManager.getLastReadSurahId().coerceIn(78, 114)
        val savedVerseNum = preferencesManager.getLastReadVerseNumber()
        jumpToSurah(savedSurahId, savedVerseNum)
    }

    fun replayVerse(verse: Verse) {
        audioPlayer.replayVerse(verse)
    }

    fun replayVerseAtIndex(globalIndex: Int) {
        val targetVerse = allVerses.getOrNull(globalIndex) ?: return
        audioPlayer.replayVerse(targetVerse)
    }

    fun toggleDarkMode() {
        val newDark = !_isDarkMode.value
        _isDarkMode.value = newDark
        preferencesManager.saveDarkMode(newDark)
    }

    fun toggleTajweed() {
        _isTajweedEnabled.value = !_isTajweedEnabled.value
    }

    fun toggleTransliteration() {
        _showTransliteration.value = !_showTransliteration.value
    }

    fun toggleTranslation() {
        _showTranslation.value = !_showTranslation.value
    }

    fun toggleTranslationLanguage() {
        val newLang = if (_translationLanguage.value == TranslationLanguage.INDONESIAN) {
            TranslationLanguage.ENGLISH
        } else {
            TranslationLanguage.INDONESIAN
        }
        _translationLanguage.value = newLang
        preferencesManager.saveTranslationLanguage(newLang)
    }

    fun setTranslationLanguage(language: TranslationLanguage) {
        _translationLanguage.value = language
        preferencesManager.saveTranslationLanguage(language)
    }

    fun toggleMemorizationMode() {
        _isMemorizationMode.value = !_isMemorizationMode.value
    }

    fun toggleBookmark(globalVerseIndex: Int) {
        val current = _bookmarkedVerseIndexes.value.toMutableSet()
        if (current.contains(globalVerseIndex)) {
            current.remove(globalVerseIndex)
        } else {
            current.add(globalVerseIndex)
        }
        _bookmarkedVerseIndexes.value = current
        preferencesManager.saveBookmarkedIndexes(current)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectTajweedRuleFilter(rule: TajweedRuleType?) {
        _selectedTajweedRuleFilter.value = rule
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.clear()
    }
}
