package com.example.model

data class Surah(
    val id: Int,                         // Surah Number (78..114 for Juz Amma)
    val nameArabic: String,             // e.g., "النبأ"
    val nameTransliteration: String,    // e.g., "An-Naba"
    val nameTranslation: String,        // e.g., "The Tidings"
    val totalVerses: Int,               // e.g., 40
    val revelationType: RevelationType, // MECCAN or MEDINAN
    val startVerseIndex: Int            // Index of first verse in global Juz Amma list
)

enum class RevelationType(val title: String, val iconText: String) {
    MECCAN("Meccan", "مكّيّة"),
    MEDINAN("Medinan", "مدنيّة")
}

data class Verse(
    val globalIndex: Int,                // 0-based index across all Juz Amma verses (~564 verses)
    val surahNumber: Int,               // 78..114
    val surahNameTransliteration: String,
    val verseNumber: Int,                // 1-based verse number in Surah
    val arabicText: String,             // Full Hafs Arabic script
    val taggedArabicText: String,       // Text with inline XML-like tags e.g. <ghunnah>عَمَّ</ghunnah>
    val transliteration: String,        // English phonetic pronunciation
    val translation: String,            // Saheeh International translation
    val tajweedRules: List<TajweedRuleType>, // Tajweed rules highlighted in this verse
    val isBismillahNeeded: Boolean = false // True if Ayah 1 of Surah (except Surah 9 or Fatiha)
) {
    /**
     * Mishary Rashid Alafasy audio URL formatted for 128kbps recitation
     */
    fun getAlafasyAudioUrl(): String {
        val surahStr = surahNumber.toString().padStart(3, '0')
        val ayahStr = verseNumber.toString().padStart(3, '0')
        return "https://everyayah.com/data/Alafasy_128kbps/${surahStr}${ayahStr}.mp3"
    }

    /**
     * Backup Islamic Network CDN audio URL
     */
    fun getBackupAudioUrl(): String {
        val surahStr = surahNumber.toString().padStart(3, '0')
        val ayahStr = verseNumber.toString().padStart(3, '0')
        return "https://mirrors.quranicaudio.com/everyayah/Alafasy_128kbps/${surahStr}${ayahStr}.mp3"
    }
}

data class UserBookmark(
    val surahNumber: Int,
    val verseNumber: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val note: String = ""
)
