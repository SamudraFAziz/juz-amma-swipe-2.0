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
        assertEquals("Juz Amma", appName)
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
}
