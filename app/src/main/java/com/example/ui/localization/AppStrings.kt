package com.example.ui.localization

import com.example.data.TranslationLanguage

object AppStrings {

    fun appTitle(): String = "Juz Amma Swipe"

    fun arabicSubtitle(): String = "جُزْء عَمَّ • ٱلْقُرْآن ٱلْكَرِيم"

    fun headerStats(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "37 Surahs • 564 Verses • Juz 30"
        TranslationLanguage.INDONESIAN -> "37 Surah • 564 Ayat • Juz 30"
    }

    fun searchPlaceholder(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Search Surah by name, Arabic, or number..."
        TranslationLanguage.INDONESIAN -> "Cari Surah dari nama, Arab, atau nomor..."
    }

    fun continueReciting(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Continue Reciting"
        TranslationLanguage.INDONESIAN -> "Lanjutkan Membaca"
    }

    fun lastRead(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Last Read"
        TranslationLanguage.INDONESIAN -> "Terakhir Dibaca"
    }

    fun resume(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Resume"
        TranslationLanguage.INDONESIAN -> "Lanjut"
    }

    fun allSurahsHeader(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "All Surahs (78 - 114)"
        TranslationLanguage.INDONESIAN -> "Daftar Surah (78 - 114)"
    }

    fun noSurahFound(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "No Surahs found matching your search."
        TranslationLanguage.INDONESIAN -> "Tidak ada Surah yang cocok dengan pencarian."
    }

    fun guideAndFaq(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Guide & FAQ"
        TranslationLanguage.INDONESIAN -> "Panduan & FAQ"
    }

    fun backToSurahs(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Surahs"
        TranslationLanguage.INDONESIAN -> "Daftar Surah"
    }

    fun ayah(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Ayah"
        TranslationLanguage.INDONESIAN -> "Ayat"
    }

    fun versesCount(count: Int, lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "$count Verses"
        TranslationLanguage.INDONESIAN -> "$count Ayat"
    }

    fun revelationMeccan(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Meccan"
        TranslationLanguage.INDONESIAN -> "Makkiyah"
    }

    fun revelationMedinan(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Medinan"
        TranslationLanguage.INDONESIAN -> "Madaniyah"
    }

    fun tapCardToRecite(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Tap card to recite"
        TranslationLanguage.INDONESIAN -> "Ketuk kartu untuk murottal"
    }

    fun playingTapToReplay(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Playing • Tap to Replay"
        TranslationLanguage.INDONESIAN -> "Memutar • Ketuk untuk Ulangi"
    }

    fun memorizationModeTitle(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Memorization Practice Mode"
        TranslationLanguage.INDONESIAN -> "Mode Latihan Hafalan"
    }

    fun memorizationModeDesc(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Recite verse from memory, then tap to reveal!"
        TranslationLanguage.INDONESIAN -> "Lafalkan ayat dari ingatan, lalu ketuk untuk melihat!"
    }

    fun phoneticReading(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Phonetic Reading"
        TranslationLanguage.INDONESIAN -> "Bacaan Latin / Transliterasi"
    }

    fun translationLabel(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "English Translation (Saheeh International)"
        TranslationLanguage.INDONESIAN -> "Terjemahan Bahasa Indonesia (Kemenag RI)"
    }

    fun tajweedRulesTitle(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Tajweed Rules in Verse (Tap to learn)"
        TranslationLanguage.INDONESIAN -> "Hukum Tajwid pada Ayat (Ketuk untuk pelajari)"
    }

    fun swipePrevAyah(ayahNumber: Int, lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "‹ Ayah $ayahNumber"
        TranslationLanguage.INDONESIAN -> "‹ Ayat $ayahNumber"
    }

    fun swipeNextAyah(ayahNumber: Int, lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Ayah $ayahNumber ›"
        TranslationLanguage.INDONESIAN -> "Ayat $ayahNumber ›"
    }

    fun swipeHint(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Swipe or tap chevrons to navigate Ayahs"
        TranslationLanguage.INDONESIAN -> "Geser atau ketuk panah untuk pindah ayat"
    }

    fun audioControls(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Audio Controls"
        TranslationLanguage.INDONESIAN -> "Kontrol Audio"
    }

    fun ayahPlaying(ayahNumber: Int, lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Ayah $ayahNumber • Playing (Tap to expand)"
        TranslationLanguage.INDONESIAN -> "Ayat $ayahNumber • Memutar (Ketuk untuk buka)"
    }

    fun ayahPaused(ayahNumber: Int, lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Ayah $ayahNumber • Paused (Tap to expand)"
        TranslationLanguage.INDONESIAN -> "Ayat $ayahNumber • Dijeda (Ketuk untuk buka)"
    }

    fun offlineReady(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Offline Ready"
        TranslationLanguage.INDONESIAN -> "Siap Offline"
    }

    fun downloadSurah(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Download Surah"
        TranslationLanguage.INDONESIAN -> "Unduh Surah"
    }

    fun downloading(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Downloading..."
        TranslationLanguage.INDONESIAN -> "Mengunduh..."
    }

    fun autoPlayOnSwipe(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Auto-Play"
        TranslationLanguage.INDONESIAN -> "Putar Otomatis"
    }

    fun autoNext(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Auto-Next"
        TranslationLanguage.INDONESIAN -> "Lanjut Ayat"
    }

    fun loopVerse(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Loop Verse"
        TranslationLanguage.INDONESIAN -> "Ulangi Ayat"
    }

    fun speed(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Speed"
        TranslationLanguage.INDONESIAN -> "Kecepatan"
    }

    fun reciterName(): String = "Mishary Rashid Alafasy"

    fun surahList(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Surah List"
        TranslationLanguage.INDONESIAN -> "Daftar Surah"
    }

    fun tajweedColorGuide(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Tajweed Color Guide"
        TranslationLanguage.INDONESIAN -> "Panduan Warna Tajwid"
    }

    fun toggleTajweed(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Tajweed Colors"
        TranslationLanguage.INDONESIAN -> "Warna Tajwid"
    }

    fun toggleDarkMode(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Theme"
        TranslationLanguage.INDONESIAN -> "Tema"
    }

    fun toggleTransliteration(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Transliteration"
        TranslationLanguage.INDONESIAN -> "Transliterasi Latin"
    }

    fun toggleTranslation(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Translation"
        TranslationLanguage.INDONESIAN -> "Terjemahan"
    }

    fun toggleMemorization(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Memorization Mode"
        TranslationLanguage.INDONESIAN -> "Mode Hafalan"
    }

    fun languageLabel(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Language: English"
        TranslationLanguage.INDONESIAN -> "Bahasa: Indonesia"
    }

    fun tapToSwitchLanguage(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "Switch to Bahasa Indonesia"
        TranslationLanguage.INDONESIAN -> "Ganti ke English"
    }

    fun onText(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "ON"
        TranslationLanguage.INDONESIAN -> "ON"
    }

    fun offText(lang: TranslationLanguage): String = when (lang) {
        TranslationLanguage.ENGLISH -> "OFF"
        TranslationLanguage.INDONESIAN -> "OFF"
    }
}
