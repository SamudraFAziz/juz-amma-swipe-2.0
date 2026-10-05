package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.data.JuzAmmaRepository
import com.example.data.TranslationLanguage
import com.example.model.TajweedRuleType
import com.example.ui.components.VersePageContent
import com.example.ui.theme.JuzAmmaTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    val verse = JuzAmmaRepository.allVerses.first()
    val surah = JuzAmmaRepository.surahs.first()

    composeTestRule.setContent {
      JuzAmmaTheme(darkTheme = true) {
        VersePageContent(
          verse = verse,
          surah = surah,
          isTajweedEnabled = true,
          showTransliteration = true,
          showTranslation = true,
          translationLanguage = TranslationLanguage.INDONESIAN,
          isMemorizationMode = false,
          isBookmarked = false,
          isPlaying = false,
          onPlayPauseClicked = {},
          onReplayClicked = {},
          onBookmarkToggled = {},
          onRuleClicked = {},
          onOpenVersePicker = {}
        )
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}
