package com.example.data

import android.content.Context
import android.content.SharedPreferences

class UserPreferencesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveLastRead(surahId: Int, verseNumber: Int, globalIndex: Int) {
        prefs.edit()
            .putInt(KEY_LAST_SURAH_ID, surahId)
            .putInt(KEY_LAST_VERSE_NUMBER, verseNumber)
            .putInt(KEY_LAST_GLOBAL_INDEX, globalIndex)
            .putLong(KEY_LAST_TIMESTAMP, System.currentTimeMillis())
            .commit()
    }

    fun getLastReadSurahId(): Int = prefs.getInt(KEY_LAST_SURAH_ID, 78)

    fun getLastReadVerseNumber(): Int = prefs.getInt(KEY_LAST_VERSE_NUMBER, 1)

    fun getLastReadGlobalIndex(): Int = prefs.getInt(KEY_LAST_GLOBAL_INDEX, 0)

    fun hasLastRead(): Boolean = prefs.contains(KEY_LAST_SURAH_ID)

    fun getBookmarkedIndexes(): Set<Int> {
        val stringSet = prefs.getStringSet(KEY_BOOKMARKS, emptySet()) ?: emptySet()
        return stringSet.mapNotNull { it.toIntOrNull() }.toSet()
    }

    fun saveBookmarkedIndexes(indexes: Set<Int>) {
        val stringSet = indexes.map { it.toString() }.toSet()
        prefs.edit().putStringSet(KEY_BOOKMARKS, stringSet).commit()
    }

    fun getSavedTranslationLanguage(): TranslationLanguage? {
        val langStr = prefs.getString(KEY_LANGUAGE, null) ?: return null
        return try {
            TranslationLanguage.valueOf(langStr)
        } catch (_: Exception) {
            null
        }
    }

    fun saveTranslationLanguage(language: TranslationLanguage) {
        prefs.edit().putString(KEY_LANGUAGE, language.name).commit()
    }

    fun getSavedDarkMode(): Boolean? {
        return if (prefs.contains(KEY_DARK_MODE)) {
            prefs.getBoolean(KEY_DARK_MODE, false)
        } else {
            null
        }
    }

    fun saveDarkMode(isDark: Boolean) {
        prefs.edit().putBoolean(KEY_DARK_MODE, isDark).apply()
    }

    companion object {
        private const val PREFS_NAME = "juz_amma_swipe_prefs"
        private const val KEY_LAST_SURAH_ID = "last_surah_id"
        private const val KEY_LAST_VERSE_NUMBER = "last_verse_number"
        private const val KEY_LAST_GLOBAL_INDEX = "last_global_index"
        private const val KEY_LAST_TIMESTAMP = "last_timestamp"
        private const val KEY_BOOKMARKS = "bookmarked_verse_indexes"
        private const val KEY_LANGUAGE = "app_translation_language"
        private const val KEY_DARK_MODE = "app_dark_mode"
    }
}
