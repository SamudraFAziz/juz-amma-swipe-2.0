package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.JuzAmmaRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read app name string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Juz Amma Swipe", appName)
    }

    @Test
    fun `verify juz amma repository surahs count`() {
        val surahs = JuzAmmaRepository.surahs
        assertEquals(37, surahs.size)
        assertEquals(78, surahs.first().id)
        assertEquals("An-Naba", surahs.first().nameTransliteration)
        assertEquals(114, surahs.last().id)
        assertEquals("An-Nas", surahs.last().nameTransliteration)
    }

    @Test
    fun `verify verses audio url format`() {
        val verse = JuzAmmaRepository.allVerses.first()
        assertEquals("https://everyayah.com/data/Alafasy_128kbps/078001.mp3", verse.getAlafasyAudioUrl())
    }

    @Test
    fun `verify surah sort order persistence in UserPreferencesManager`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val prefs = com.example.data.UserPreferencesManager(context)
        // Default should be ASCENDING (78 -> 114)
        assertEquals(com.example.model.SurahSortOrder.ASCENDING, prefs.getSurahSortOrder())

        // Save DESCENDING (114 -> 78)
        prefs.saveSurahSortOrder(com.example.model.SurahSortOrder.DESCENDING)
        assertEquals(com.example.model.SurahSortOrder.DESCENDING, prefs.getSurahSortOrder())

        // Reset back to ASCENDING
        prefs.saveSurahSortOrder(com.example.model.SurahSortOrder.ASCENDING)
        assertEquals(com.example.model.SurahSortOrder.ASCENDING, prefs.getSurahSortOrder())
    }

    @Test
    fun `verify surahs list sorting both ascending and descending`() {
        val surahs = JuzAmmaRepository.surahs
        val ascendingList = surahs.sortedBy { it.id }
        assertEquals(78, ascendingList.first().id)
        assertEquals(114, ascendingList.last().id)

        val descendingList = surahs.sortedByDescending { it.id }
        assertEquals(114, descendingList.first().id)
        assertEquals(78, descendingList.last().id)
    }
}
